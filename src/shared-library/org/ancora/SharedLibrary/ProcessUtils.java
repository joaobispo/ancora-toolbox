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

package org.ancora.SharedLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Joao Bispo
 */
public class ProcessUtils {

   public static int runProcess(List<String> command, String workingDir) {
      int returnValue = -1;
      Logger logger = Logger.getLogger(ProcessUtils.class.getName());

      try {
         String commandString = getCommandString(command);
         logger.info("Running: " + commandString);
         

         ProcessBuilder builder = new ProcessBuilder(command);
         builder.directory(new File(workingDir));

         final Process process = builder.start();

         BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
         BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

         String errline = null;
         String stdline = null;

         //output(stdInput, stdError);
         
         while ((stdline = stdInput.readLine()) != null ||
                 (errline = stdError.readLine()) != null) {
            if(stdline != null) {
               System.out.println(stdline);
            }

            if(errline!= null) {
               System.err.println(errline);
            }

         }
          
          


         returnValue = process.waitFor();
         logger.info("Program terminated.");
      } catch (InterruptedException ex) {
         logger.warning("Program interrupted:"+ex.getMessage());
      } catch (IOException e) {
         logger.warning("IOException during program execution:"+e.getMessage());
      }

      return returnValue;
   }

   /**
    * Transforms a String List representing a command into a single String
    * separated by spaces.
    * 
    * @param command
    * @return
    */
    private static String getCommandString(List<String> command) {
      StringBuilder builder = new StringBuilder();

      builder.append(command.get(0));
      for(int i=1; i<command.size(); i++) {
         builder.append(" ");
         builder.append(command.get(i));
      }

      return builder.toString();
   }

   private static void output(BufferedReader stdInput, BufferedReader stdError) {
      String errline = null;
      String stdline = null;
      try {

         while((stdline = stdInput.readLine()) != null) {
            System.out.println(stdline);
         }

         while((errline = stdError.readLine()) != null) {
            System.out.println(errline);
         }

         /*
         while ((errline = stdError.readLine()) != null
                 || (stdline = stdInput.readLine()) != null) {
            if (errline != null) {
               System.err.println(errline);
            }
            if (stdline != null) {
               System.out.println(stdline);
            }
         }
          *
          */

      } catch (IOException e) {
         System.err.println("IOException during program execution:" + e.getMessage());
      }
   }
}
