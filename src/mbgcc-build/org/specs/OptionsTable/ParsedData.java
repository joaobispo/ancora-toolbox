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

package org.specs.OptionsTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for data collected after parsing.
 *
 * @author Joao Bispo
 */
class ParsedData {

   public ParsedData() {
      this.separator = OptionsTable.DEFAULT_LIST_SEPARATOR;
      //this.avaliableOptions = new HashSet<OptionName>();
      this.avaliableOptions = new HashMap<String, OptionName>();
   }

   //public Set<OptionName> getAvaliableOptions() {
   public Map<String, OptionName> getAvaliableOptions() {
      return avaliableOptions;
   }

   public String getSeparator() {
      return separator;
   }

   //public void setAvaliableOptions(Set<OptionName> avaliableOptions) {
   public void setAvaliableOptions(Map<String, OptionName> avaliableOptions) {
      this.avaliableOptions = avaliableOptions;
   }

   public void setSeparator(String separator) {
      this.separator = separator;
   }

 

   private String separator;
   //private Set<OptionName> avaliableOptions;
   private Map<String, OptionName> avaliableOptions;
}
