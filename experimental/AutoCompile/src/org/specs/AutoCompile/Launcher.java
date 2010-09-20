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
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.AppBase.Frontend.CommandLine;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Options.JobOption;
import org.specs.AutoCompile.Options.Config;
import org.specs.AutoCompile.Options.TargetOption;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();

       // Load configuration file
       Map<String, AppOption> config = loadConfig();
       if(config == null) {
          LoggingUtils.getLogger().
                  warning("Could not load configuration file.");
          return;
       }

       // Parse command line and find a job to put on the table
       String jobFile = parseArguments(args);
       if(jobFile == null) {
          LoggingUtils.getLogger().
                  warning("Could not find a job.");
          return;
       }
       config.put(Config.jobFile.getName(), new AppOption(jobFile));

       // Create and launch application object
        AutoCompile aComp = new AutoCompile();
        aComp.execute(config);


        //CommandLine frontend = new CommandLine(aComp);

        // Link this program options to frontend
        //frontend.getOptionClasses().add(TargetOption.class);

        //frontend.run(args);

    }

   public static void writeOptionFiles() {
      IoUtils.write(new File("job.txt"), AppUtils.generateFile(JobOption.class));
      IoUtils.write(new File("config.dat"), AppUtils.generateFile(Config.class));
   }



   private static Map<String, AppOption> loadConfig() {
      // Check if config file exists
      File configFile = new File(CONFIG_FILE);
      if(!configFile.exists()) {
         LoggingUtils.getLogger().
                 warning("Could not load configuration file '"+configFile.getPath()+"'.");
         return null;
      }

      Map<String, AppOption> map = AppUtils.parseFile(configFile);

      if (map == null) {
         LoggingUtils.getLogger().
                 warning("'map' is null");
      }

      return map;
   }


   private static String parseArguments(String[] args) {
      if(args.length == 0) {
         LoggingUtils.getLogger().
                 warning("No job file specified.");
         return null;
      }

      if(args.length > 1) {
         LoggingUtils.getLogger().
                 info("Reading first argument and discarding the other arguments.");
      }

      File jobFile = new File(args[0]);
      if(!jobFile.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not locate file '"+jobFile.getPath()+"'");
         return null;
      }

      return jobFile.getPath();
   }

   // VARIABLES
   private final static String CONFIG_FILE = "config.dat";





}
