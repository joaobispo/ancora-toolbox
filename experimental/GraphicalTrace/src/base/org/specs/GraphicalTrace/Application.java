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
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DymaLib.Dotty.DottyTraceUnit;
import org.specs.DToolPlus.DymaLib.FW_3SP.FW_3SP_Decoder;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DToolPlus.DymaLib.DToolReader;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;

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

        // Check if the files for running DToolPlus exist
        DToolUtils.prepareDtoolMicroblaze();


        App app = new Application();
        SimpleGui gui = new SimpleGui(app);
        gui.setTitle("Graphical Trace v0.2");
        gui.execute();
    }


   public int execute(Map<String, AppValue> options) throws InterruptedException {
      boolean success = init(options);
      if (!success) {
         return -1;
      }

      // Process each file
      for (File file : inputFiles) {
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

      TraceReader traceReader = (TraceReader)DToolReader.newDToolReader(inputFile, setup);
      if(traceReader == null) {
         LoggingUtils.getLogger().
                 warning("traceReader is null.");
         return;
      }

      UnitBuilder builder = UnitBuilderFactory.newUnitBuilder(traceUnitName, instructionDecoder);
      DottyTraceUnit dotty = new DottyTraceUnit();

      String inst = null;
      //long totalInstructions = 0l;
      while((inst = traceReader.nextInstruction()) != null) {
         Integer address = traceReader.getAddress();
         builder.nextInstruction(address, inst);
         processTraceUnit(builder, dotty);
      }
      builder.close();
      processTraceUnit(builder, dotty);

      File outputFile = new File(outputFolder, inputFile.getName()+"."+traceUnitName+".dotty");
      IoUtils.write(outputFile, dotty.generateDot());
   }

   private long processTraceUnit(UnitBuilder builder, DottyTraceUnit dotty) {
      long totalInstructions = 0l;

      List<TraceUnit> traceUnits = builder.getAndClearUnits();
      if(traceUnits == null) {
         return totalInstructions;
      }

      for(TraceUnit traceUnit : traceUnits) {
         dotty.addUnit(traceUnit);
      }

      return totalInstructions;
   }

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
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<File> inputFiles;
   private File outputFolder;
   private TraceUnits traceUnitName;
   private File systemConfigFile;
   private final static InstructionDecoder instructionDecoder = new FW_3SP_Decoder();

}
