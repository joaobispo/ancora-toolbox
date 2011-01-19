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

package org.specs.LoopOptimization.V2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.Assembly.AssemblyAnalysis;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.MicroBlaze.Assembly.MbAssemblyUtils;
import org.specs.DymaLib.MicroBlaze.MbWeightsSetup;
import org.specs.DymaLib.MicroBlaze.Vbi.MbGraphBuilder;
import org.specs.DymaLib.MicroBlaze.Vbi.MbSolver;
import org.specs.DymaLib.MicroBlaze.Vbi.MbVbiParser;
import org.specs.DymaLib.Vbi.Analysis.VbiAnalysis;
import org.specs.DymaLib.Vbi.Optimization.OptimizersList;
import org.specs.DymaLib.Vbi.Optimization.VbiOptimizer;
import org.specs.DymaLib.Vbi.Utils.GraphBuilder;
import org.specs.DymaLib.Vbi.VbiUtils;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.specs.DymaLib.Weights.WeightTable;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.EnumUtils;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MicroBlazeParser;

/**
 *
 * @author Joao Bispo
 */
public class MbLoopAnalyser {

   public MbLoopAnalyser(Setup mbLoopAnalysisSetup) {
      Setup weightSetup = BaseUtils.getSetup(mbLoopAnalysisSetup.get(MbLoopAnalysisSetup.MbWeights));
      weights = MbWeightsSetup.buildTable(weightSetup);

      Map<String, Setup> optimizations = BaseUtils.getMapOfSetups(mbLoopAnalysisSetup.get(MbLoopAnalysisSetup.Optimizations));
      //optimizationName = new ArrayList<String>();
      //optimizationSetup = new ArrayList<Setup>();
      optimizers = new ArrayList<VbiOptimizer>();
      for(String key : optimizations.keySet()) {
         // Get setup
         Setup setup = optimizations.get(key);
         // Get optimization name
         String name = BaseUtils.decodeMapOfSetupsKey(key);
         // Get Optimizer
         OptimizersList optimizerEnum = EnumUtils.valueOf(OptimizersList.class, name);
         List<Object> arguments = buildArguments(optimizerEnum, setup);
         VbiOptimizer newOptimizer = optimizerEnum.getOptimizer(arguments);
         optimizers.add(newOptimizer);
      }
   }

   private List<Object> buildArguments(OptimizersList value, Setup setup) {
      List<Object> arguments = new ArrayList<Object>();

      if(value == OptimizersList.ConstantFoldingAndPropagation) {
         arguments.add(new MbSolver(setup));
         return arguments;
      }

      LoggingUtils.getLogger().
              warning("Case not defined:"+value);
      return arguments;
   }

   public MbLoopAnalysis analyse(CodeSegment loop) {
 // Build MicroBlaze instructions cache
      List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
              loop.getAddresses(), loop.getInstructions());

       // Gather complete pass analysis data
      AssemblyAnalysis asmData = MbAssemblyUtils.buildAssemblyAnalysis(loop.getRegisterValues(), mbInstructions);

      // Expand instructions into very big instructions
      MbVbiParser vbiParser = new MbVbiParser(asmData);
      List<VeryBigInstruction32> vbis = VbiUtils.getVbis(mbInstructions, vbiParser);

      //GraphBuilder graphBuilder = new MbGraphBuilder(nodeWeights);
      GraphBuilder graphBuilder = new MbGraphBuilder(weights);
      GraphNode rootNode = graphBuilder.buildGraph(vbis);


      VbiAnalysis vbiAnalysisOriginal = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);

      optimizeVbis(vbis);
      //graphBuilder = new MbGraphBuilder(nodeWeights);
      graphBuilder = new MbGraphBuilder(weights);
      rootNode = graphBuilder.buildGraph(vbis);

      //VbiAnalysis vbiAnalysisTransformed = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisTransformed = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      System.out.println(vbiAnalysisTransformed.diff(vbiAnalysisOriginal));

      int originalCpl = vbiAnalysisOriginal.criticalPathLenght;
      int transformedCpl = vbiAnalysisTransformed.criticalPathLenght;
      int loopIterations = loop.getIterations();

      //int communicationCycles = calcCommCycles(asmData);
      int communicationCycles = MbLoopAnalysis.calcCommunicationCycles(asmData);
      return MbLoopAnalysis.buildAnalysis(originalCpl, transformedCpl, loopIterations, communicationCycles);

      //int nonOptCycles = (vbiAnalysisOriginal.criticalPathLenght*loop.getIterations()) + communicationCycles;
      //int optCycles = (vbiAnalysisTransformed.criticalPathLenght*loop.getIterations()) + communicationCycles;

      //return new MbLoopAnalysis(optCycles, nonOptCycles);
   }

    private void optimizeVbis(List<VeryBigInstruction32> vbis) {
      for(VeryBigInstruction32 vbi : vbis) {
         optimizeVbi(vbi);
      }
   }

   private void optimizeVbi(VeryBigInstruction32 vbi) {
      for (VbiOptimizer optimizer : optimizers) {
         optimizer.optimize(vbi);
      }
   }

   //private List<String> optimizationName;
   //private List<Setup> optimizationSetup;
   private List<VbiOptimizer> optimizers;
   private WeightTable weights;


}
