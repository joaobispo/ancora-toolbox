/*
 *  Copyright 2010 SPeCS Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.LoopDetection;

import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DymaLib.DToolReader;
import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.MbImplementation;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class LoopDetection implements App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       System.setSecurityManager(null);
       LoggingUtils.setupConsoleOnly();

       DToolUtils.prepareDtoolMicroblaze();

       LoopDetection loopDetection = new LoopDetection();
       SimpleGui simpleGui = new SimpleGui(loopDetection);
       simpleGui.setTitle("Loop Detection in MicroBlaze programs v0.5");
       simpleGui.execute();
    }

   public int execute(Map<String, AppValue> options) throws InterruptedException {
      // Initialize
      processor = new MbImplementation();
      boolean success = init(options);
      if(!success) {
         return -1;
      }

      for (int fileIndex = 0; fileIndex < inputFiles.size(); fileIndex++) {
         for (int detectorIndex = 0; detectorIndex < loopDetectorNames.size(); detectorIndex++) {
            detectLoops(fileIndex, detectorIndex);
         }
      }
      

      return 0;
   }

   public Class getAppOptionEnum() {
      return AppOptions.class;
   }

   private boolean init(Map<String, AppValue> options) {
      String inputTypeName = AppUtils.getString(options, AppOptions.InputType);
      InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      inputFiles = InputType.getFiles(options, AppOptions.ProgramFileOrFolder, inputType);
      if(inputFiles == null) {
         return false;
      }
      

      outputFolder = AppUtils.getFolder(options, AppOptions.OutputFolder);
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder.");
         return false;
      }


      String systemConfigFilename = AppUtils.getString(options, AppOptions.SystemSetup);
      systemConfigFile = new File(systemConfigFilename);
      if (!systemConfigFile.exists()) {
         LoggingUtils.getLogger().
                 info("MicroBlaze setup file '" + systemConfigFilename + "' not found.");
         return false;
      }

      // Extract loop detectors
      List<String> detectors = AppUtils.getStringList(options, AppOptions.LoopDetector);
      if(detectors == null) {
         System.out.println("Could not get loop detector configuration.");
         return false;
      }
      initLoopDetectors(detectors);

      File diskWriterSetupfile = AppUtils.getExistingFile(options, AppOptions.LoopWriterSetup);
      if( diskWriterSetupfile == null) {
         System.out.println("Could not open disk writer setup file.");
         return false;
      }
      diskWriterSetup = DiskWriterSetup.newSetup(diskWriterSetupfile);

      writeDotFilesForEachElfProgram = AppUtils.getBool(options, AppOptions.WriteDotFilesForEachElfProgram);

      return true;
   }


   private void initLoopDetectors(List<String> loopDetectors) {
      loopDetectorNames = new ArrayList<String>();
      loopDetectorSetups = new ArrayList<File>();

      for(String loopDetectorName : loopDetectors) {
         List<Object> returnValues = AppUtils.unpackSetup(loopDetectorName);
         String name = (String)returnValues.get(0);
         File setup = (File)returnValues.get(1);

         loopDetectorNames.add(name);
         loopDetectorSetups.add(setup);
      }

   }


   private void detectLoops(int fileIndex, int detectorIndex) throws InterruptedException {
      File elfFile = inputFiles.get(fileIndex);
      String detectorName = loopDetectorNames.get(detectorIndex);
      File detectorSetup = loopDetectorSetups.get(detectorIndex);
      LoopDetector loopDetector = LoopUtils.newLoopDetector(detectorName,
              detectorSetup, processor.getInstructionDecoder());
      if (loopDetector == null) {
         return;
      }

      // Stats
      dotty = new DottyLoopUnit();
      loopInstCount = 0;

      // Instantiate System
      SystemSetup setup = SystemSetup.buildConfig(systemConfigFile);
      if (setup == null) {
         setup = SystemSetup.getDefaultConfig();
      }

      TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);
      String instruction = null;
      int traceCount = 0;
      boolean isStraighLineLoop = detectorName.equals(LoopDetectors.MegaBlock.name());
      Enum[] instructionNames = processor.getInstructionNames();
      String baseFilename = ParseUtils.removeSuffix(elfFile.getName(), ".");

      LoopDiskWriter loopWriter = new LoopDiskWriter(outputFolder, baseFilename,
              detectorSetup.getName(), processor.getLowLevelParser(), diskWriterSetup, isStraighLineLoop,
              instructionNames);


      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<LoopUnit> loops = loopDetector.getAndClearUnits();
         processLoops(loops);
         loopWriter.addLoops(loops);
         

         // Check if work should be interrupted
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Application Interrupted");
         }
      }

      loopDetector.close();
      List<LoopUnit> loops = loopDetector.getAndClearUnits();
      processLoops(loops);

      
      loopWriter.addLoops(loops);
      
      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);

      // Build Dotty
      if(writeDotFilesForEachElfProgram) {
         buildDotty(fileIndex, detectorIndex, dotty);
      }
   }


   private void processLoops(List<LoopUnit> loops) {
      if(loops == null) {
         return;
      }

      for(LoopUnit unit : loops) {
         loopInstCount += unit.getTotalInstructions();
         dotty.addUnit(unit);
         if(unit.isLoop()) {
            //loopCollector.addLoop(unit);
         }
      }
   }

   private void testInstructionNumber(int traceCount, int loopCount) {

      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }

   }

      private void buildDotty(int fileIndex, int detectorIndex, DottyLoopUnit dotty) {
         // Build name
         String inputFilename = inputFiles.get(fileIndex).getName();
         inputFilename = ParseUtils.removeSuffix(inputFilename, ".");
         inputFilename = inputFilename + "." + loopDetectorSetups.get(detectorIndex).getName();
         inputFilename = inputFilename + ".dotty";
         
         IoUtils.write(new File(outputFolder, inputFilename), dotty.generateDot());
   }

   /**
    *
    */
   private List<File> inputFiles;
   private File outputFolder;
   private File systemConfigFile;
   private boolean writeDotFilesForEachElfProgram;
   private List<String> loopDetectorNames;
   private List<File> loopDetectorSetups;
   private DiskWriterSetup diskWriterSetup;
   private ProcessorImplementation processor;

   // STATS
   private DottyLoopUnit dotty;
   private int loopInstCount;

}
