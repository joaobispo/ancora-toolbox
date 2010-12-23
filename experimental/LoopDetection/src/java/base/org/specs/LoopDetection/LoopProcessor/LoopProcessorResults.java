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

import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;
import org.specs.LoopDetection.LoopProcessorJobs.DottyWriter;
import org.specs.LoopDetection.LoopProcessorJobs.LoopWriter;

/**
 *
 * @author Joao Bispo
 */
public class LoopProcessorResults {

   public LoopProcessorResults(int executedInstructions) {
      this.executedInstructions = executedInstructions;
   }

   
   /*
   public LoopDetectorWorkerResults(DottyWriter dottyWriter, LoopWriter loopWriter,
           long executedInstructions) {
      this.dottyWriter = dottyWriter;
      this.loopWriter = loopWriter;
      this.executedInstructions = executedInstructions;
   }
*/
   /*
   public LoopDetectorWorkerResults(LoopDiskWriter loopDiskWriter) {
      dottyWriter = new DottyWriter();
      loopWriter = new LoopWriter(loopDiskWriter);
      executedInstructions = 0l;
   }
    *
    */



   //public final DottyWriter dottyWriter;
   //public final LoopWriter loopWriter;
   public final int executedInstructions;
}
