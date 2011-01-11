/*
 *  Copyright 2011 SuikaSoft.
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

package org.specs.LoopOptimization;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.DataStructures.CodeSegment;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.MicroBlaze.MbAssemblyAnalyser;
import org.specs.DymaLib.MicroBlaze.MbGraphBuilder;
import org.specs.DymaLib.MicroBlaze.MbLowLevelParser;
import org.specs.DymaLib.MicroBlaze.MbVbiParser;
import org.specs.DymaLib.Stats.SllAnalyser;
import org.specs.DymaLib.AssemblyAnalyser;
import org.specs.DymaLib.DataStructures.VbiAnalysis;
import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;
import org.specs.DymaLib.Utils.VbiAnalyser;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MicroBlazeParser;

/**
 *
 * @author Joao Bispo
 */
public class LoopOptimizationApplication implements App {

   public int execute(File setupFile) throws InterruptedException {
      // Get block files to process

      Setup setup = (Setup)IoUtils.readObject(setupFile);

      // Get serialized files
      List<File> serializedBlocks = getSerializedFiles(setup);

      // Get object
      //List<CodeSegment> loops = new ArrayList<CodeSegment>();
      System.out.println("Processing "+serializedBlocks.size()+" files.");
      for(File file : serializedBlocks) {
         System.out.println("File '"+file.getName()+"':");
         CodeSegment codeSegment = (CodeSegment)IoUtils.readObject(file);
         if(codeSegment == null) {
            continue;
         }

         //loops.add(codeSegment);
         processLoop(codeSegment);
      }

      /*
      for(CodeSegment loop : loops) {
               processLoop(loop);
      }
*/

      return 0;
   }

   public Collection<EnumKey> getEnumKeys() {
      return BaseUtils.extractEnumValues(LoopOptimizationOptions.class);
   }

   private List<File> getSerializedFiles(Setup setup) {

      List<File> files = InputType.getFiles(setup, LoopOptimizationOptions.InputPath,
              LoopOptimizationOptions.PathType);

      // Filter files
      List<File> serializedBlocks = new ArrayList<File>();
      for (File file : files) {
         if (!file.getName().endsWith(LoopDiskWriter.SERIALIZED_BLOCK_EXTENSION)) {
            continue;
         }

         serializedBlocks.add(file);
      }

      return serializedBlocks;
   }

   private void processLoop(CodeSegment loop) {

      // Build MicroBlaze instructions cache
      List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
              loop.getAddresses(), loop.getInstructions());

      // Gather complete pass analysis data
      AssemblyAnalyser assAnal = MbAssemblyAnalyser.create(loop.getRegisterValues(), mbInstructions);
//      System.out.println(assAnal);
      //showPreAnalysisInfo(assAnal);


      MbVbiParser vbiParser = new MbVbiParser(assAnal.getLiveOuts(), assAnal.getConstantRegisters());
      List<VeryBigInstruction32> vbis = new ArrayList<VeryBigInstruction32>();
      for(MbInstruction mbInstruction : mbInstructions) {
         VeryBigInstruction32 vbi = vbiParser.parseInstruction(mbInstruction);
         if(vbi == null) {
            LoggingUtils.getLogger().
                    warning("Returned null VBI. This may impact operations which"
                    + " use line number information from pre-analysis.");
            continue;
         }
         vbis.add(vbi);

         //testNop(mbInstruction, vbi);
      }

      VbiAnalysis vbiAnalysis = VbiAnalyser.getData(vbis, MbInstructionName.add, new MbGraphBuilder());
//      System.out.println(vbiAnalysis);
      //showVbiInfo(vbis);
      // Expand instructions into very big instructions

      // Characterize the loop
      LowLevelParser lowLevelParser = new MbLowLevelParser();
      List<LowLevelInstruction> llInsts =
              lowLevelParser.parseInstructions(loop.getAddresses(), loop.getInstructions());

      SllAnalyser analysis = SllAnalyser.analyse(llInsts);

      //System.out.println(analysis);

      //System.out.println("Register Values:");
      //System.out.println(loop.getRegisterValues());
   }

   private void testNop(MbInstruction mbInstruction, VeryBigInstruction32 vbi) {
      boolean isNop = mbInstruction.isMbNop();
      if (isNop) {
         System.err.println("NOP!:" + mbInstruction);
         System.out.println("Resulting VBI:" + vbi);
      }
   }

/*
   private void showVbiInfo(List<VeryBigInstruction32> vbis) {
      Collection<String> loadStoreInstNames = new ArrayList<String>();
      for(MbInstructionName instName : InstructionProperties.LOAD_INSTRUCTIONS) {
         loadStoreInstNames.add(instName.name());
      }
      for(MbInstructionName instName : InstructionProperties.STORE_INSTRUCTIONS) {
         loadStoreInstNames.add(instName.name());
      }

      Map<String,Integer> histogram = VbiAnalyser.getInstructionsHistogram(vbis, loadStoreInstNames);
      // Extract num of loads and stores
      int numLoadStoreInst = 0;
      for(String key : histogram.keySet()) {
         numLoadStoreInst += histogram.get(key);
      }


      int mappableInstructions = VbiAnalyser.getMappableInstructions(vbis);
      int cpl = VbiAnalyser.getCriticalPathLenght(vbis, new MbGraphBuilder());


      System.out.println("#Mappable Instructions  :"+mappableInstructions);
      System.out.println("#Load/Store Instructions:"+numLoadStoreInst);
      System.out.println("Critical Path Lenght    :"+cpl);
      System.out.println(" ");
      /*
      System.out.println("VBIs size:"+vbis.size());
      for(VeryBigInstruction32 vbi : vbis) {
         System.out.println(vbi);
      }


      System.out.print("Critical Path Length:"+VbiAnalyser.getCriticalPathLenght(vbis, new MbGraphBuilder()));
       *
       */
//   }

        /*
   private void showPreAnalysisInfo(AssemblyAnalyser assAnal) {
      System.out.println("Constant Registers:");
      System.out.println(assAnal.getConstantRegisters());
      System.out.println("Live Outs:");
      System.out.println(assAnal.getLiveOuts());
      System.out.println(" ");
   }
*/
}
