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
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DymaLib.DToolReader;
import org.specs.DToolPlus.DymaLib.FW_3SP.FW_3SP_Decoder;
import org.specs.DToolPlus.FW_3SP.InstructionDecode;
import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockDetector;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class LoopDetection implements App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();
       LoopDetection loopDetection = new LoopDetection();
       SimpleGui simpleGui = new SimpleGui(loopDetection);
       simpleGui.setTitle("Loop Detection in MicroBlaze programs v1.0");
       simpleGui.execute();
    }

   public int execute(Map<String, AppValue> options) throws InterruptedException {
      // Initialize
      boolean success = init(options);
      if(!success) {
         return -1;
      }

      detectLoops();

      return 0;
   }

   public Class getAppOptionEnum() {
      return AppOptions.class;
   }

   private boolean init(Map<String, AppValue> options) {
      elfFile = AppUtils.getExistingFile(options, AppOptions.Program);
      if (elfFile == null) {
         LoggingUtils.getLogger().
                 warning("Could not open program file.");
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
      List<String> loopDetectors = AppUtils.getStringList(options, AppOptions.LoopDetector);
      if(loopDetectors == null) {
         System.out.println("Could not get loop detector configuration.");
         return false;
      }
      initLoopDetectors(loopDetectors);

      return true;
   }


   private void initLoopDetectors(List<String> loopDetectors) {
      this.loopDetectors = new ArrayList<LoopDetector>();
      loopDetectorNames = new ArrayList<String>();

      for(String loopDetectorName : loopDetectors) {
         List<Object> returnValues = AppUtils.unpackSetup(loopDetectorName);
         String name = (String)returnValues.get(0);
         File setup = (File)returnValues.get(1);

         LoopDetector loopDetector = LoopUtils.newLoopDetector(name, setup, DECODER);
         if(loopDetector == null) {
            continue;
         }

         this.loopDetectors.add(loopDetector);
         loopDetectorNames.add(name);
      }

   }

   private void detectLoops() throws InterruptedException {
      // Instantiate System
      SystemSetup setup = SystemSetup.buildConfig(systemConfigFile);
      if(setup == null) {
         setup = SystemSetup.getDefaultConfig();
      }

      TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);
      //UnitBuilder unitBuilder = UnitBuilderFactory.newUnitBuilder(TraceUnits.SuperBlock, new FW_3SP_Decoder());
      //int maxPatternSize = 32;
      //LoopDetector loopDetector = new MegaBlockDetector(unitBuilder, maxPatternSize, false, false);
      for (LoopDetector loopDetector : loopDetectors) {


         String instruction = null;
         int traceCount = 0;
         while ((instruction = traceReader.nextInstruction()) != null) {
            traceCount++;
            int address = traceReader.getAddress();
            loopDetector.step(address, instruction);




            // Check if work should be interrupted
            if (Thread.currentThread().isInterrupted()) {
               throw new InterruptedException("Application Interrupted");
            }
         }

         loopDetector.close();


         // TEST
         DottyLoopUnit dotty = new DottyLoopUnit();

         int loopCount = 0;
         for (LoopUnit unit : loopDetector.getAndClearUnits()) {
            System.out.println(unit);
            loopCount += unit.getTotalInstructions();
            dotty.addUnit(unit);
         }

         if (traceCount != loopCount) {
            LoggingUtils.getLogger().
                    warning("Trace instruction count (" + traceCount + ") different "
                    + "from loop instruction count (" + loopCount + ").");
         }

         //System.out.println("Total Given Inst:"+traceCount);
         //System.out.println("Total Out Inst:"+loopCount);
         //System.out.println(((MegaBlockDetector)loopDetector).loopUnit);
         IoUtils.write(new File("E:/mega.dotty"), dotty.generateDot());
      }
   }

   /**
    *
    */
   private File elfFile;
   private File systemConfigFile;
   private List<LoopDetector> loopDetectors;
   private List<String> loopDetectorNames;

   /**
    * Decoder for MicroBlaze instructions.
    */
   public static final InstructionDecoder DECODER = new FW_3SP_Decoder();

}
