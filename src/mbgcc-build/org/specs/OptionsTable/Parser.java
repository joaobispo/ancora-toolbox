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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LineReader;
import org.ancora.SharedLibrary.Parsing.CommandParser;

/**
 * Parses files with information to create OptionsTable objects.
 *
 * @author Joao Bispo
 */
class Parser {

   /**
    *
    * @param enumFile
    * @return
    */
   public static ParsedData parseEnumFile(File enumFile) {
      ParsedData parsedData = new ParsedData();

      LineReader lineReader = LineReader.createLineReader(enumFile);
      CommandParser commandParser = new CommandParser();

      Set<String> addedOptions = new HashSet<String>();
      String line;
      while((line = lineReader.nextLine()) != null) {
         List<String> arguments = commandParser.splitCommand(line);
         
         // Ignore
         if(arguments.isEmpty()) {
            continue;
         }

         // Collect options from enum
         if(arguments.size() == 1) {
            addEnumOptions(parsedData, arguments.get(0), addedOptions);
            continue;
         }

         // Set option
         if(arguments.size() > 1) {
            setSetting(parsedData, arguments);
         }
      }

      return parsedData;
   }



   private static void addEnumOptions(ParsedData parsedData, String enumClass, Set<String> addedOptions) {
      Class<?> c = null;

      // Get class
      try {
          c = Class.forName(enumClass);
      } catch (ClassNotFoundException ex) {
         Logger.getLogger(Parser.class.getName()).
                 warning("Enum not found: '"+enumClass+"'.");
         return;
      }

      // Check if class is enum
      if(!c.isEnum()) {
         Logger.getLogger(Parser.class.getName()).
                 warning("Class '"+enumClass+"' is not an enum.");
         return;
      }

      Object[] enums = c.getEnumConstants();
      for(Object option : enums) {
         OptionName optionObject = (OptionName)option;
         // Check if optionName is still avaliable
         String optionName = optionObject.getOptionName();
//         boolean isSuccess = addedOptions.add(optionName);
         boolean containsKey = parsedData.getAvaliableOptions().containsKey(optionName);
         //if(!isSuccess) {
         if(containsKey) {
            Logger.getLogger(Parser.class.getName()).
                    warning("Duplicated option name: '"+optionName+"'");
            continue;
         }

         //parsedData.getAvaliableOptions().add(optionObject);
         parsedData.getAvaliableOptions().put(optionName, optionObject);

         /*
         boolean isSuccess = parsedData.getAvaliableOptions().add(optionName);
         if(!isSuccess) {
            Logger.getLogger(Parser.class.getName()).
                    warning("Duplicated option name: '"+optionName+"'");
         }
          * 
          */
      }

   }

   private static void setSetting(ParsedData parsedData, List<String> arguments) {
      // Check setting
      String key = arguments.get(0);
      String value = arguments.get(1);

      if(key.equals(SETTING_SEPARATOR)) {
         parsedData.setSeparator(value);
         return;
      }

      Logger.getLogger(Parser.class.getName()).
              warning("Setting does not exist: '"+key+"'");
   }


   public static final String COMMENT_PREFIX = "//";
   public static final String SETTING_SEPARATOR = "list-separator";
}
