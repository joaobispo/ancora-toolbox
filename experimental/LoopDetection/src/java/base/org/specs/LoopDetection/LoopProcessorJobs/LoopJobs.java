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

package org.specs.LoopDetection.LoopProcessorJobs;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;
import org.specs.LoopDetection.LoopProcessorInfo;
import org.specs.LoopDetection.Worker.LoopProcessorJob;

/**
 *
 * @author Joao Bispo
 */
public class LoopJobs {

   public LoopJobs(DottyWriter dottyWriter, LoopWriter loopWriter) {
      this.dottyWriter = dottyWriter;
      this.loopWriter = loopWriter;
   }

   public static LoopJobs newLoopProcessors(DiskWriterSetup diskWriterSetup,
           LoopProcessorInfo jobInfo) {

      // Disk Writer
      LoopWriter loopWriter = LoopWriter.newLoopWriter(diskWriterSetup, jobInfo);
      if(loopWriter == null) {
         LoggingUtils.getLogger().
                 warning("Could not create LoopWriter.");
         return null;
      }

      DottyWriter dottyWriter = new DottyWriter();

      return new LoopJobs(dottyWriter, loopWriter);
   }

   public List<LoopProcessorJob> asList() {
      List<LoopProcessorJob> list = new ArrayList<LoopProcessorJob>();

      list.add(loopWriter);
      list.add(dottyWriter);
      
      return list;
   }



   public final DottyWriter dottyWriter;
   public final LoopWriter loopWriter;
}
