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

import org.specs.AutoCompile.Job.JobProgress;
import org.specs.AutoCompile.Targets.Targets;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Job.Job;
import org.specs.AutoCompile.Job.JobUtils;
import org.specs.AutoCompile.Job.JobOption;
import org.specs.AutoCompile.Targets.TargetOption;

/**
 * Automates compilation of benchmarks.
 *
 * @author Joao Bispo
 */
public class AutoCompile implements App {

//   public AutoCompile(String targetFolder) {
   private AutoCompile(Targets targets) {
      //targets = Targets.buildTargets(targetFolder);
      this.targets = targets;
   }

   public static AutoCompile newAutoCompile(String targetFolder) {
      Targets targets = Targets.buildTargets(targetFolder);
      if(targets == null) {
         LoggingUtils.getLogger().
                 warning("Could not create application object.");
         return null;
      }

      return new AutoCompile(targets);
   }



   public Class getAppOptionEnum() {
      return JobOption.class;
   }

   /**
    *
    * @param options
    * @return
    */
   public int execute(Map<String, AppValue> jobOptions) {

      // Get Job options
      /*
      String jobFilepath = AppUtils.getString(options, Config.jobFile);
      File jobFile = new File(jobFilepath);
      Map<String,AppOption> jobOptions = AppUtils.parseFile(jobFile);
*/

      // Get Target Options
      String target = AppUtils.getString(jobOptions, JobOption.target);
      String compiler = AppUtils.getString(jobOptions, JobOption.compiler);
      File targetConfig = targets.getTargetConfig(target, compiler);
      if(targetConfig == null) {
         LoggingUtils.getLogger().
                 warning("Could not get configuration for '"+Targets.getTargetName(target, compiler)+"'");
         return -1;
      }

      Class targetClass = TargetOption.class;
      Map<String,AppValue> targetOptions = AppOptionFile.parseFile(targetConfig, targetClass).getMap();
      //Map<String,AppValue> targetOptions = AppUtils.parseFile(targetConfig);

      // Get jobs
      List<Job> jobs = JobUtils.buildJobs(jobOptions, targetOptions);
      if(jobs == null) {
         LoggingUtils.getLogger().
                 warning("Could not build jobs.");
         return -1;
      }

      //JobProgress jobProgress = new JobProgress(jobFile.getName(), jobs.size());
      JobProgress jobProgress = new JobProgress(jobs.size());
      jobProgress.initialMessage();

      for(Job job : jobs) {
         jobProgress.nextMessage();
         int returnValue = job.run();
         if(returnValue != 0) {
            LoggingUtils.getLogger().
                    warning("Problems while running job: returned value '"+returnValue+"'.\n"
                    + "Job:"+job.toString());
         }
      }

      //System.out.println(options);
      //System.out.println(targets.getTargets());
      //System.out.println(targets.getTargetFiles());
      //System.out.println(jobOptions);
      return 0;
   }

   /**
    * INSTANCE VARIABLES
    */
   private Targets targets;

}
