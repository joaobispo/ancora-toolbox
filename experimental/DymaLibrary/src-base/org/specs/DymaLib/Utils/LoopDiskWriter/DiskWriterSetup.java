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

package org.specs.DymaLib.Utils.LoopDiskWriter;

import java.io.File;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Container class for setup variables of LoopDiskWriter.
 *
 * @author Joao Bispo
 */
public class DiskWriterSetup {

   public DiskWriterSetup(int iterationsThreshold, boolean writeDottyForStraighLineLoops,
           boolean writeTxtFilesForLoops) {
      this.iterationsThreshold = iterationsThreshold;
      this.writeDottyForStraighLineLoops = writeDottyForStraighLineLoops;
      this.writeTxtFilesForLoops = writeTxtFilesForLoops;
   }



    /**
    * Builds a SystemConfiguration from a file built with AppOptionFile.
    *
    * @return
    */
   public static DiskWriterSetup newSetup(File setupFile) {
      if (!setupFile.exists()) {
         LoggingUtils.getLogger().
                 warning("File '" + setupFile.getPath() + "' does not exist.");
         return null;
      }

      AppOptionFile optionFile = AppOptionFile.parseFile(setupFile, DiskWriterOptions.class);
      if (optionFile == null) {
         LoggingUtils.getLogger().
                 warning("AppOptionFile object is null.");
         return null;
      }

      return newSetup(optionFile.getMap());
   }

   public static DiskWriterSetup newSetup(Map<String, AppValue> options) {
      Integer iterationsThreshold = AppUtils.getInteger(options, DiskWriterOptions.IterationsThreshold);
      if (iterationsThreshold == null) {
         iterationsThreshold = 0;
      }
      boolean writeDottyForStraighLineLoops = AppUtils.getBool(options, DiskWriterOptions.WriteDotForStraighLineLoops);
      boolean writeTxtFilesForLoops = AppUtils.getBool(options, DiskWriterOptions.WriteBlockFilesForLoops);

      return new DiskWriterSetup(iterationsThreshold, writeDottyForStraighLineLoops, writeTxtFilesForLoops);
   }

   public final int iterationsThreshold;
   public final boolean writeDottyForStraighLineLoops;
   public final boolean writeTxtFilesForLoops;
}
