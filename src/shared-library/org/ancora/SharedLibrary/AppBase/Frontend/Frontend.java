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

package org.ancora.SharedLibrary.AppBase.Frontend;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Acts as a command-line and GUI (yet to implement) front-end for applications
 * which use the AppBase classes.
 *
 * @author Joao Bispo
 */
public class Frontend {

   public Frontend(App app) {
      this.app = app;
      mode = Mode.commandLine;
      optionFilesExtension = DEFAULT_OPTION_FILE_EXTENSION;
   }

   /**
    * Parses the arguments and launches the application.
    *
    * <p>Rules for argument parsing:
    * <br>When no arguments are given, uses current folder as input folder;
    * <br>If the first argument is a folder, reads the options in that folder.
    * Ignores the remaining arguments;
    * <br>If the first argument is a file, interprets all arguments as option files;
    * <br>Returns if the first argument is not a valid path;
    * 
    * @param args
    * @return
    */
   public int run(String[] args) {

      Map<String, AppOption> options = parseArguments(args);

      if(options == null) {
         LoggingUtils.getLogger().
                 warning("Options table is null.");
         return -1;
      }

      return app.execute(options);

   }

   private Map<String, AppOption> parseArguments(String[] args) {
      if(args.length == 0) {
          LoggingUtils.getLogger(this).
                 info("No arguments given. Using current folder as source path"
                 + " for options files.");
         return parseFolder(new File("./"));
      }

      File firstArg = new File(args[0]);
      if(!firstArg.isFile()) {
         LoggingUtils.getLogger().
                 warning("First argument '"+args[0]+"' does not represent a "
                 + "valid path.");
         return null;
      }

      if(firstArg.isDirectory()) {
         LoggingUtils.getLogger(this).
                 info("First argument is a folder. Ignoring remaining arguments.");
         return parseFolder(firstArg);
      }

      return parseFiles(args);
   }


   private Map<String, AppOption> parseFolder(File file) {
      // Find options files inside folder
      List<File> optionFiles = IoUtils.getFilesRecursive(file, optionFilesExtension);

      // For each option file, get the table and merge contents with the mastertable
      Map<String, AppOption> masterTable = new HashMap<String, AppOption>();

      for(File optionFile : optionFiles) {
         Map<String, AppOption> currentTable = AppUtils.parseFile(optionFile);

         if(currentTable == null) {
            LoggingUtils.getLogger().
                    warning("File '"+optionFile+"' could not be parsed into an"
                    + "options table.");
            continue;
         }

         masterTable.putAll(currentTable);
      }

      return masterTable;
   }


   private Map<String, AppOption> parseFiles(String[] args) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   /**
    * INSTANCE VARIABLES
    */
   private App app;
   private Mode mode;
   private String optionFilesExtension;

   public static final String DEFAULT_OPTION_FILE_EXTENSION = "option";




   enum Mode {
      commandLine,
      gui;
   }
}
