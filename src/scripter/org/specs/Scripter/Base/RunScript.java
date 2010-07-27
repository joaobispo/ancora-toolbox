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

package org.specs.Scripter.Base;

import java.io.File;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.specs.Scripter.Program;
import org.specs.Scripter.Scripter;
import org.specs.Scripter.ScripterOption;


/**
 * Runs the commands written in a script file.
 *
 * @author Joao Bispo
 */
public class RunScript implements Program {

   public boolean execute(List<String> arguments, OptionsTable state) {
      // Check if has an argument
      if(arguments.isEmpty()) {
         LoggingUtils.getLogger(this).
                 info("Please provide a script file.");
         return true;
      }

     String scripFilename = arguments.get(0);
     File scriptFile = new File(scripFilename);

     // Create Scripter
     Scripter newScripter = new Scripter(ScripterOption.buildProgramsTable(state),
             ScripterOption.buildLineParser(state), state);

     return newScripter.runScript(scriptFile);
   }

   public String getHelpMessage(OptionsTable state) {
      return "Given a file with commands as argument, executes each of the commands.";
   }

}
