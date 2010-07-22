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

package org.ancora.SharedLibrary.OptionsTable;

/**
 * Options for the ProgramLauncher
 *
 * @author Joao Bispo
 */
public enum ProgramOption implements OptionName {

   programsFilename("supported-programs",""),
   isRunningShell("shellcyclestate","true"),
   loggerLevel("logger-level","ALL");

   private ProgramOption(String optionSuffix, String defaultValue) {
      this.optionSuffix = optionSuffix;
      this.defaultValue = defaultValue;
   }

   

   @Override
   public String getOptionName() {
      return OPTION_PREFIX + SEPARATOR + optionSuffix;
   }

   @Override
   public String getDefaultValue() {
      return defaultValue;
   }

   /**
    * INSTANCE VARIABLES
    */
   private final String optionSuffix;
   private final String defaultValue;
   private static final String OPTION_PREFIX = "scripter";
   private static final String SEPARATOR = ".";
}
