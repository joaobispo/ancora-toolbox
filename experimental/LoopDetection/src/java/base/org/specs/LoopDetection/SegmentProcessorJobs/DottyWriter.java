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

import org.specs.DymaLib.Dotty.DottyLoopUnit;
import org.specs.DymaLib.LoopDetection.CodeSegment;
import org.specs.DymaLib.Utils.SegmentProcessor.SegmentProcessorJob;

/**
 *
 * @author Joao Bispo
 */
public class DottyWriter implements SegmentProcessorJob {

   public DottyWriter() {
      dotty = new DottyLoopUnit();
   }



   public void processSegment(CodeSegment loopUnit) {
      dotty.addUnit(loopUnit);
   }

   public DottyLoopUnit getDotty() {
      return dotty;
   }

   

   private final DottyLoopUnit dotty;
}