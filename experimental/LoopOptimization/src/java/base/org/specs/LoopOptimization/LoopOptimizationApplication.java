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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.specs.DymaLib.MicroBlaze.Vbi.MbGraphBuilder;
import org.specs.DymaLib.MicroBlaze.Vbi.MbVbiParser;
import org.specs.DymaLib.Assembly.AssemblyAnalysis;
import org.specs.DymaLib.Vbi.Analysis.VbiAnalysis;
import org.specs.DymaLib.Dotty.DottyGraph;
import org.specs.DymaLib.MicroBlaze.Assembly.MbAssemblyUtils;
import org.specs.DymaLib.Vbi.Utils.GraphBuilder;
import org.specs.DymaLib.Vbi.Optimization.ConstantFoldingAndPropagation;
import org.specs.DymaLib.Vbi.Optimization.ConstantLoadsRemoval;
import org.specs.DymaLib.MicroBlaze.Vbi.MbSolver;
import org.specs.DymaLib.Vbi.Utils.Solver;
import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;
import org.specs.DymaLib.Vbi.Optimization.VbiOptimizer;
import org.specs.DymaLib.Vbi.Parser.VbiParser;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MicroBlazeParser;
import org.suikasoft.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class LoopOptimizationApplication implements App {

   public LoopOptimizationApplication() {
      nodeWeights = null;
   }



   public int execute(File setupFile) throws InterruptedException {
      /**
       * INIT
       */

      // Get block files to process
      Setup setup = (Setup)IoUtils.readObject(setupFile);

      // Get serialized files
      List<File> serializedBlocks = getSerializedFiles(setup);
      
      nodeWeights = getWeights(setup);

      outputFolder = BaseUtils.getFolder(setup.get(LoopOptimizationOptions.OutputFolder));
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder.");
         return -1;
      }

      // Get object
      //List<CodeSegment> loops = new ArrayList<CodeSegment>();
      MbSolver.reset();
      System.out.println("Processing "+serializedBlocks.size()+" files.");
      for(File file : serializedBlocks) {
         System.out.println("File '"+file.getName()+"':");
         /*
         CodeSegment codeSegment = (CodeSegment)IoUtils.readObject(file);
         if(codeSegment == null) {
            continue;
         }
          *
          */

         //loops.add(codeSegment);
         //processLoop(codeSegment);
         processLoop(file);
      }


      // What instructions are not yet supported?
      System.out.println("Instructions not yet supported by MbSolver which could be optimized by CFP:");
      System.out.println(MbSolver.operationsNotSupported);

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

   //private void processLoop(CodeSegment loop) {
   private void processLoop(File loopFile) {
      CodeSegment loop = (CodeSegment) IoUtils.readObject(loopFile);
      if (loop == null) {
         LoggingUtils.getLogger().
                 warning("Could not open loop for analysis");
         return;
      }


      // Build MicroBlaze instructions cache
      List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
              loop.getAddresses(), loop.getInstructions());

      // Gather complete pass analysis data
      //AssemblyAnalyser assAnal = MbAssemblyAnalyser.create(loop.getRegisterValues(), mbInstructions);
      AssemblyAnalysis asmData = MbAssemblyUtils.buildAssemblyAnalysis(loop.getRegisterValues(), mbInstructions);
      //System.out.println(assAnal);

      // Expand instructions into very big instructions
      //MbVbiParser vbiParser = new MbVbiParser(assAnal.getLiveOuts(), assAnal.getConstantRegisters());
      MbVbiParser vbiParser = new MbVbiParser(asmData);
      List<VeryBigInstruction32> vbis = getVbis(mbInstructions, vbiParser);

      GraphBuilder graphBuilder = new MbGraphBuilder(nodeWeights);
      GraphNode rootNode = graphBuilder.buildGraph(vbis);
      String dottyFilename = loopFile.getName() + ".dotty";
      IoUtils.write(new File(outputFolder, dottyFilename), DottyGraph.generateDotty(rootNode));

      //VbiAnalysis vbiAnalysisOriginal = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisOriginal = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      //System.out.println(vbiAnalysisOriginal);


      optimizeVbis(vbis);
      graphBuilder = new MbGraphBuilder(nodeWeights);
      rootNode = graphBuilder.buildGraph(vbis);

      //VbiAnalysis vbiAnalysisTransformed = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisTransformed = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      System.out.println(vbiAnalysisTransformed.diff(vbiAnalysisOriginal));

      dottyFilename = loopFile.getName() + ".after.dotty";
      IoUtils.write(new File(outputFolder, dottyFilename), DottyGraph.generateDotty(rootNode));
   }

   private List<VeryBigInstruction32> getVbis(List<?> mbInstructions,
           VbiParser vbiParser) {
      List<VeryBigInstruction32> vbis = new ArrayList<VeryBigInstruction32>();
      //for (MbInstruction mbInstruction : mbInstructions) {
      for (Object mbInstruction : mbInstructions) {
         VeryBigInstruction32 vbi = vbiParser.parseInstruction(mbInstruction);
         if (vbi == null) {
            LoggingUtils.getLogger().
                    warning("Returned null VBI. This may impact operations which"
                    + " use line number information from pre-analysis.");
            continue;
         }
         vbis.add(vbi);

         //testNop(mbInstruction, vbi);
         //testImm(mbInstruction, vbi);
      }

      return vbis;
   }

   private void testNop(MbInstruction mbInstruction, VeryBigInstruction32 vbi) {
      boolean isNop = mbInstruction.isMbNop();
      if (isNop) {
         System.err.println("NOP!:" + mbInstruction);
         System.out.println("Resulting VBI:" + vbi);
      }
   }

   private void testImm(MbInstruction mbInstruction, VeryBigInstruction32 vbi) {
      boolean isImm = mbInstruction.getInstructionName() == MbInstructionName.imm;
      if (isImm) {
         System.err.println("IMM!:" + mbInstruction);
         System.out.println("Resulting VBI:" + vbi);
      }
   }

   private Map<String, Integer> getWeights(Setup setup) {

      String propertiesFilename = BaseUtils.getString(setup.get(LoopOptimizationOptions.PropertiesFileWithInstructionCycles));
      if (propertiesFilename.isEmpty()) {
         return null;
      }

      File propertiesFile = new File(propertiesFilename);
      if (!propertiesFile.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not load properties file '" + propertiesFilename + "'.");
         return null;
      }

      Properties properties = IoUtils.loadProperties(propertiesFile);


      if (properties == null) {
         return null;
      }

      Map<String, Integer> weights = new HashMap<String, Integer>();
      for (String key : properties.stringPropertyNames()) {
         String stringValue = properties.getProperty(key);
         Integer value = ParseUtils.parseInteger(stringValue);
         if (value == null) {
            continue;
         }
         weights.put(key, value);
      }

      return weights;
   }

   private void optimizeVbis(List<VeryBigInstruction32> vbis) {
      Solver solver = new MbSolver();
      VbiOptimizer constantPropagation = new ConstantFoldingAndPropagation(solver);
      //VbiOptimizer constantPropagation2 = new ConstantFoldingAndPropagation(solver);
      ConstantLoadsRemoval loadRemove = new ConstantLoadsRemoval();

      for(VeryBigInstruction32 vbi : vbis) {
         constantPropagation.optimize(vbi);
//         constantPropagation2.optimize(vbi);
         loadRemove.optimize(vbi);
      }

      loadRemove.close();
   }

   /**
    * INSTANCE VARIABLES
    */
   private Map<String, Integer> nodeWeights;
   private File outputFolder;


}
