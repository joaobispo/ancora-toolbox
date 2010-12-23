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
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.LoopDetection.CodeSegment;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.MicroBlaze.MbImplementation;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;
import org.specs.LoopDetection.SegmentProcessorJobs.LoopDetectionJobs;
import org.specs.LoopDetection.SegmentProcessor.SegmentProcessor;
//import org.specs.LoopDetection.SegmentProcessor.LoopProcessorResults;

/**
 * Detects and extract loops.
 * 
 * <p>The purpose of this class is to handle the configuration of the program,
 * and passing that configuration to the appropriate objects.
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
            // Build JobInfo
            File elfFile = inputFiles.get(fileIndex);
            String detectorName = loopDetectorNames.get(detectorIndex);
            File detectorSetup = loopDetectorSetups.get(detectorIndex);
            LoopDetectionInfo jobInfo = new LoopDetectionInfo(elfFile, outputFolder, detectorName, detectorSetup, processor);
//            detectLoops(fileIndex, detectorIndex);
            //detectLoops(jobInfo);
            detectLoops2(jobInfo);
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
      File systemConfigFile = new File(systemConfigFilename);
      if (!systemConfigFile.exists()) {
         LoggingUtils.getLogger().
                 info("MicroBlaze setup file '" + systemConfigFilename + "' not found.");
         return false;
      }

      systemSetup = SystemSetup.buildConfig(systemConfigFile);
      if (systemSetup == null) {
         systemSetup = SystemSetup.getDefaultConfig();
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


   //private void detectLoops(int fileIndex, int detectorIndex) throws InterruptedException {
   private void detectLoops(LoopDetectionInfo jobInfo) throws InterruptedException {
      //File elfFile = inputFiles.get(fileIndex);
      //String detectorName = loopDetectorNames.get(detectorIndex);
      //File detectorSetup = loopDetectorSetups.get(detectorIndex);
      File elfFile = jobInfo.elfFile;
      String detectorName = jobInfo.detectorName;
      File detectorSetup = jobInfo.detectorSetup;

      LoopDetector loopDetector = LoopUtils.newLoopDetector(detectorName,
              detectorSetup, processor.getInstructionDecoder());
//              detectorSetup, jobInfo.processor.getInstructionDecoder());
      if (loopDetector == null) {
         return;
      }

      // Stats
      dotty = new DottyLoopUnit();
      loopInstCount = 0;
      removedInstCount = 0l;


      // Instantiate System
      /*
      SystemSetup setup = SystemSetup.buildConfig(systemConfigFile);
      if (setup == null) {
         setup = SystemSetup.getDefaultConfig();
      }
       * 
       */

      //TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);
      TraceReader traceReader = DToolReader.newDToolReader(elfFile, systemSetup);
      String instruction = null;
      int traceCount = 0;
      //boolean isStraighLineLoop = detectorName.equals(LoopDetectors.MegaBlock.name());
      //Enum[] instructionNames = processor.getInstructionNames();
      String baseFilename = ParseUtils.removeSuffix(elfFile.getName(), ".");
/*
      LoopDiskWriter loopWriter = new LoopDiskWriter(outputFolder, baseFilename,
              detectorSetup.getName(), processor.getLowLevelParser(), diskWriterSetup, isStraighLineLoop,
              instructionNames);
*/
      // Create loopWriter
      //LoopDiskWriter loopWriter = newLoopDiskWriter(elfFile, setup, detectorName, detectorSetup);
      LoopDiskWriter loopWriter = newLoopDiskWriter(elfFile, systemSetup, detectorName, detectorSetup);

      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<CodeSegment> loops = loopDetector.getAndClearUnits();
         processLoops(loops);
         loopWriter.addLoops(loops);
         

         // Check if work should be interrupted
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Application Interrupted");
         }
      }

      loopDetector.close();
      List<CodeSegment> loops = loopDetector.getAndClearUnits();
      processLoops(loops);

      
      loopWriter.addLoops(loops);
      
      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);

      // Build Dotty
      if(writeDotFilesForEachElfProgram) {
//         buildDotty(fileIndex, detectorIndex, dotty);
         buildDotty(elfFile, detectorSetup, outputFolder, dotty);
      }

      System.out.println("Executed Instructions:"+baseFilename+"\t"+traceCount);
      // Show total instructions vs removed instructions
 //     System.out.println(baseFilename+"\t"+traceCount+"\t"+removedInstCount);
      //System.out.println("Total Instructions:"+traceCount);
      //System.out.println("After Transformations:"+(traceCount-removedInstCount));
      //System.out.println("Removed Instructions:"+removedInstCount);
   }


   private void processLoops(List<CodeSegment> loops) {
      if(loops == null) {
         return;
      }


      for(CodeSegment unit : loops) {
         loopInstCount += unit.getTotalInstructions();
         dotty.addUnit(unit);
         if(unit.isLoop()) {
            //loopCollector.addLoop(unit);
         }
         
 //        removedInstCount += LoopMapping.removedInst(unit);
      }

   }

   private void testInstructionNumber(int traceCount, int loopCount) {
      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }
   }

      //private void buildDotty(int fileIndex, int detectorIndex, DottyLoopUnit dotty) {
      private void buildDotty(File elfFile, File detectorSetup, File outputFolder, DottyLoopUnit dotty) {
         // Build name
         //String inputFilename = inputFiles.get(fileIndex).getName();
         String inputFilename = elfFile.getName();
         inputFilename = ParseUtils.removeSuffix(inputFilename, ".");
         inputFilename = inputFilename + "." + detectorSetup.getName();
         inputFilename = inputFilename + ".dotty";
         
         IoUtils.write(new File(outputFolder, inputFilename), dotty.generateDot());
   }

   /**
    *
    */
   private List<File> inputFiles;
   private File outputFolder;
   //private File systemConfigFile;
   private SystemSetup systemSetup;
   private boolean writeDotFilesForEachElfProgram;
   private List<String> loopDetectorNames;
   private List<File> loopDetectorSetups;
   private DiskWriterSetup diskWriterSetup;
   private ProcessorImplementation processor;

   // STATS
   private DottyLoopUnit dotty;
   private int loopInstCount;
   private long removedInstCount;

   private LoopDiskWriter newLoopDiskWriter(File elfFile, SystemSetup setup,
           String detectorName, File detectorSetup) {

      boolean isStraighLineLoop = detectorName.equals(LoopDetectors.MegaBlock.name());
      Enum[] instructionNames = processor.getInstructionNames();
      String baseFilename = ParseUtils.removeSuffix(elfFile.getName(), ".");

      LoopDiskWriter loopWriter = new LoopDiskWriter(outputFolder, baseFilename,
              detectorSetup.getName(), processor.getLowLevelParser(), diskWriterSetup, isStraighLineLoop,
              instructionNames);

      return loopWriter;
   }

   private void detectLoops2(LoopDetectionInfo jobInfo) throws InterruptedException {
      SegmentProcessor worker = new SegmentProcessor();
      //LoopProcessor worker = new LoopProcessor(jobInfo);
      //LoopProcessor worker = LoopProcessor.newLoopWorker(jobInfo, systemSetup);
      LoopDetectionJobs loopProcessors =
              LoopDetectionJobs.newLoopProcessors(diskWriterSetup, jobInfo);
      worker.getLoopProcessors().addAll(loopProcessors.asList());

      //worker.getLoopProcessors().addAll(buildLoopProcessors(jobInfo));
      
      // Build LoopDetector
      LoopDetector loopDetector = LoopUtils.newLoopDetector(jobInfo.detectorName,
              jobInfo.detectorSetup, jobInfo.processor.getInstructionDecoder());

      if (loopDetector == null) {
         LoggingUtils.getLogger().
                 warning("Could not create LoopDetector");
         return;
      }

      // Build TraceReader
      TraceReader traceReader = DToolReader.newDToolReader(jobInfo.elfFile, systemSetup);
      

//      LoopProcessorResults results = worker.run(traceReader, loopDetector);
      int executedInst = worker.run(traceReader, loopDetector);

      // Process results
      processResults(jobInfo, loopProcessors);

      String baseFilename = ParseUtils.removeSuffix(jobInfo.elfFile.getName(), ".");
      //System.out.println("Executed Instructions:"+baseFilename+"\t"+results.executedInstructions);
      System.out.println("Executed Instructions:"+baseFilename+"\t"+executedInst);
   }

   private void processResults(LoopDetectionInfo jobInfo, LoopDetectionJobs loopProcessors) {
      // Write Dotty
      if(writeDotFilesForEachElfProgram) {  
         buildDotty(jobInfo.elfFile, jobInfo.detectorSetup, jobInfo.outputFolder, loopProcessors.dottyWriter.getDotty());
      }
   }

   /*
   private List<LoopProcessor> buildLoopProcessors(LoopJobInfo jobInfo) {
      List<LoopProcessor> list = new ArrayList<LoopProcessor>();

      // Disk Writer
      LoopWriter loopWriter = LoopWriter.newLoopWriter(diskWriterSetup, jobInfo);
      if(loopWriter != null) {
         list.add(loopWriter);
      }
      
      return list;
   }
    *
    */

}
