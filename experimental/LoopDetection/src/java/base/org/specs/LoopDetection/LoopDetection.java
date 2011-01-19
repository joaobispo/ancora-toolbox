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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.jfree.data.xy.XYSeriesCollection;
import org.specs.CoverageData.ChartWriter;
import org.specs.CoverageData.GlobalDataColector;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.MicroBlaze.MbImplementation;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;
import org.specs.LoopDetection.SegmentProcessorJobs.LoopDetectionJobs;
import org.specs.DymaLib.Utils.SegmentProcessor.SegmentProcessor;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.Jani.SimpleGui;

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
       simpleGui.setTitle("Loop Detection in MicroBlaze programs v0.7");
       simpleGui.execute();
    }

//   public int execute(Map<String, AppValue> options) throws InterruptedException {
   public int execute(File setupFile) throws InterruptedException {
      Setup options = (Setup)IoUtils.readObject(setupFile);

      // Initialize
      processor = new MbImplementation();
      boolean success = init(options);
      if(!success) {
         return -1;
      }

      for (int fileIndex = 0; fileIndex < inputFiles.size(); fileIndex++) {
         for(String loopDetectorRunName : loopDetectorSetups.keySet()) {
         //for (int detectorIndex = 0; detectorIndex < loopDetectorNames.size(); detectorIndex++) {
            // Build JobInfo
            File elfFile = inputFiles.get(fileIndex);
            //String detectorName = loopDetectorNames.get(detectorIndex);
            String detectorName = BaseUtils.decodeMapOfSetupsKey(loopDetectorRunName);
            //File detectorSetup = loopDetectorSetups.get(detectorIndex);
            //Setup detectorSetup = loopDetectorSetups.get(detectorIndex);
            Setup detectorSetup = loopDetectorSetups.get(loopDetectorRunName);

            // Build LoopDetector
            LoopDetector loopDetector = LoopUtils.newLoopDetector(detectorName,
              detectorSetup, processor.getInstructionDecoder());

            String loopDetectorId = loopDetector.getId();

            LoopDetectionInfo jobInfo = new LoopDetectionInfo(elfFile, outputFolder, 
                    //detectorName, detectorSetup, processor, loopDetectorId);
                    loopDetectorRunName, detectorSetup, processor, loopDetectorId);

            LoopDetectionJobs loopProcessors =
              LoopDetectionJobs.newLoopProcessors(diskWriterSetup, jobInfo);

            Integer executedInst = detectLoops(jobInfo, loopDetector, loopProcessors);

            // Print number of executed instructions
            String baseFilename = ParseUtils.removeSuffix(jobInfo.elfFile.getName(), ".");
            System.out.println("Executed Instructions:" + baseFilename + "\t" + executedInst);

            // Process results
            processResults(jobInfo, loopProcessors);
         }
      }
      
      //System.out.println("Global data");
      //System.out.println(globalDataColector.getMainTable());
      XYSeriesCollection collection = globalDataColector.processData();

      //ChartWriter chartWriter = ChartWriter.create(chartConfigFile);
      ChartWriter chartWriter = ChartWriter.create(chartSetup);
      chartWriter.createChart(collection, outputFolder);

      return 0;
   }
/*
   public Class getAppOptionEnum() {
      return AppOptions.class;
   }
   */
   public Collection<EnumKey> getEnumKeys() {
      return BaseUtils.extractEnumValues(AppOptions.class);
   }

   //private boolean init(Map<String, AppValue> options) {
   private boolean init(Setup options) {
      //String inputTypeName = BaseUtils.getString(options.get(AppOptions.InputType));
      //InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      //inputFiles = InputType.getFiles(options, AppOptions.ProgramFileOrFolder, inputType);
      inputFiles = InputType.getFiles(options, AppOptions.ProgramFileOrFolder, AppOptions.InputType);
      if(inputFiles == null) {
         return false;
      }
      

      outputFolder = BaseUtils.getFolder(options.get(AppOptions.OutputFolder));
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder.");
         return false;
      }


      //String systemConfigFilename = BaseUtils.getString(options.get(AppOptions.SystemSetup));
      Setup systemConfigFilename = BaseUtils.getSetup(options.get(AppOptions.SystemSetup));
      /*
      File systemConfigFile = new File(systemConfigFilename);
      if (!systemConfigFile.exists()) {
         LoggingUtils.getLogger().
                 info("MicroBlaze setup file '" + systemConfigFilename + "' not found.");
         return false;
      }
       *
       */

      //systemSetup = SystemSetup.buildConfig(systemConfigFile);
      systemSetup = new SystemSetup(systemConfigFilename);
      if (systemSetup == null) {
         System.out.println("Using default System Setup configuration.");
         systemSetup = SystemSetup.getDefaultConfig();
      }

      // Extract loop detectors
      //List<String> detectors = BaseUtils.getStringList(options.get(AppOptions.LoopDetector));
      Map<String, Setup> detectors = BaseUtils.getMapOfSetups(options.get(AppOptions.LoopDetector));
      if(detectors == null) {
         System.out.println("Could not get loop detector configuration.");
         return false;
      }
      loopDetectorSetups = detectors;
      //initLoopDetectors(detectors);


//      diskWriterSetup = (Setup) options.get(AppOptions.LoopWriterSetup).getRawValue();
      /*
      //File diskWriterSetupfile = BaseUtils.getExistingFile(options.get(AppOptions.LoopWriterSetup));
      if( diskWriterSetupfile == null) {
         System.out.println("Could not open disk writer setup file.");
         return false;
      }
*/
      //Setup dwSetup = (Setup) options.get(AppOptions.LoopWriterSetup).getRawValue();
      Setup dwSetup = BaseUtils.getSetup(options.get(AppOptions.LoopWriterSetup));

      //diskWriterSetup = DiskWriterSetup.newSetup(diskWriterSetupfile);
      diskWriterSetup = DiskWriterSetup.newSetup(dwSetup);

      writeDotFilesForEachElfProgram = BaseUtils.getBoolean(options.get(AppOptions.WriteDotFilesForEachElfProgram));

      //String chartConfigFilename = BaseUtils.getString(options.get(AppOptions.ChartSetup));
      chartSetup = BaseUtils.getSetup(options.get(AppOptions.ChartSetup));
      /*
      chartConfigFile = new File(chartConfigFilename);
      if (!chartConfigFile.exists()) {
         LoggingUtils.getLogger().
                 info("Chart setup file '" + systemConfigFilename + "' not found.");
         return false;
      }
       *
       */

      // Add option, null if disabled?
      globalDataColector = new GlobalDataColector();

      return true;
   }


   //private void initLoopDetectors(List<String> loopDetectors) {
   /*
   private void initLoopDetectors(Map<String, Setup> loopDetectors) {
      loopDetectorNames = new ArrayList<String>();
      //loopDetectorSetups = new ArrayList<File>();
      loopDetectorSetups = new ArrayList<Setup>();

      BaseUtils.parseMapOfSetups(loopDetectors, loopDetectorNames, loopDetectorSetups);
    *
    */
      //for(String loopDetectorName : loopDetectors) {
      /*
      for(String key : loopDetectors.keySet()) {
         Setup setup = loopDetectors.get(key);
         /*
         List<Object> returnValues = BaseUtils.unpackSetup(loopDetectorName);
         if(returnValues == null) {
            System.out.println("No loop detectors defined.");
         }
          *
          */
/*
         String name = (String)returnValues.get(0);
         File setup = (File)returnValues.get(1);
*/
  //       loopDetectorNames.add(name);
         /*
         String decodedLoopDetectorName = BaseUtils.decodeMapOfSetupsKey(key);
         //loopDetectorNames.add(loopDetectorName);
         loopDetectorNames.add(decodedLoopDetectorName);
         //loopDetectorSetups.add(loopDetectors.get(loopDetectorName));
         loopDetectorSetups.add(setup);
      }
          *
          */

   //}

      //private void buildDotty(File elfFile, File detectorSetup, File outputFolder, DottyLoopUnit dotty) {
      private void buildDotty(File elfFile, String detectorSetupRunName, File outputFolder, DottyLoopUnit dotty) {
         // Build name
         String inputFilename = elfFile.getName();
         inputFilename = ParseUtils.removeSuffix(inputFilename, ".");
         inputFilename = inputFilename + "." + detectorSetupRunName;
         inputFilename = inputFilename + ".dotty";
         
         IoUtils.write(new File(outputFolder, inputFilename), dotty.generateDot());
   }

   private Integer detectLoops(LoopDetectionInfo jobInfo, LoopDetector loopDetector,
           LoopDetectionJobs loopProcessors) throws InterruptedException {
   //private void detectLoops(LoopDetectionInfo jobInfo) throws InterruptedException {
      SegmentProcessor worker = new SegmentProcessor();
//      LoopDetectionJobs loopProcessors =
//              LoopDetectionJobs.newLoopProcessors(diskWriterSetup, jobInfo);
      worker.getLoopProcessors().addAll(loopProcessors.asList());

      // Build LoopDetector
      //LoopDetector loopDetector = LoopUtils.newLoopDetector(jobInfo.detectorName,
      //        jobInfo.detectorSetup, jobInfo.processor.getInstructionDecoder());

      if (loopDetector == null) {
         LoggingUtils.getLogger().
                 warning("Could not create LoopDetector");
         return null;
      }

      // Build TraceReader
      TraceReader traceReader = DToolReader.newDToolReader(jobInfo.elfFile, systemSetup);

      // Run LoopProcessor
      int executedInst = worker.run(traceReader, loopDetector);

      return executedInst;
   }

   private void processResults(LoopDetectionInfo jobInfo, LoopDetectionJobs loopProcessors) {
      // Write Dotty
      if(writeDotFilesForEachElfProgram) {
         //buildDotty(jobInfo.elfFile, jobInfo.detectorSetup, jobInfo.outputFolder, loopProcessors.dottyWriter.getDotty());
         buildDotty(jobInfo.elfFile, jobInfo.detectorRunName, jobInfo.outputFolder, loopProcessors.dottyWriter.getDotty());
      }

      // Add set of data to trace coverage
      globalDataColector.addTcData(loopProcessors.tcDataCollector, jobInfo);
   }

   /**
    *
    */
   private List<File> inputFiles;
   private File outputFolder;
   private SystemSetup systemSetup;
   private boolean writeDotFilesForEachElfProgram;
   //private List<String> loopDetectorNames;
   //private List<File> loopDetectorSetups;
   //private List<Setup> loopDetectorSetups;
   private Map<String, Setup> loopDetectorSetups;
   private DiskWriterSetup diskWriterSetup;
   private ProcessorImplementation processor;
   private GlobalDataColector globalDataColector;
   //private File chartConfigFile;
   private Setup chartSetup;

}
