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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class OptionsTableFactory {

   /**
    * Creates a new OptionsTable from an options config file.
    *
    * <p>The format of the options config file is key - value mapping where the
    * arguments are separated by a space (' ') character. The avaliable keys are:
    * <br><b>listSeparatorCharacter</b>: the string which will serve as separator
    * character in OptionTable's lists.
    * <br><b>option<i>n</i></b>: the full name of the enum implementing interface
    * OptionName to add to the OptionTable. This key can be used several times,
    * with different suffixes.
    *
    * <p>Config file example:
    * <i><br>listSeparatorCharacter ;
    * <br>option1 org.specs.Test.TestOption
    * <br>option2 org.specs.ProgramLauncher.ProgramOption</i>
    *
    * @param optionsConfig
    * @return
    */
   public static OptionsTable fromFile(File optionsConfig) {
      LineParser lineParser = new LineParser(SEPARATOR_KEY_VALUE, JOINER_STRING, COMMENT_PREFIX);

      Map<String, String> configMap = ParseUtils.parseTableFromFile(optionsConfig, lineParser);

      // Declare objects needed to build OptionsTable
      Map<String, OptionName> avaliableOptions = new HashMap<String, OptionName>();
      String listSeparatorCharacter = null;

      for(String key : configMap.keySet()) {
         // Check if option
         if(key.startsWith(KEY_OPTION_PREFIX)) {
            String className = configMap.get(key);
            Map<String, OptionName> optionNames = getOptionNamesFromClass(className);
            if(optionNames != null) {
               addOptions(avaliableOptions, optionNames);
            }
            continue;
         }

         if(key.startsWith(KEY_LIST_SEPARATOR)) {
            listSeparatorCharacter = configMap.get(key);
            continue;
         }

         Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Key not supported: '"+key+"'.");
      }

      return new OptionsTable(avaliableOptions, listSeparatorCharacter);
   }

   /**
    * Tries to load an enum class implementing the OptionName interface. If
    * successful, maps all its elements to a table. If an error occurs, the
    * motive is logged and null is returned.
    * 
    * If the given enum is empty, regardless of implementing OptionName or not,
    * returns an empty Map.
    *
    * @param enumClassName
    * @return
    */
   public static Map<String, OptionName> getOptionNamesFromClass(String enumClassName) {
      Class<?> c = null;

      // Get class
      try {
         c = Class.forName(enumClassName);
      } catch (ClassNotFoundException ex) {
         Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Class not found: '" + enumClassName + "'.");
         return null;
      } catch (NoClassDefFoundError err) {
          Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Error with class '" + enumClassName + "'. Message: "+err.getMessage());
         return null;
      }

      // Check if class is enum
      if (!c.isEnum()) {
         Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Class '" + enumClassName + "' is not an enum.");
         return null;
      }




      /*
      // Check if enum is empty
      if(enums.length == 0) {
         return enumOptions;
      }
*/
  /*
      try {
         OptionName optionObject = (OptionName) enums[0];
      } catch (ClassCastException ex) {
         Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Class '" + enumClassName + "' does not implement interface " + OptionName.class + ".");
         return null;
      }
    */


      /*
      if (!enumObjectClass.isInstance(OptionName.class)) {
         Logger.getLogger(OptionsTableFactory.class.getName()).
                 warning("Class '" + enumClassName + "' does not implement interface " + OptionName.class + ".");
         return null;
      }
       *
       */

      
      
      Map<String, OptionName> enumOptions = new HashMap<String, OptionName>();
      Object[] enums = c.getEnumConstants();

      for (Object option : enums) {
         OptionName optionObject = null;

         // Check 
         try {
            optionObject = (OptionName) option;
         } catch (ClassCastException ex) {
            Logger.getLogger(OptionsTableFactory.class.getName()).
                    warning("Class '" + enumClassName + "' does not implement interface " + OptionName.class + ".");
            return null;
         }
         String optionName = optionObject.getOptionName();
         enumOptions.put(optionName, optionObject);
      }

      return enumOptions;
   }
      
   private static void addOptions(Map<String, OptionName> totalOptions, Map<String, OptionName> partialOptions) {
      // Check if there are no duplicate options being added
      for(String key : partialOptions.keySet()) {
         OptionName returnValue = totalOptions.put(key, partialOptions.get(key));
         if(returnValue != null) {
            Logger.getLogger(OptionsTableFactory.class.getName()).
                    warning("Duplicated option name: '"+key+"'.");
         }
      }
   }


   public static final String SEPARATOR_KEY_VALUE = " ";
   public static final String COMMENT_PREFIX = "//";
   public static final String JOINER_STRING = "\"";

   public static final String KEY_OPTION_PREFIX = "option";
   public static final String KEY_LIST_SEPARATOR = "listSeparatorCharacter";




}
