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

package org.specs.DymaLib.Utils.SegmentProcessor;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Applies a LoopDetector to a TraceReader and processes the code according to
 * the registered SegmentProcessorJobs.
 *
 * @author Joao Bispo
 */
public class SegmentProcessor {

   public SegmentProcessor() {
      this.loopProcessors = new ArrayList<SegmentProcessorJob>();
   }

   public int run(TraceReader traceReader, LoopDetector loopDetector) throws InterruptedException {

      // Stats
      int loopInstCount = 0;
      int traceCount = 0;

      String instruction = null;
      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<CodeSegment> loops = getLoops(loopDetector, traceReader);
  
         loopInstCount += processLoops(loops);

         // Check if work should be interrupted
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Application Interrupted");
         }
      }

      loopDetector.close();
      List<CodeSegment> loops = getLoops(loopDetector, traceReader);

      loopInstCount += processLoops(loops);

      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);

      //return new LoopProcessorResults(traceCount);
      return traceCount;
   }

   private void testInstructionNumber(int traceCount, int loopCount) {
      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }
   }


   private int processLoops(List<CodeSegment> loops) {
      if(loops == null) {
         return 0;
      }

      int loopInstCount = 0;
      for(CodeSegment unit : loops) {
         processLoop(unit);
         // Stats
         loopInstCount += unit.getTotalInstructions();
      }
      return loopInstCount;
   }

   /**
    * Feed the loop to all registeres loop processors.
    * @param unit
    */
   private void processLoop(CodeSegment unit) {
      for(SegmentProcessorJob loopProcessor : loopProcessors) {
         loopProcessor.processSegment(unit);
      }
   }

   public List<SegmentProcessorJob> getLoopProcessors() {
      return loopProcessors;
   }

      private void addRegisterValuesToLoop(List<CodeSegment> loops, TraceReader traceReader) {
      if (loops == null) {
         return;
      }

      if(loops.isEmpty()) {
         LoggingUtils.getLogger().warning("Loop detector returned empty list?");
         return;
      }

      if (loops.size() > 1) {
         LoggingUtils.getLogger().
                 warning("Loop Detector returned more than one loop. Cannot add "
                 + "the values of the registers reliably in this case.");
      }

      // Get register values
       //Map<RegisterId,Integer> registerValues = traceReader.getRegisters();
       RegisterTable registerValues = traceReader.getRegisters();
       if(registerValues == null) {
          LoggingUtils.getLogger().
                  warning("Implementation of TraceReader '"+traceReader.getClass()+
                  "' does not support reading the values of register.");
          return;
       }

       // There should be only one element in the list at this point,
       // add the values of the registers.
       //System.out.println("Register Values Prime:");
       //System.out.println(registerValues);
       //System.out.println("Loop Before:"+loops.get(0).getRegisterValues());
       loops.get(0).setRegisterValues(registerValues);
       //System.out.println("Loop After:"+loops.get(0).getRegisterValues());

   }

   private List<CodeSegment> getLoops(LoopDetector loopDetector, TraceReader traceReader) {
      List<CodeSegment> loops = loopDetector.getAndClearUnits();
      addRegisterValuesToLoop(loops, traceReader);

      return loops;
   }


   /**
    * INSTANCE VARIABLES
    */
   private final List<SegmentProcessorJob> loopProcessors;

}
