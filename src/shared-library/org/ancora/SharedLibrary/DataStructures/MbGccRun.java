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

package org.ancora.SharedLibrary.DataStructures;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 *
 * @author Joao Bispo
 */
public class MbGccRun {

   public MbGccRun(String outputFile, String[] inputFiles, String optimization, String[] otherFlags, String workingDir) {
      this.outputFile = outputFile;
      this.inputFiles = inputFiles;
      this.optimization = optimization;
      this.otherFlags = otherFlags;
      this.workingDir = workingDir;
   }

   public int run() {
      // Create Folder for outputfile
      File outputFil = new File(outputFile);
      File outputFol = outputFil.getParentFile();
      if (!outputFol.exists()) {
         boolean success = outputFol.mkdirs();
         if (!success) {
            Logger.getLogger(MbGccRun.class.getName()).
                    warning("Could not create output folder '" + outputFol + "'");
         }
      }
      


      List<String> command = new ArrayList<String>();
      command.add(getProgram());
      command.addAll(Arrays.asList(inputFiles));

      command.add(getOutputFlag());
      command.add(outputFile);
      command.add(optimization);
      command.addAll(Arrays.asList(otherFlags));


        return ProcessUtils.runProcess(command, workingDir);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("Output:");
      builder.append(outputFile);
      builder.append("\n");

      builder.append("Input:\n");
      for(String input : inputFiles) {
      builder.append(input);
      builder.append("\n");
      }

      builder.append("Optimization:");
      builder.append(optimization);
      builder.append("\n");

      builder.append("Flags:\n");
      for(String flag : otherFlags) {
      builder.append(flag);
      builder.append("\n");
      }



      return builder.toString();
   }



   /**
    * 
    * @return mb-gcc
    */
   public String getProgram() {
      return "mb-gcc";
   }

   /**
    * 
    * @return -o
    */
   public String getOutputFlag() {
      return "-o";
   }

   public String[] getInputFiles() {
      return inputFiles;
   }

   public String getOptimization() {
      return optimization;
   }

   public String[] getOtherFlags() {
      return otherFlags;
   }

   public String getOutputFile() {
      return outputFile;
   }


   /**
    * INSTANCE VARIABLES
    */
   String outputFile;
   String[] inputFiles;
   String optimization;
   String[] otherFlags;
   String workingDir;
}
