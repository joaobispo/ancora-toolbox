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
 * Represents the name of an option to be used as a key for OptionsTable.
 *
 * <p>This interface is to be implemented by enums.
 *
 * @author Joao Bispo
 */
public interface OptionName {

   /**
    * @return a String representing the name of the option
    */
   String getOptionName();
   /**
    *
    * @return a String with the default value for this option
    */
   String getDefaultValue();

}
