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

import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionName;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.specs.Scripter.Program;


/**
 * Concatenates a new value to the end of a value of the current program state,
 * forming a list.
 * <br> If more than one value is given as parameter, the values after the first
 * paramenter are ignored.
 *
 * @author Joao Bispo
 */
public class Add implements Program {

   @Override
   public boolean execute(List<String> arguments, OptionsTable state) {

      // Check arguments
      int minimumArgs = 1;
      if (arguments.size() < minimumArgs) {
         LoggingUtils.getLogger(this).
                 warning("Too few arguments (" + arguments.size() + "). Minimum is "
                 + minimumArgs + ".");
         return false;
      }

      // Get option
      OptionName optionName = state.getOption(arguments.get(0));

      if(optionName == null) {
         LoggingUtils.getLogger(this).
                 warning("'"+arguments.get(0)+"' is not a valid setting.");
         return false;
      }


      if(arguments.size() == 1) {
         LoggingUtils.getLogger(this).
                 info("No value defined for 'add' operation on key '"+optionName.getOptionName()+"'");
         return true;
      } 

     String value = arguments.get(1);
      

      // Introduce value
      state.add(optionName, value);

      return true;
   }

   @Override
   public String getHelpMessage(OptionsTable state) {
      StringBuilder builder = new StringBuilder();
      builder.append("Concatenates values to existing options, forming lists. " +
              "Current lists and values:\n");
      List<OptionName> keys = state.getAvaliableOptions();
      for(OptionName key : keys) {
         List<String> list = state.getList(key);
         if(list.size() < 2) {
            continue;
         }

         builder.append(key.getOptionName());
         builder.append(" = ");
         builder.append(state.get(key));
         builder.append("\n");
      }

      return builder.toString();
   }

}
