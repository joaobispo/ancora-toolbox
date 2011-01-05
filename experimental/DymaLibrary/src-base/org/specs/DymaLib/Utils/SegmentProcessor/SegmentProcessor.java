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
import java.util.Map;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.DataStructures.CodeSegment;
import org.suikasoft.SharedLibrary.Processors.RegisterId;

/**
 * Applies a LoopDetector to a TraceReader and processes the code according to
 * the registered SegmentProcessorJobs.
 *
 * @author Joao Bispo
 */
public class SegmentProcessor {

   public SegmentProcessor() {
//   public LoopProcessor(LoopDetectionInfo jobInfo) {
//   public LoopProcessor(LoopProcessorInfo jobInfo, LoopDetector loopDetector,
//           SystemSetup systemSetup) {
      //this.jobInfo = jobInfo;
      //this.loopDetector = loopDetector;
      //this.systemSetup = systemSetup;

      this.loopProcessors = new ArrayList<SegmentProcessorJob>();
   }


   /*
   public static LoopProcessor newLoopWorker(LoopProcessorInfo jobInfo) {
//   public static LoopProcessor newLoopWorker(LoopProcessorInfo jobInfo,
//           SystemSetup systemSetup) {
      LoopDetector loopDetector = LoopUtils.newLoopDetector(jobInfo.detectorName,
              jobInfo.detectorSetup, jobInfo.processor.getInstructionDecoder());

      if (loopDetector == null) {
         LoggingUtils.getLogger().
                 warning("Could not create LoopDetector");
         return null;
      }

      return new LoopProcessor(jobInfo, loopDetector, systemSetup);
   }
    * 
    */

//   public LoopProcessorResults run() throws InterruptedException {
//   public LoopProcessorResults run(TraceReader traceReader, LoopDetector loopDetector) throws InterruptedException {
   //public int run(TraceReader traceReader, LoopDetector loopDetector) throws InterruptedException {
   public int run(TraceReader traceReader, LoopDetector loopDetector) throws InterruptedException {

      //TraceReader traceReader = DToolReader.newDToolReader(jobInfo.elfFile, systemSetup);

      // Stats
      int loopInstCount = 0;
      int traceCount = 0;

      String instruction = null;
      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<CodeSegment> loops = loopDetector.getAndClearUnits();
         loopInstCount += processLoops(loops);

         addRegisterValuesToLoop(loops, traceReader);

         // Check if work should be interrupted
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Application Interrupted");
         }
      }

      loopDetector.close();
      List<CodeSegment> loops = loopDetector.getAndClearUnits();
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

   

   /**
    * INSTANCE VARIABLES
    */
   //private final LoopDetectionInfo jobInfo;
   //private final LoopDetector loopDetector;
   //private final SystemSetup systemSetup;

   private final List<SegmentProcessorJob> loopProcessors;

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
       Map<RegisterId,Integer> registerValues = traceReader.getRegisters();
       if(registerValues == null) {
          LoggingUtils.getLogger().
                  warning("Implementation of TraceReader '"+traceReader.getClass()+
                  "' does not support reading the values of register.");
          return;
       }

       
   }



}
