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
import org.specs.DToolPlus.DymaLib.DToolReader;
import org.specs.DToolPlus.DymaLib.FW_3SP.FW_3SP_Decoder;
import org.specs.DToolPlus.DymaLib.LowLevelInstruction.MbLowLevelParser;
import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopCollector;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.Stats.SllAnalyser;

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
       simpleGui.setTitle("Loop Detection in MicroBlaze programs v0.3");
       simpleGui.execute();
    }

   public int execute(Map<String, AppValue> options) throws InterruptedException {
      // Initialize
      boolean success = init(options);
      if(!success) {
         return -1;
      }

      for (int fileIndex = 0; fileIndex < inputFiles.size(); fileIndex++) {
         //for (int detectorIndex = 0; detectorIndex < loopDetectors.size(); detectorIndex++) {
         for (int detectorIndex = 0; detectorIndex < loopDetectorNames.size(); detectorIndex++) {
            detectLoops(fileIndex, detectorIndex);
         }
      }
      /*
      for (LoopDetector loopDetector : loopDetectors) {
         detectLoops(loopDetector);
      }
       *
       */
      

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

      /*
      elfFile = AppUtils.getExistingFile(options, AppOptions.ProgramFileOrFolder);
      if (elfFile == null) {
         LoggingUtils.getLogger().
                 warning("Could not open program file.");
         return false;
      }
      */

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

      iterationsThreshold = AppUtils.getInteger(options, AppOptions.IterationsThreshold);
      if(iterationsThreshold == null) {
         System.out.println("Could not get iterations threshold.");
         return false;
      }

      return true;
   }


   private void initLoopDetectors(List<String> loopDetectors) {
      //this.loopDetectors = new ArrayList<LoopDetector>();
      loopDetectorNames = new ArrayList<String>();
      //loopDetectorTypes = new ArrayList<LoopDetectors>();
      loopDetectorSetups = new ArrayList<File>();

      for(String loopDetectorName : loopDetectors) {
         List<Object> returnValues = AppUtils.unpackSetup(loopDetectorName);
         String name = (String)returnValues.get(0);
         File setup = (File)returnValues.get(1);

         /*
         LoopDetector loopDetector = LoopUtils.newLoopDetector(name, setup, DECODER);
         if(loopDetector == null) {
            continue;
         }
          *
          */

         //this.loopDetectors.add(loopDetector);
         //loopDetectorNames.add(setup.getName());
         loopDetectorNames.add(name);
         //loopDetectorTypes.add(EnumUtils.valueOf(LoopDetectors.class, name));
         loopDetectorSetups.add(setup);
      }

   }


   //private void detectLoops(LoopDetector loopDetector) throws InterruptedException {
   private void detectLoops(int fileIndex, int detectorIndex) throws InterruptedException {
      File elfFile = inputFiles.get(fileIndex);
      String detectorName = loopDetectorNames.get(detectorIndex);
      File detectorSetup = loopDetectorSetups.get(detectorIndex);
      LoopDetector loopDetector = LoopUtils.newLoopDetector(detectorName,
              detectorSetup, DECODER);
      if (loopDetector == null) {
         return;
      }

      // Stats
      dotty = new DottyLoopUnit();
      loopCollector = new LoopCollector();
      loopInstCount = 0;

      //LoopDetector loopDetector = loopDetectors.get(detectorIndex);


      // Instantiate System
      SystemSetup setup = SystemSetup.buildConfig(systemConfigFile);
      if (setup == null) {
         setup = SystemSetup.getDefaultConfig();
      }

      TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);
      LowLevelParser lowLevelParser = new MbLowLevelParser();
      String instruction = null;
      int traceCount = 0;
      boolean isStraighLineLoop = detectorName.equals(LoopDetectors.MegaBlock.name());

      LoopDiskWriter loopWriter = new LoopDiskWriter(outputFolder, elfFile.getName(),
              detectorSetup.getName(), iterationsThreshold, lowLevelParser, isStraighLineLoop);
      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<LoopUnit> loops = loopDetector.getAndClearUnits();
         processLoops(loops);
         ///loopWriter.addLoops(loops, isStraighLineLoop);
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
      //loopWriter.addLoops(loops, isStraighLineLoop);
      
      //System.out.println("LoopUnits:"+loops.size());
      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);
      //testInstructionNumber(traceCount, loops);

      // Build Dotty
//      buildDotty(fileIndex, detectorIndex, loops);
      buildDotty(fileIndex, detectorIndex, dotty);

      // Characterize MegaBlocks
      /*
      if(detectorName.equals(LoopDetectors.MegaBlock.name())) {
         characterizeMegablocks(loopCollector);
      }
       *
       */

   }


   private void processLoops(List<LoopUnit> loops) {
      if(loops == null) {
         return;
      }

      for(LoopUnit unit : loops) {
         loopInstCount += unit.getTotalInstructions();
         dotty.addUnit(unit);
         if(unit.isLoop()) {
            loopCollector.addLoop(unit);
         }
      }
   }

   private void testInstructionNumber(int traceCount, int loopCount) {
   //private void testInstructionNumber(int traceCount, List<LoopUnit> loops) {
      /*
      int loopCount = 0;
      for (LoopUnit unit : loops) {
         loopCount += unit.getTotalInstructions();

      }
       *
       */

      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }

      //System.out.println("Trace Count:"+traceCount);
      //System.out.println("Loop Count:"+loopCount);
   }

      private void buildDotty(int fileIndex, int detectorIndex, DottyLoopUnit dotty) {
      //private void buildDotty(int fileIndex, int detectorIndex, List<LoopUnit> loops) {
/*
         DottyLoopUnit dotty = new DottyLoopUnit();
         for (LoopUnit unit : loops) {
            dotty.addUnit(unit);
         }
*/
         // Build name
         String inputFilename = inputFiles.get(fileIndex).getName();
         inputFilename = ParseUtils.removeSuffix(inputFilename, ".");
         //inputFilename = inputFilename + "." + loopDetectorNames.get(detectorIndex);
         inputFilename = inputFilename + "." + loopDetectorSetups.get(detectorIndex).getName();
         inputFilename = inputFilename + ".dotty";
         
         IoUtils.write(new File(outputFolder, inputFilename), dotty.generateDot());
   }

      /*
   private void characterizeLoop(LoopUnit unit) {
      if(!unit.isLoop()) {
         return;
      }

      List<MbInstruction> mbInsts = MicroBlazeParser.getMbInstructions(
              unit.getAddresses(), unit.getInstructions());
      processMbInsts(mbInsts);

   }
       *
       */
/*
   private void processMbInsts(List<MbInstruction> mbInsts) {
      for(MbInstruction mbInstruction : mbInsts) {
         System.out.println(mbInstruction);
      }
   }
*/

/*
   private void characterizeMegablocks(LoopCollector loopCollector) {
      for(LoopUnit megaBlock : loopCollector.getLoops()) {
         characterizeMegablock(megaBlock);
      }
   }
*/
      /*
   private void characterizeMegablock(LoopUnit megaBlock) {

      List<LowLevelInstruction> insts = MbLowLevelParser.getMbInstructions(
              megaBlock.getAddresses(), megaBlock.getInstructions());

      // Get information from the MegaBlock

      System.out.println("MegaBlock Start:");
      for(LowLevelInstruction instruction : insts) {
         System.out.println(instruction);
      }
      System.out.println("-------MegaBlock End-------");

      System.out.println(SllAnalyser.analyse(insts));
      System.out.println("---------------------------");
   }
*/

   /**
    *
    */
   private List<File> inputFiles;
   private File outputFolder;
   //private File elfFile;
   private File systemConfigFile;
   //private List<LoopDetector> loopDetectors;
   //private List<String> loopDetectorNames;
   //private List<LoopDetectors> loopDetectorTypes;
   private List<String> loopDetectorNames;
   private List<File> loopDetectorSetups;
   private Integer iterationsThreshold;

   // STATS
   private DottyLoopUnit dotty;
   private LoopCollector loopCollector;
   private int loopInstCount;

   /**
    * Decoder for MicroBlaze instructions.
    */
   public static final InstructionDecoder DECODER = new FW_3SP_Decoder();











}
