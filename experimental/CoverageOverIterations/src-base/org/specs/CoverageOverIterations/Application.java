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

package org.specs.CoverageOverIterations;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Utilities.ProgressCounter;
import org.specs.DymaLib.Partitioning.SupportedPartitioners;

/**
 * Implements the CoverageOverIterations application
 *
 * @author Joao Bispo
 */
public class Application implements App {

   public int execute(Map<String, AppValue> options) {
      // Get input programs
      List<File> inputPrograms = Utils.getInputPrograms(options);
      if(inputPrograms == null) {
         LoggingUtils.getLogger().
                 warning("Could not get programs.");
         return -1;
      }

      ProgressCounter<File> programsCounter = new ProgressCounter<File>(inputPrograms);

      File file;
      while((file=programsCounter.nextElement()) != null) {
         programsCounter.message("File "+ProgressCounter.PROGRESS);
         System.out.println(file);
      }

      // Get partitioners
      List<SupportedPartitioners> partitioners = Utils.getPartitioners(options);

      ProgressCounter<SupportedPartitioners> partsCounter = new ProgressCounter<SupportedPartitioners>(partitioners);

      SupportedPartitioners part;
      while((part=partsCounter.nextElement()) != null) {
         partsCounter.message("Partitioner "+ProgressCounter.PROGRESS+": "+part);
      }
      System.out.println("List size:"+partitioners.size());
      return 0;
   }

   public Class getAppOptionEnum() {
      return Options.class;
   }

}
