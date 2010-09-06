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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Table which maps OptionName keys to String values.
 *
 * @author Joao Bispo
 */
public class OptionsTable {


   /**
    * Creates a new OptionsTable.
    * 
    * @param avaliableOptions table which maps the name of the option (String) to
    * the correspondent OptionName object
    * @param listSeparator OptionsTable can build and retrieve lists, but stores
    * them as a String. This parameters indicates what is the string which works
    * as separator. It should be one character which is never used in the values
    * of the list. The default separator is ";"
    */
   public OptionsTable(Map<String, OptionName> avaliableOptions, String listSeparator) {
      this.optionsTable = new HashMap<String, String>();
      this.avaliableOptions = avaliableOptions;
      this.listSeparator = listSeparator;
   }




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
    * Clears the field correspondent to key.
    * 
    * @param key
    */
   public void clear(OptionName key) {
      if(!isValid(key)) {
         return;
      }

      optionsTable.put(key.getOptionName(), null);
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
      // Check if optionTable supports given key
      if (!isValid(key)) {
         return;
      }

      // Adding a string to a list; Check if string does not contain the
      // separator character
      if(value.contains(listSeparator)) {
         StackTraceElement stackElement = ProcessUtils.getCallerMethod();
         Logger logger = LoggingUtils.getLogger(this);
         logger.warning("Did not add element '"+value+"' to list "+key.getOptionName()+
                 " because it contains the separator character '"+listSeparator+"'.");
         logger.warning("The method who made this call was '"+stackElement+"'.");


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
      if(!avaliableOptions.containsKey(key.getOptionName())) {
         Logger.getLogger(OptionsTable.class.getName()).
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
      OptionName option = avaliableOptions.get(optionName);
      if(option == null) {
         LoggingUtils.getLogger(this).
                 warning("Option '"+optionName+"' not found. Returning null.");
         return null;
      }

      return option;
   }

   public String getListSeparator() {
      return listSeparator;
   }


   /**
    * INSTANCE VARIABLES
    */
   private final Map<String, String> optionsTable;
   private final Map<String, OptionName> avaliableOptions;
   private final String listSeparator;

}
