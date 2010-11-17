/*
 *  Copyright 2010 SPeCS Research Group.
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

package org.ancora.SharedLibrary.AppBase.SimpleCommandLine;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Command-line frontend for applications which use the AppBase classes.
 *
 * Looks for a valid options file in the given arguments. Only uses the first
 * argument, ignores the others. Loads the option file and launches the application.
 *
 * @author Joao Bispo
 */
public class SimpleCommandLine {

   public SimpleCommandLine(App application) {
      this.application = application;
   }

   /**
    * Looks for a valid options file in the given arguments. Only uses the first
    * argument, ignores the others. Loads the option file and launches the
    * application.
    *
    * @param args
    * @return
    */
   public int execute(String[] args) {
      // Parse command line and find a job to put on the table
      File optionFile = parseArguments(args);

      if (optionFile == null) {
         LoggingUtils.getLogger().
                 warning("Could not find an option file.");
         return -1;
      }

      // Load optionFile into Map
      Class optionClass = application.getAppOptionEnum();
      Map<String, AppValue> map = AppOptionFile.parseFile(optionFile, optionClass).getMap();


      try {
         return application.execute(map);
   
   } catch (InterruptedException ex) {
         LoggingUtils.getLogger().
                 info("Cancelling application.");
         Thread.currentThread().interrupt();
         return -1;
      }
    
   }

   /**
    * 
    * @param args
    * @return
    */
   private File parseArguments(String[] args) {
      if(args.length == 0) {
         LoggingUtils.getLogger().
                 warning("No option file specified.");
         return null;
      }

      if(args.length > 1) {
         LoggingUtils.getLogger().
                 info("Reading first argument and discarding the other arguments.");
      }

      File optionFile = new File(args[0]);
      if(!optionFile.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not locate file '"+optionFile.getPath()+"'");
         return null;
      }

      return optionFile;
   }

   private final App application;
}
