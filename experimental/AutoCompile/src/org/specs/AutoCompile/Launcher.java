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

import org.ancora.SharedLibrary.AppBase.SimpleCommandLine.SimpleCommandLine;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.IoUtils;
import org.specs.AutoCompile.Job.JobOption;

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
       LoggingUtils.getRootLogger().setLevel(Level.INFO);

       // Copy files to current folder
       copyNecessaryFiles();

       // Create autocompile application
       App autoCompile = buildAutoCompileApp();
       if(autoCompile == null) {
          return;
       }

       // Launch GUI
       if(args.length == 0) {
          SimpleGui gui = new SimpleGui(autoCompile);
          //MainWindow gui = new MainWindow(autoCompile);
          gui.setTitle("AutoCompile v0.1");
          gui.execute();
          return;
       }

       // Launch command line
       SimpleCommandLine command = new SimpleCommandLine(autoCompile);
       command.execute(args);


    }

   public static void writeOptionFiles() {
      AppOptionFile.writeEmptyFile(new File("job.txt"), JobOption.class);
      AppOptionFile.writeEmptyFile(new File("config.dat"), Config.class);
//      IoUtils.write(new File("job.txt"), AppUtils.generateFile(JobOption.class));
//      IoUtils.write(new File("config.dat"), AppUtils.generateFile(Config.class));
   }



   private static Map<String, AppValue> loadConfig() {
      // Check if config file exists
      File configFile = new File(CONFIG_FILE);
      if(!configFile.exists()) {
         LoggingUtils.getLogger().
                 warning("Could not load configuration file '"+configFile.getPath()+"'.");
         return null;
      }

      Class optionClass = Config.class;

//      Map<String, AppValue> map = AppUtils.parseFile(configFile);
      Map<String, AppValue> map = AppOptionFile.parseFile(configFile, optionClass).getMap();

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

/*
   public static int launchAutoCompile(String jobFilename) {
      // Load configuration file
      Map<String, AppValue> config = loadConfig();
      if (config == null) {
         LoggingUtils.getLogger().
                 warning("Could not load configuration file.");
         return -1;
      }


      // Get Config values
      String targetsFolderpath = AppUtils.getString(config, Config.targetFolder);
      AutoCompile aComp = AutoCompile.newAutoCompile(targetsFolderpath);
      if (aComp == null) {
         return -1;
      }

      
      //config.put(Config.jobFile.getName(), new AppValue(jobFilename));

      // Create and launch application object
      //String targetsFolderpath = AppUtils.getString(config, Config.targetFolder);
      //AutoCompile aComp = AutoCompile.newAutoCompile(targetsFolderpath);
      //new AutoCompile(targetsFolderpath);
    //  if (aComp == null) {
  //       return -1;
//      }

      //String jobFilepath = AppUtils.getString(config, Config.jobFile);
      //File jobFile = new File(jobFilepath);
      File jobFile = new File(jobFilename);
//      Map<String,AppValue> jobOptions = AppUtils.parseFile(jobFile);
      Map<String,AppValue> jobOptions = AppOptionFile.parseFile(jobFile, JobOption.class).getMap();

      //return aComp.execute(config);
      return aComp.execute(jobOptions);
   }
*/
   /*
   private static int launchCommandLine(String[] args) {
      // Parse command line and find a job to put on the table
      String jobFile = parseArguments(args);

      if (jobFile == null) {
         LoggingUtils.getLogger().
                 warning("Could not find a job.");
         return -1;
      }

      return launchAutoCompile(jobFile);
   }

*/
   /*
   private static void launchGui() {
      SimpleGui.main(null);
   }
    *
    */

   /**
    * Builds an AutoCompile application object.
    *
    * @return
    */
   public static App buildAutoCompileApp() {
      // Load configuration file
      Map<String, AppValue> config = loadConfig();
      if (config == null) {
         LoggingUtils.getLogger().
                 warning("Could not load configuration file.");
         return null;
      }

      // Get Config values
      String targetsFolderpath = AppUtils.getString(config, Config.targetFolder);
      AutoCompile aComp = AutoCompile.newAutoCompile(targetsFolderpath);
      if (aComp == null) {
         LoggingUtils.getLogger().
                 warning("Could not load AutoCompile application.");
         return null;
      }
      
      return aComp;
   }

   private static void copyNecessaryFiles() {
      // Copy CONFIG_FILE to run folder
      ClassLoader cl = Launcher.class.getClassLoader();
      InputStream input = null;

      input = cl.getResourceAsStream(CONFIG_RESOURCE);
      IoUtils.copy(input, new File(".", CONFIG_FILE));

      IoUtils.safeFolder("./targets");
      input = cl.getResourceAsStream(TARGET_RESOURCE);
      IoUtils.copy(input, new File(TARGET_FILE));
   }

   // VARIABLES
   private final static String CONFIG_FILE = "config.dat";


   private final static String CONFIG_RESOURCE = "org/specs/AutoCompile/config.dat";
   private final static String TARGET_RESOURCE = "org/specs/AutoCompile/targets/microblaze.mbgcc.option";
   private final static String TARGET_FILE = "./targets/microblaze.mbgcc.option";











}
