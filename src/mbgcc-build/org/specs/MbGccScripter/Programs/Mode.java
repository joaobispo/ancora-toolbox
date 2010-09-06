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

package org.specs.MbGccScripter.Programs;

import java.util.List;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.specs.MbGccScripter.GccModes;
import org.specs.MbGccScripter.MbGccOption;
import org.specs.Scripter.Program;

/**
 *
 * @author Joao Bispo
 */
public class Mode implements Program {

   @Override
   public boolean execute(List<String> arguments, OptionsTable state) {
      // Get mode name
      if(arguments.isEmpty()) {
         LoggingUtils.getLogger(this).
                 warning("No argument given. Current mode is set to '"+state.get(MbGccOption.gccMode)+"'");
         return false;
      }

      String modeName = arguments.get(0);
      // Get enum
      GccModes mode = EnumUtils.valueOf(GccModes.class, modeName);

      if (mode == null) {
         LoggingUtils.getLogger(this).
                 warning("Invalid mode: '" + modeName + "'. Current mode is set to '"+state.get(MbGccOption.gccMode)+"'");
         return false;
      }

      state.set(MbGccOption.gccMode, modeName);
      return true;
   }

   @Override
   public String getHelpMessage(OptionsTable state) {
      StringBuilder builder = new StringBuilder();
      builder.append("Sets the compilation mode. Avaliable modes:\n");
      for(GccModes mode : GccModes.values()) {
         builder.append(mode.name());
         builder.append("\n");
      }

      return builder.toString();
   }

}
