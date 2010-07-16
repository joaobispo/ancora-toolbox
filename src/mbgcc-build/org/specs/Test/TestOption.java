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

package org.specs.Test;

import org.specs.OptionsTable.OptionName;

/**
 *
 * @author Joao Bispo
 */
public enum TestOption implements OptionName {

 option1("option-1", "default"),
 option2("option-2", "default2");

   private TestOption(String optionSuffix, String defaultValue) {
      this.optionSuffix = optionSuffix;
      this.defaultValue = defaultValue;
   }

   public String getOptionName() {
      return OPTION_PREFIX + SEPARATOR + optionSuffix;
   }

   public String getDefaultValue() {
      return defaultValue;
   }

   /**
    * INSTANCE VARIABLES
    */
   private final String optionSuffix;
   private final String defaultValue;
   private static final String OPTION_PREFIX = "test";
   private static final String SEPARATOR = ".";
}