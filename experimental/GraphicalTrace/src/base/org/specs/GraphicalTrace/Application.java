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

package org.specs.GraphicalTrace;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DToolPlus.Utilities.EasySystem;
import org.specs.DymaLib.Dotty.DottyTraceUnit;
import org.specs.DToolPlus.DymaLib.FW_3SP.FW_3SP_Decoder;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DToolPlus.DymaLib.DToolReader;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;
import system.SysteM;

/**
 *
 * @author Joao Bispo
 */
public class Application implements App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Disable security manager for Web Start
       System.setSecurityManager(null);

        LoggingUtils.setupConsoleOnly();

        // Check resources
        //checkResources();

        // Check if the files for running DToolPlus exist
        DToolUtils.prepareDtoolMicroblaze();
        //prepareDtoolMicroblaze();


        App app = new Application();
        SimpleGui gui = new SimpleGui(app);
        gui.setTitle("Graphical Trace v0.1");
        gui.execute();
    }

    /*
   private static void prepareDtoolMicroblaze() {
      for(String resource : microblazeDtoolFiles) {
         InputStream stream = IoUtils.resourceToStream(resource);
         File destination = new File(resource);
         IoUtils.copy(stream, destination);
      }

   }
     *
     */

   /*
   private static void checkResources() {
      for(String resource : microblazeDtoolFiles) {
         ClassLoader cl = Application.class.getClassLoader();
         URL fileUrl = cl.getResource(resource);
         try {
            File newFile = new File(fileUrl.toURI());
            System.out.println("File:"+newFile.getPath());
         } catch (URISyntaxException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
    * 
    */

   public int execute(Map<String, AppValue> options) throws InterruptedException {
      boolean success = init(options);
      if(!success) {
         return -1;
      }

    // Process each file
      for(File file : inputFiles) {
         processFile(file);
      }

      return 0;
   }

   public Class getAppOptionEnum() {
      return Options.class;
   }

   private void processFile(File inputFile) {
      SystemSetup setup = SystemSetup.buildConfig(systemConfigFile);
      if(setup == null) {
         setup = SystemSetup.getDefaultConfig();
      }

      //TraceReader traceReader = (TraceReader)newTraceReader(inputFile, setup);
      TraceReader traceReader = (TraceReader)DToolReader.newDToolReader(inputFile, setup);
      if(traceReader == null) {
         LoggingUtils.getLogger().
                 warning("traceReader is null.");
         return;
      }

      UnitBuilder builder = UnitBuilderFactory.newUnitBuilder(traceUnitName, instructionDecoder);
//      UnitBuilder builder = new InstructionBuilder();
 //     InstructionVerifier instVerifier = new InstructionVerifier(traceReader);

      DottyTraceUnit dotty = new DottyTraceUnit();

      String inst = null;
      //long totalInstructions = 0l;
      while((inst = traceReader.nextInstruction()) != null) {
         Integer address = traceReader.getAddress();
         builder.nextInstruction(address, inst);
         processTraceUnit(builder, dotty);
         //instVerifier.addInstructions();
         //totalInstructions +=
         // Feed instruction and address to Partitioner
         //System.out.println(traceReader.getAddress() + ": " +inst);
      }
      builder.close();
      processTraceUnit(builder, dotty);

      File outputFile = new File(outputFolder, inputFile.getName()+"."+traceUnitName+".dotty");
      IoUtils.write(outputFile, dotty.generateDot());
      //instVerifier.addInstructions(processTraceUnit(builder));
   }

   private long processTraceUnit(UnitBuilder builder, DottyTraceUnit dotty) {
      long totalInstructions = 0l;

      List<TraceUnit> traceUnits = builder.getAndClearUnits();
      if(traceUnits == null) {
         return totalInstructions;
      }

      for(TraceUnit traceUnit : traceUnits) {
         dotty.addUnit(traceUnit);
         /*
         System.out.println("Block "+traceUnit.getIdentifier());
         List<String> insts = traceUnit.getInstructions();
         List<Integer> addresses = traceUnit.getAddresses();
         for(int i=0; i<insts.size(); i++) {
            totalInstructions++;
            //System.out.println(addresses.get(i)+" "+insts.get(i));
         }
          *
          */
      }

      return totalInstructions;
   }

   /**
    * Instantiates a DToolReader loaded with an Elf file.
    *
    * @param elfFile
    * @return a DToolReader loaded with the given file, or null if the object
    * could not be created
    */
   /*
   public static DToolReader newTraceReader(File elfFile, SystemSetup setup) {
      String systemConfig = "./Configuration Files/systemconfig.xml";
      //String systemConfigResource = "Configuration Files/systemconfig.xml";
      //File systemFile = IoUtils.systemResourceToFile(systemConfigResource);
      //String systemConfig = systemFile.getPath();
      

      String elfFilename = elfFile.getPath();



      //SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, false);
      SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, setup);
      if(originalSystem == null) {
         LoggingUtils.getLogger().
                 warning("Could not create SysteM object.");
         return null;
      }
      EasySystem system = new EasySystem(originalSystem);
      DToolReader dtoolReader = new DToolReader(system);

      return dtoolReader;
   }
*/

   private boolean init(Map<String, AppValue> options) {
      inputFiles = getFiles(options);
      if(inputFiles == null) {
         return false;
      }
      

      outputFolder = AppUtils.getFolder(options, Options.OutputFolder);
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder.");
         return false;
      }

      // Get trace unit
      String traceUnitString = AppUtils.getString(options, Options.TraceUnit);
      traceUnitName = EnumUtils.valueOf(TraceUnits.class, traceUnitString);
      if(traceUnitName == null) {
          LoggingUtils.getLogger().
                 warning("Could not get Trace Unit name.");
         return false;
      }

      String systemConfigFilename = AppUtils.getString(options, Options.SystemSetup);
      systemConfigFile = new File(systemConfigFilename);
      if(!systemConfigFile.exists()) {
         LoggingUtils.getLogger().
                 info("MicroBlaze setup file '"+systemConfigFilename+"' not found.");
      }


      return true;
   }

   /**
    * Check if input is is single file or folder
    * @param options
    * @return
    */
   private List<File> getFiles(Map<String, AppValue> options) {
      String inputTypeName = AppUtils.getString(options, Options.InputType);
      InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      return InputType.getFiles(options, Options.Input, inputType);
      /*
      // Is Folder mode
      if (inputType == InputType.FilesInFolder) {
         File inputFolder = AppUtils.getFolder(options, Options.Input);
         if (inputFolder == null) {
            LoggingUtils.getLogger().
                    warning("Could not open folder.");
            return null;
         }
         return IoUtils.getFilesRecursive(inputFolder);
      }

      if (inputType == InputType.SingleFile) {
         File inputFile = AppUtils.getExistingFile(options, Options.Input);
         if (inputFile == null) {
            LoggingUtils.getLogger().
                    warning("Could not open file.");
            return null;
         }
         List<File> files = new ArrayList<File>();
         files.add(inputFile);
         return files;
      }

      LoggingUtils.getLogger().
              warning("Case not defined:'" + inputType + "'");
      return null;
       *
       */
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<File> inputFiles;
   private File outputFolder;
   private TraceUnits traceUnitName;
   private File systemConfigFile;
   private final static InstructionDecoder instructionDecoder = new FW_3SP_Decoder();

   /*
   public static final List<String> microblazeDtoolFiles = Arrays.asList(
            "Configuration Files/cpuconfig.dtd",
            "Configuration Files/deviceconfig.dtd",
            "Configuration Files/systemconfig.dtd",
            "Configuration Files/systemconfig.xml",
            "OPBDevices/OPBTimerCounter/OPBTimerCounter.xml",
            "OPBDevices/OPBUARTLite/OPBUARTLite.xml",
            "Processors/FW_3SP/FW_3SP.xml"
            );
*/
}
