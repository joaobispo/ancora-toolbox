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

import java.util.logging.Logger;

/**
 * Shows information about progress of jobs.
 *
 * @author Joao Bispo
 */
public class JobProgress {

   //public JobProgress(String jobFilename, int numJobs) {
   public JobProgress(int numJobs) {
      //this.jobFilename = jobFilename;
      this.numJobs = numJobs;
      counter = 0;
   }

   public void initialMessage() {
            //logger.info("Found "+numJobs+" jobs for JobFile '"+jobFilename+"'.");
            logger.info("Found "+numJobs+" jobs.");
   }


   public void nextMessage() {
      if(counter >= numJobs) {
         logger.warning("Already showed the total number of steps.");
      }

      counter++;
      //logger.info("("+jobFilename+") Job "+counter+" of "+numJobs+".");
      logger.info("Job "+counter+" of "+numJobs+".");
   }

   /**
    * INSTANCE VARIABLES
    */
   //private String jobFilename;
   private int numJobs;
   private int counter;
   private final static Logger logger = Logger.getLogger(JobProgress.class.getName());



}
