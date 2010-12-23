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

import org.specs.DymaLib.LoopDetection.CodeSegment;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;
import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;
import org.specs.LoopDetection.LoopDetectionInfo;
import org.specs.LoopDetection.SegmentProcessor.SegmentProcessorJob;
import org.specs.LoopDetection.Utils;

/**
 *
 * @author Joao Bispo
 */
public class LoopWriter implements SegmentProcessorJob {

   public LoopWriter(LoopDiskWriter loopWriter) {
      this.loopWriter = loopWriter;
   }

   public static LoopWriter newLoopWriter(DiskWriterSetup diskWriterSetup,
           LoopDetectionInfo jobInfo) {

      LoopDiskWriter loopDiskWriter = Utils.newLoopDiskWriter(diskWriterSetup, jobInfo);

      return new LoopWriter(loopDiskWriter);
   }

         // Create loopWriter
      //LoopDiskWriter loopWriter = Utils.newLoopDiskWriter(diskWriterSetup, jobInfo);

   public void processSegment(CodeSegment loopUnit) {
      loopWriter.addLoop(loopUnit);
   }

   private final LoopDiskWriter loopWriter;
}
