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

package org.specs.AutoCompile.Job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.specs.AutoCompile.Options.JobOption;
import org.specs.AutoCompile.Targets;

/**
 * Utility methods related to job options.
 *
 * @author Joao Bispo
 */
public class JobUtils {

   public static List<Job> buildJobs(Map<String,AppOption> jobOptions,
           Map<String,AppOption> targetOptions) {
   //public static List<Job> buildJobs(Map<String,AppOption> jobOptions, Targets targets) {
      List<Job> jobs = new ArrayList<Job>();

      // Get Target for job
      //String target = AppUtils.getString(jobOptions, JobOption.target);
      //String compiler = AppUtils.getString(jobOptions, JobOption.compiler);
      //File targetConfig = targets.getTargetConfig(target, compiler);
      //Map<String,AppOption> targetOptions = AppUtils.parseFile(targetConfig);

      /*
       *    String outputFile;
   List<String> inputFiles;
   String optimization;
   List<String> otherFlags;
   String workingDir;
   String programExecutable;
   String outputFlag;
       */

      return jobs;
   }
   /*
    * NOT Sure if I should validate first, or while variables are used.
   public static boolean validateJob(Map<String,AppOption> jobOptions, Targets targets) {
      //JobOption.
      return false;
   }
    * 
    */
}
