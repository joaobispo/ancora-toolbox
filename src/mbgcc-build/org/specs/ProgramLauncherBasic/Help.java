/*
 *  Copyright 2010 SPECS Research Group.
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

package org.specs.ProgramLauncherBasic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Utilities.LineParser;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.OptionsTable.OptionsTable;
import org.specs.ProgramLauncher.Program;
import org.specs.ProgramLauncher.ProgramLauncher;
import org.specs.ProgramLauncher.ProgramOption;

/**
 * Shows help messages of the supported programs by program launcher.
 *
 * @author Joao Bispo
 */
public class Help implements Program {

   @Override
   public boolean execute(List<String> arguments, OptionsTable state) {
      // Get supported programs
      String filename = state.get(ProgramOption.programsFilename);
      File file = new File(filename);
      //Map<String, String> supportedPrograms = (new LineParser()).getTableFromFile(file);
      LineParser lineParser = new LineParser(" ", "\"", "//");
      Map<String, String> supportedPrograms = ParseUtils.parseTableFromFile(file, lineParser);

      Logger logger = LoggingUtils.getLogger(this);

      // If there are no arguments, show list of programs
      if(arguments.isEmpty()) {
         logger.info("Supported programs:");
         //logger.info("");
         List<String> sortedNames = new ArrayList<String>(supportedPrograms.keySet());
         Collections.sort(sortedNames);
         for(String key : sortedNames) {
            logger.info(key);
         }

         return true;
      }

      // Get program correspondent to first argument and show its help
      String programName = arguments.get(0);
      Program program = ProgramLauncher.getProgram(programName, supportedPrograms);

      if(program == null) {
         return false;
      }

      logger.info(program.getHelpMessage(state));
      return true;
   }

   @Override
   public String getHelpMessage(OptionsTable state) {
      return "Write the command with no arguments to see a list of supported programs, " +
              "or give the name of a program as argument to show its help message.";
   }

}
