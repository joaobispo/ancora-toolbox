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
import java.util.List;
import java.util.Map;
import java.util.logging.LoggingMXBean;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Job.Job;
import org.specs.AutoCompile.Job.JobUtils;
import org.specs.AutoCompile.Options.Config;
import org.specs.AutoCompile.Options.JobOption;

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


      // Get Job options
      String jobFilepath = AppUtils.getString(options, Config.jobFile);
      Map<String,AppOption> jobOptions = AppUtils.parseFile(new File(jobFilepath));


      // Get Target Options
      String target = AppUtils.getString(jobOptions, JobOption.target);
      String compiler = AppUtils.getString(jobOptions, JobOption.compiler);
      File targetConfig = targets.getTargetConfig(target, compiler);
      Map<String,AppOption> targetOptions = AppUtils.parseFile(targetConfig);


      // Get jobs
      List<Job> jobs = JobUtils.buildJobs(jobOptions, targetOptions);
      //List<Job> jobs = JobUtils.buildJobs(jobOptions, targets);
      if(jobs == null) {
         LoggingUtils.getLogger().
                 warning("Could not build jobs.");
         return -1;
      }

      // Run each Job
      for(Job job : jobs) {
         int returnValue = job.run();
         if(returnValue != 0) {
            LoggingUtils.getLogger().
                    warning("Problems while running job: returned value '"+returnValue+"'.\n"
                    + "Job:"+job.toString());
         }
      }

      /*
      if(!JobUtils.validateJob(jobOptions, targets)) {
         LoggingUtils.getLogger().
                 warning("Problem while validating job.");
      }
       * 
       */

      //System.out.println(options);
      //System.out.println(targets.getTargets());
      //System.out.println(targets.getTargetFiles());
      //System.out.println(jobOptions);
      return 0;
   }

}
