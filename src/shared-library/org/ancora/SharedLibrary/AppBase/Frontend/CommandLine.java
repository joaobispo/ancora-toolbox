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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Acts as a command-line front-end for applications
 * which use the AppBase classes.
 *
 * @author Joao Bispo
 */
public class CommandLine {

   public CommandLine(App app) {
      this.app = app;
      optionFilesExtension = DEFAULT_OPTION_FILE_EXTENSION;
      optionClasses = new ArrayList<Class>();
   }

   /**
    * List of option classes associated with this object.
    *
    * @return
    */
   public List<Class> getOptionClasses() {
      return optionClasses;
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

      // PreParse some simple options
      boolean preParseResult = preParseAguments(args);
      if(preParseResult) {
         return 0;
      }

      Map<String, AppValue> options = parseArguments(args);

      if(options == null) {
         LoggingUtils.getLogger().
                 warning("Options table is null.");
         return -1;
      }

      return app.execute(options);

   }

   private Map<String, AppValue> parseArguments(String[] args) {
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


   private Map<String, AppValue> parseFolder(File file) {
      // Find options files inside folder
      List<File> optionFiles = IoUtils.getFilesRecursive(file, optionFilesExtension);

      // For each option file, get the table and merge contents with the mastertable
      Map<String, AppValue> masterTable = new HashMap<String, AppValue>();

      for(File optionFile : optionFiles) {
         //Map<String, AppValue> currentTable = AppUtils.parseFile(optionFile);
         // Introduced null on purpose, this object is deprecated and did this to not acuse errors.
         Map<String, AppValue> currentTable = null;

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


   private Map<String, AppValue> parseFiles(String[] args) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

      private boolean preParseAguments(String[] args) {
      if(args.length == 0) {
         return false;
      }

      if(args[0].equals(helpFlag)) {
         LoggingUtils.getLogger().
                 info(HELP_MESSAGE);
         return true;
      }

      if(args[0].equals(generateFlag)) {
         generateOptions();
         return true;
      }

      return false;
   }

   /**
    * Options for this program:
    * - Target.TargetOption
    */
   private void generateOptions() {
      if(optionClasses.isEmpty()) {
         LoggingUtils.getLogger().
                 info("This application has no option files.");
         return;
      }

      for (Class c : optionClasses) {
         String optionFilename = c.getSimpleName() + IoUtils.DEFAULT_EXTENSION_SEPARATOR
                 + optionFilesExtension;
         AppOptionFile.writeEmptyFile(new File(optionFilename), c);
         //IoUtils.write(new File(optionFilename), AppUtils.generateFile(c));
      }

   }


   /**
    * INSTANCE VARIABLES
    */
   private App app;
   private String optionFilesExtension;
   private List<Class> optionClasses;

   private static final String helpFlag = "-help";
   private static final String generateFlag = "-generate";
   public static final String DEFAULT_OPTION_FILE_EXTENSION = "option";
   public static final String HELP_MESSAGE =
           helpFlag + " - Shows this help message\n"
           + generateFlag + " - Creates a skeleton of the option files needed for this program\n"
           + "<nothing> - Uses current folder as source folder\n"
           + "<optionsFile, optionsFile...> - Loads the given option files\n"
           + "<folder> - Recursively reads all option files inside the given folder\n";

}
