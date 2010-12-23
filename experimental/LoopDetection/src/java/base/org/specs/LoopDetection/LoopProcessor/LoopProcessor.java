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

package org.specs.LoopDetection.LoopProcessor;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.Interfaces.TraceReader;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.LoopDetection.LoopProcessorInfo;

/**
 *
 * @author Joao Bispo
 */
public class LoopProcessor {

   public LoopProcessor(LoopProcessorInfo jobInfo, LoopDetector loopDetector,
           SystemSetup systemSetup) {
      this.jobInfo = jobInfo;
      this.loopDetector = loopDetector;
      this.systemSetup = systemSetup;

      this.loopProcessors = new ArrayList<LoopProcessorJob>();
   }


   public static LoopProcessor newLoopWorker(LoopProcessorInfo jobInfo,
           SystemSetup systemSetup) {
      LoopDetector loopDetector = LoopUtils.newLoopDetector(jobInfo.detectorName,
              jobInfo.detectorSetup, jobInfo.processor.getInstructionDecoder());

      if (loopDetector == null) {
         LoggingUtils.getLogger().
                 warning("Could not create LoopDetector");
         return null;
      }

      return new LoopProcessor(jobInfo, loopDetector, systemSetup);
   }

   public LoopProcessorResults run() throws InterruptedException {

      TraceReader traceReader = DToolReader.newDToolReader(jobInfo.elfFile, systemSetup);

      // Stats
      int loopInstCount = 0;
      int traceCount = 0;

      String instruction = null;
      while ((instruction = traceReader.nextInstruction()) != null) {
         traceCount++;
         int address = traceReader.getAddress();
         loopDetector.step(address, instruction);

         List<LoopUnit> loops = loopDetector.getAndClearUnits();
         loopInstCount += processLoops(loops);

         // Check if work should be interrupted
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Application Interrupted");
         }
      }

      loopDetector.close();
      List<LoopUnit> loops = loopDetector.getAndClearUnits();
      loopInstCount += processLoops(loops);

      // Test #instructions
      testInstructionNumber(traceCount, loopInstCount);

      return new LoopProcessorResults(traceCount);
   }

   private void testInstructionNumber(int traceCount, int loopCount) {
      if (traceCount != loopCount) {
         LoggingUtils.getLogger().
                 warning("Trace instruction count (" + traceCount + ") different "
                 + "from loop instruction count (" + loopCount + ").");
      }
   }


   private int processLoops(List<LoopUnit> loops) {
      if(loops == null) {
         return 0;
      }

      int loopInstCount = 0;
      for(LoopUnit unit : loops) {
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
   private void processLoop(LoopUnit unit) {
      for(LoopProcessorJob loopProcessor : loopProcessors) {
         loopProcessor.processLoop(unit);
      }
   }

   public List<LoopProcessorJob> getLoopProcessors() {
      return loopProcessors;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private final LoopProcessorInfo jobInfo;
   private final LoopDetector loopDetector;
   private final SystemSetup systemSetup;

   private final List<LoopProcessorJob> loopProcessors;



}
