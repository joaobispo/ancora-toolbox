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
package org.specs.DymaLib.Simulator;

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.PreAnalysis.CodeSegment;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.TraceReader;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Utility methods related to simulation of files and loops.
 *
 * @author Joao Bispo
 */
public class SimpleSimulator {

   public SimpleSimulator() {
      this.traceReader = null;
      this.loopDetector = null;
      this.iterationThreshold = null;
   }

   private void init(TraceReader traceReader, LoopDetector loopDetector, Integer iterationThreshold) {
      this.traceReader = traceReader;
      this.loopDetector = loopDetector;
      this.iterationThreshold = iterationThreshold;

      loopInstCount = 0;
      traceCount = 0;
      foundLoops = new ArrayList<CodeSegment>();
   }

   public SimulatorResults run(TraceReader traceReader, LoopDetector loopDetector,
           int iterationThreshold) {

      // Initialize simulator
      init(traceReader, loopDetector, iterationThreshold);

      return run();

     
   }

      private SimulatorResults run() {
       String instruction = null;
      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);
         process();
      }
      loopDetector.close();
      process();


      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);

      return new SimulatorResults(traceCount, traceReader.getCycles(), foundLoops);
   }

   private void process() {
      List<CodeSegment> loops = getLoops(loopDetector, traceReader, iterationThreshold);
      if(loops == null) {
         return;
      }
      
      foundLoops.addAll(loops);
   }

   private static int getLoopsInstructions(List<CodeSegment> loops) {
      if (loops == null) {
         return 0;
      }

      int loopInstCount = 0;
      for (CodeSegment unit : loops) {
         // Stats
         loopInstCount += unit.getTotalInstructions();
      }
      return loopInstCount;
   }

   private List<CodeSegment> getLoops(LoopDetector loopDetector,
           TraceReader traceReader, int iterationThreshold) {

      List<CodeSegment> loops = loopDetector.getAndClearUnits();
      if (loops == null) {
         return null;
      }

      if (loops.isEmpty()) {
         LoggingUtils.getLogger().warning("Loop detector returned empty list?");
         return null;
      }

      if (loops.size() > 1) {
         LoggingUtils.getLogger().
                 warning("Loop Detector returned more than one loop. Cannot add "
                 + "the values of the registers reliably in this case.");
      }

      loopInstCount += getLoopsInstructions(loops);

      // Remove from the list loops which do not meet the iteration threshold
      List<CodeSegment> approvedLoops = new ArrayList<CodeSegment>();
      for (CodeSegment loop : loops) {
         if (loop.getIterations() < iterationThreshold) {
            continue;
         }

         approvedLoops.add(loop);
      }
//System.out.println("Approved Loops:");
//      System.out.println(approvedLoops);
      if(approvedLoops.isEmpty()) {
         return null;
      }
      
      addRegisterValuesToLoop(approvedLoops, traceReader);

      //return loops; //BUG
      return approvedLoops;
   }

   private static void addRegisterValuesToLoop(List<CodeSegment> loops, TraceReader traceReader) {      
      // Get register values
      //Map<RegisterId,Integer> registerValues = traceReader.getRegisters();
      RegisterTable registerValues = traceReader.getRegisters();
      if (registerValues == null) {
         LoggingUtils.getLogger().
                 warning("Implementation of TraceReader '" + traceReader.getClass()
                 + "' does not support reading the values of register.");
         return;
      }

      if(loops.size() > 1) {
         LoggingUtils.getLogger().
                 warning("Adding values to constant registers in more than one loop. The values will not be reliable.");
      }

      for(CodeSegment codeSegment : loops) {
        codeSegment.setRegisterValues(registerValues);
      }
      //loops.get(0).setRegisterValues(registerValues);

   }

   private static void testInstructionNumber(int traceCount, int loopCount) {
      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }
   }
   
   private TraceReader traceReader;
   private LoopDetector loopDetector;
   private Integer iterationThreshold;
   // Stats
   private int loopInstCount;
   private int traceCount;
   private List<CodeSegment> foundLoops;


}
