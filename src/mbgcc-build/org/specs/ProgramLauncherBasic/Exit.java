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

import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.OptionsTable.OptionsTable;
import org.specs.ProgramLauncher.Program;
import org.specs.ProgramLauncher.ProgramOption;

/**
 * Sets the program launcher shell cycle variable to false, exiting the shell
 * loop.
 *
 * @author Joao Bispo
 */
public class Exit implements Program {

   public boolean execute(List<String> arguments, OptionsTable state) {
      state.set(ProgramOption.isRunningShell, "false");
      LoggingUtils.getLogger(this).
              info("Bye!");
      return true;
   }

   public String getHelpMessage(OptionsTable state) {
      return "Exits from the Shell mode. There are no additional options.";
   }

}
