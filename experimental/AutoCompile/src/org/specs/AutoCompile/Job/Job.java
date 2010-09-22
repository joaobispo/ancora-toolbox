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
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 *
 * @author Joao Bispo
 */
public class Job {

   public Job(String programExecutable, String outputFile, List<String> inputFiles,
           String optimization, List<String> otherFlags, String outputFlag, String workingDir) {
      this.outputFile = outputFile;
      this.inputFilenames = inputFiles;
      this.optimization = optimization;
      this.otherFlags = otherFlags;
      this.workingFoldername = workingDir;
      this.outputFlag = outputFlag;
      this.programExecutable = programExecutable;
   }

   /**
    * Launches the compilation job in a separate process.
    * 
    * @return
    */
   public int run() {
      // Create Folder for outputfile
      //File outputFil = new File(outputFile);
      //File outputFol = outputFil.getParentFile();
      /*
      if (!outputFol.exists()) {
         boolean success = outputFol.mkdirs();
         if (!success) {
            LoggingUtils.getLogger().
                    warning("Could not create output folder '" + outputFol + "'");
         }
      }
       *
       */



      List<String> command = new ArrayList<String>();
      command.add(getProgram());
      command.addAll(inputFilenames);


      command.add(optimization);
      command.addAll(otherFlags);
            command.add(getOutputFlag());
      command.add(outputFile);


        return ProcessUtils.runProcess(command, workingFoldername);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("Output:");
      builder.append(outputFile);
      builder.append("\n");

      builder.append("Input:\n");
      for(String input : inputFilenames) {
      builder.append(input);
      builder.append("\n");
      }

      builder.append("Optimization:");
      builder.append(optimization);
      builder.append("\n");

      builder.append("Flags:\n");
      builder.append(otherFlags);
      builder.append("\n");
      /*
      for(String flag : otherFlags) {
      builder.append(flag);
      builder.append("\n");
      }
       *
       */



      return builder.toString();
   }



   /**
    *
    * @return mb-gcc
    */
   public String getProgram() {
      return programExecutable;
   }

   /**
    *
    * @return -o
    */
   public String getOutputFlag() {
      return outputFlag;
   }

   public List<String> getInputFiles() {
      return inputFilenames;
   }

   public String getOptimization() {
      return optimization;
   }

   public List<String> getOtherFlags() {
      return otherFlags;
   }

   public String getOutputFile() {
      return outputFile;
   }


   /**
    * INSTANCE VARIABLES
    */
   String outputFile;
   List<String> inputFilenames;
   String optimization;
   List<String> otherFlags;
   String workingFoldername;
   String programExecutable;
   String outputFlag;
}
