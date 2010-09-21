/*
 *  Copyright 2010 Ancora Research Group.
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

package org.specs.AutoCompile;

import java.io.File;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.specs.AutoCompile.Options.Config;

/**
 * Automates compilation of benchmarks.
 *
 * @author Joao Bispo
 */
public class AutoCompile implements App {

   public AutoCompile() {
   }


   /**
    *
    * @param options
    * @return
    */
   public int execute(Map<String, AppOption> options) {
      // Build target tables
      String targetFolder = AppUtils.getString(options, Config.targetFolder);
      Targets targets = Targets.buildTargets(targetFolder);

      // Perform job
      String jobFilepath = AppUtils.getString(options, Config.jobFile);
      Map<String,AppOption> jobOptions = AppUtils.parseFile(new File(jobFilepath));



      /*
      if(!JobUtils.validateJob(jobOptions, targets)) {
         LoggingUtils.getLogger().
                 warning("Problem while validating job.");
      }
       * 
       */

      System.out.println(options);
      System.out.println(targets.getTargets());
      System.out.println(targets.getTargetFiles());
      System.out.println(jobOptions);
      return 0;
   }

}
