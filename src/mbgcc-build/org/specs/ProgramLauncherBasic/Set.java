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
import java.util.logging.Level;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.OptionsTable.OptionName;
import org.specs.OptionsTable.OptionsTable;
import org.specs.ProgramLauncher.Program;
import org.specs.ProgramLauncher.ProgramOption;

/**
 * Sets the values of the current program state. If more than one value is given
 * as parameter, the values after the first are ignored.
 *
 * @author Joao Bispo
 */
public class Set implements Program {

   @Override
   public boolean execute(List<String> arguments, OptionsTable state) {
      // Check arguments

      int minimumArgs = 1;
      if(arguments.size() < minimumArgs) {
         LoggingUtils.getLogger(this).
                 warning("Too few arguments ("+arguments.size()+"). Minimum is "+
                 minimumArgs+".");
         return false;
      }

      // Get option
      OptionName optionName = state.getOption(arguments.get(0));
 
      if(optionName == null) {
         LoggingUtils.getLogger(this).
                 warning("'"+arguments.get(0)+"' is not a valid setting.");
         return false;
      }

      String value = null;
      if(arguments.size() == 1) {
         value = "";
      } else {
         value = arguments.get(1);
      }

      // Concatenate remainging arguments into a single string
      // If this is done, do it on the CommandParser
      /*
      if(arguments.size() > 2) {
         StringBuilder builder = new StringBuilder();
         builder.append(value);
         for(int i=2; i<arguments.size(); i++) {
            builder.append(CommandParser.COMMAND_SEPARATOR);
            builder.append(arguments.get(i));
         }
         value = builder.toString();
      }
       */

      // Introduce value
      state.set(optionName, value);

      // Special cases
      specialCases(optionName, state);

      return true;

   }

   private void specialCases(OptionName option, OptionsTable state) {      
      String optionName = option.getOptionName();

      // If loggerlevel, reset logger
      if (optionName.equals(ProgramOption.loggerLevel.getOptionName())) {
         String levelString = state.get(ProgramOption.loggerLevel);
         Level level = LoggingUtils.parseLevel(levelString);
         LoggingUtils.getRootLogger().setLevel(level);
      }
   }

   @Override
   public String getHelpMessage(OptionsTable state) {
      StringBuilder builder = new StringBuilder();
      builder.append("Sets options in the program state. Current options and values:\n");
      List<OptionName> keys = state.getAvaliableOptions();
      for(OptionName key : keys) {
         builder.append(key.getOptionName());
         builder.append(" = ");
         builder.append(state.get(key));
         builder.append("\n");
      }

      return builder.toString();
   }

}
