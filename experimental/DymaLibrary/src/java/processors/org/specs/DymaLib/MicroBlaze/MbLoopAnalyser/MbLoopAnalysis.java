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

package org.specs.DymaLib.MicroBlaze.MbLoopAnalyser;

import org.specs.DymaLib.PreAnalysis.AssemblyAnalysis;
import org.specs.DymaLib.Liveness.LivenessAnalysis;

/**
 * Data extracted from MegaBlock analysis.
 *
 * @author Joao Bispo
 */
public class MbLoopAnalysis {



   public MbLoopAnalysis(long totalCyclesWithOptimizations, long totalCyclesWithoutOptimizations) {
      this.totalCyclesWithOptimizations = totalCyclesWithOptimizations;
      this.totalCyclesWithoutOptimizations = totalCyclesWithoutOptimizations;
   }

   public static MbLoopAnalysis buildAnalysis(int originalCpl, int transformedCpl, int loopIterations,
           int communicationCycles) {

      long totalCyclesWithOptimizations = calcLoopCycles(transformedCpl, loopIterations, communicationCycles);
      long totalCyclesWithoutOptimizations = calcLoopCycles(originalCpl, loopIterations, communicationCycles);

      return new MbLoopAnalysis(totalCyclesWithOptimizations, totalCyclesWithoutOptimizations);
   }


   public static long calcLoopCycles(int transformedCpl, int loopIterations, int communicationCycles) {
      return transformedCpl * loopIterations + communicationCycles;
   }
/*
   public static MbLoopAnalysis analyse(CodeSegment loop, Map<String, Integer> nodeWeights) {
      // Build MicroBlaze instructions cache
      List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
              loop.getAddresses(), loop.getInstructions());

       // Gather complete pass analysis data
      AssemblyAnalysis asmData = MbAssemblyUtils.buildAssemblyAnalysis(loop.getRegisterValues(), mbInstructions);

      // Expand instructions into very big instructions
      MbVbiParser vbiParser = new MbVbiParser(asmData);
      List<VeryBigInstruction32> vbis = VbiUtils.getVbis(mbInstructions, vbiParser);

      GraphBuilder graphBuilder = new MbGraphBuilder(nodeWeights);
      GraphNode rootNode = graphBuilder.buildGraph(vbis);
      
      
      VbiAnalysis vbiAnalysisOriginal = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);

      optimizeVbis(vbis);
      graphBuilder = new MbGraphBuilder(nodeWeights);
      rootNode = graphBuilder.buildGraph(vbis);

      //VbiAnalysis vbiAnalysisTransformed = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisTransformed = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      System.out.println(vbiAnalysisTransformed.diff(vbiAnalysisOriginal));

      int communicationCycles = calcCommunicationCycles(asmData);

      int nonOptCycles = (vbiAnalysisOriginal.criticalPathLenght*loop.getIterations()) + communicationCycles;
      int optCycles = (vbiAnalysisTransformed.criticalPathLenght*loop.getIterations()) + communicationCycles;
      
      return new MbLoopAnalysis(optCycles, nonOptCycles);
   }
*/
   /**
    * Communication is calculated by giving one cycle to each non-constant
    * live-in and another cycle for each live-out.
    * 
    * @param asmData
    * @return
    */
   public static int calcCommunicationCycles(AssemblyAnalysis asmData) {
      LivenessAnalysis liveness = asmData.livenessAnalysis;
      // # Non-constant live-ins
      //int liveIns = asmData.liveIns.size() - asmData.constantRegisters.size();
      int liveIns = liveness.liveIns.size() - liveness.constantRegisters.size();
      int liveInsCycles = liveIns * DEFAULT_COMM_CYCLES_PER_REG;

      //int liveOuts = asmData.liveOuts.size();
      int liveOuts = liveness.liveOuts.size();
      int liveOutsCycles = liveOuts * DEFAULT_COMM_CYCLES_PER_REG;

      return liveInsCycles + liveOutsCycles;
   }
/*
      private static void optimizeVbis(List<VeryBigInstruction32> vbis) {
      Solver solver = new MbSolver();
      VbiOptimizer constantPropagation = new ConstantFoldingAndPropagation(solver);
      //VbiOptimizer constantPropagation2 = new ConstantFoldingAndPropagation(solver);
      //ConstantLoadsRemoval loadRemove = new ConstantLoadsRemoval();

      for(VeryBigInstruction32 vbi : vbis) {
         constantPropagation.optimize(vbi);
//         constantPropagation2.optimize(vbi);
         //loadRemove.optimize(vbi);
      }

      //loadRemove.close();
   }
*/

   public final long totalCyclesWithOptimizations;
   public final long totalCyclesWithoutOptimizations;

   public static final Integer DEFAULT_COMM_CYCLES_PER_REG = 1;

   //private Setup mbSolverSetup;
}
