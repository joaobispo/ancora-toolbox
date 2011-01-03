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

package org.specs.GraphicalTraceV4;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DymaLib.Dotty.DottyTraceUnit;
import org.specs.DymaLib.MicroBlaze.FW_3SP_Decoder;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.TraceReader;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.Jani.SimpleGui;

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
        gui.setTitle("Graphical Trace v0.3");
        gui.execute();
    }


   public int execute(File setupFile) throws InterruptedException {
            Setup options = (Setup)IoUtils.readObject(setupFile);

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

   public Collection<EnumKey> getEnumKeys() {
      return BaseUtils.extractEnumValues(Options.class);
   }

   private void processFile(File inputFile) {
      SystemSetup setup = new SystemSetup(systemConfigSetup);
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

   private boolean init(Setup options) {
      inputFiles = getFiles(options);
      if(inputFiles == null) {
         return false;
      }
      

      outputFolder = BaseUtils.getFolder(options.get(Options.OutputFolder));
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder.");
         return false;
      }

      // Get trace unit
      String traceUnitString = BaseUtils.getString(options.get(Options.TraceUnit));
      traceUnitName = EnumUtils.valueOf(TraceUnits.class, traceUnitString);
      if(traceUnitName == null) {
          LoggingUtils.getLogger().
                 warning("Could not get Trace Unit name.");
         return false;
      }

      systemConfigSetup = BaseUtils.getSetup(options.get(Options.SystemSetup));

      return true;
   }

   /**
    * Check if input is is single file or folder
    * @param options
    * @return
    */
   private List<File> getFiles(Setup options) {
      String inputTypeName = BaseUtils.getString(options.get(Options.InputType));
      InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      return InputType.getFiles(options, Options.Input, inputType);
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<File> inputFiles;
   private File outputFolder;
   private TraceUnits traceUnitName;
   private Setup systemConfigSetup;
   private final static InstructionDecoder instructionDecoder = new FW_3SP_Decoder();


}
