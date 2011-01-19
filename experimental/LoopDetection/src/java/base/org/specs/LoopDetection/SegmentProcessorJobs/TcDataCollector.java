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

package org.specs.LoopDetection.SegmentProcessorJobs;

import org.specs.CoverageData.TcData;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.Utils.SegmentProcessor.SegmentProcessorJob;

/**
 *
 * @author Joao Bispo
 */
public class TcDataCollector implements SegmentProcessorJob {

   public TcDataCollector() {
      tcData = new TcData();
      maxRep = 0;
      maxBlockSize = 0;
   }



   public void processSegment(CodeSegment segment) {
      tcData.addBlock(segment);

      maxRep = Math.max(maxRep, segment.getIterations());
      maxBlockSize = Math.max(maxBlockSize, segment.getTotalInstructions());
   }

   public int getMaxRep() {
      return maxRep;
   }

   public long getMaxBlockSize() {
      return maxBlockSize;
   }



   public final TcData tcData;
   private int maxRep;
   private long maxBlockSize;

}
