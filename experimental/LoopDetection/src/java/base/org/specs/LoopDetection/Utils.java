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

package org.specs.LoopDetection;

import java.io.File;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterSetup;
import org.specs.DymaLib.Utils.LoopDiskWriter.LoopDiskWriter;

/**
 *
 * @author Joao Bispo
 */
public class Utils {

   /**
    * Builds a LoopDisksWriter.
    *
    * <p>MegaBlocks are currently the only type of loops it considers as straight
    * line loops.
    *
    * @param elfFile
    * @param diskWriterSetup
    * @param detectorName
    * @param detectorSetup
    * @param jobInfo
    * @return
    */
   public static LoopDiskWriter newLoopDiskWriter(DiskWriterSetup diskWriterSetup,
           LoopDetectionInfo jobInfo) {

      //boolean isStraighLineLoop = jobInfo.detectorRunName.equals(LoopDetectors.MegaBlock.name());
      boolean isStraighLineLoop = jobInfo.getDetectorName().equals(LoopDetectors.MegaBlock.name());
      Enum[] instructionNames = jobInfo.processor.getInstructionNames();
      String baseFilename = ParseUtils.removeSuffix(jobInfo.elfFile.getName(), ".");

      LoopDiskWriter loopWriter = new LoopDiskWriter(jobInfo.outputFolder, baseFilename,
    //          jobInfo.detectorSetup.getName(), jobInfo.processor.getLowLevelParser(), diskWriterSetup, isStraighLineLoop,
              jobInfo.detectorRunName, jobInfo.processor.getLowLevelParser(), diskWriterSetup, isStraighLineLoop,
              instructionNames);

      return loopWriter;
   }
}
