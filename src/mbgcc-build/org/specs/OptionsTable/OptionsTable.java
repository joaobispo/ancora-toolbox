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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.ParseUtils;

/**
 * Table which maps OptionName keys to String values.
 *
 * @author Joao Bispo
 */
public class OptionsTable {

//   private OptionsTable(Set<String> avaliableOptions, String listSeparator) {
//   private OptionsTable(Set<OptionName> avaliableOptions, String listSeparator) {
   private OptionsTable(Map<String, OptionName> avaliableOptions, String listSeparator) {
      this.optionsTable = new HashMap<String, String>();
      this.avaliableOptions = avaliableOptions;
      this.listSeparator = listSeparator;
   }



   /**
    * Creates a new OptionsTable from an enumOptions file.
    *
    * @param optionsFile a file containing the names of enums implementing the OptionName interface
    * @return a new OptionsTable
    */
   public static OptionsTable newOptionsTable(File enumOptions) {
      // Parse file, get Enum objects
      ParsedData parsedData = Parser.parseEnumFile(enumOptions);
      // Use return to create OptionsTable
      return new OptionsTable(parsedData.getAvaliableOptions(), parsedData.getSeparator());
   }

   /*
   public String getKeyString(String key) {
       // Check if table contains value
      String value = optionsTable.get(key);

      return value;
   }
    *
    */

   /**
    * Returns the value to which the specified key is mapped, or 
    * the default value if this map contains no mapping for the key.
    *
    * @param key
    * @return the value associated with the given key
    */
   public String get(OptionName key) {
      if(!isValid(key)) {
         return null;
      }

      String optionName = key.getOptionName();

      // Check if table contains value
      String value = optionsTable.get(optionName);

      // Check if value is defined on table
      if(value == null) {
         return key.getDefaultValue();
      }

      return value;
   }

   public List<String> getList(OptionName key) {
      String value = get(key);
      if(value == null) {
         return null;
      }

      String[] values = value.split(listSeparator);

      List<String> stringList = new ArrayList<String>();
      for(int i=0; i<values.length; i++) {
         if(values[i].length() > 0) {
            stringList.add(values[i]);
         }
      }
      

      return stringList;
   }

   /**
    * Associates the specified value with the specified key in this map.
    *
    * @param key
    * @param value
    */
   public void set(OptionName key, String value) {
      if(!isValid(key)) {
         return;
      }

      optionsTable.put(key.getOptionName(), value);
   }

   /**
    * Adds a parameters to a "list" variable.
    *
    * <p>If there is no value defined, when adding to a key it will override the
    * default value, instead of adding to the end of the default value.
    *
    * @param key
    * @param value
    */
   public void add(OptionName key, String value) {
      if (!isValid(key)) {
         return;
      }

      // Get old value
      String oldValue = optionsTable.get(key.getOptionName());
      String newValue = "";

      if(oldValue == null) {
         newValue = value;
      } else {
         newValue = oldValue + listSeparator + value;
      }

      optionsTable.put(key.getOptionName(), newValue);
   }

   private boolean isValid(OptionName key) {
//      String optionName = key.getOptionName();
//      if(!avaliableOptions.contains(optionName)) {
      //if(!avaliableOptions.contains(key)) {
      if(!avaliableOptions.containsKey(key.getOptionName())) {
         Logger.getLogger(OptionsTable.class.getName()).
//                 warning("Invalid option:"+optionName);
                 warning("Invalid option:"+key.getOptionName());
         return false;
      }

      return true;
   }

   /**
    *
    * @return list with avaliable options
    */
   public List<OptionName> getAvaliableOptions() {
 /*
      //List<OptionName> sortedOptions = new ArrayList<OptionName>(avaliableOptions);
      List<OptionName> sortedOptions = new ArrayList<OptionName>(avaliableOptions.values());
      //Collections.sort(sortedOptions);
      //List<String> sortedOptions = ParseUtils.getSortedList(avaliableOptions);
      Collections.sort(sortedOptions, new Comparator<OptionName>() {

         public int compare(OptionName o1, OptionName o2) {
            return o1.getOptionName().compareTo(o2.getOptionName());
         }
      });
*/

      List<OptionName> sortedOptions = new ArrayList<OptionName>();
      Set<String> keySet = avaliableOptions.keySet();
      List<String> keys = ParseUtils.getSortedList(keySet);
      for(String key : keys) {
         sortedOptions.add(avaliableOptions.get(key));
      }
      return sortedOptions;
   }

   /**
    * @param optionName
    * @return the OptionName object correspondent to the given name, or null if
    * there is no such mapping
    */
   public OptionName getOption(String optionName) {
      return avaliableOptions.get(optionName);
   }

   /**
    * INSTANCE VARIABLES
    */
   private Map<String, String> optionsTable;
   //private Set<String> avaliableOptions;
   //private Set<OptionName> avaliableOptions;
   private Map<String, OptionName> avaliableOptions;
   private String listSeparator;

   public static String DEFAULT_LIST_SEPARATOR = " ";
}
