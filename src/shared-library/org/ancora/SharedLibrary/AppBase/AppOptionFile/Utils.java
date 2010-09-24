/*
 *  Copyright 2010 Ancora Research Group.
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

package org.ancora.SharedLibrary.AppBase.AppOptionFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.Extra.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;


/**
 * Several utility methods
 *
 * @author Joao Bispo
 */
public class Utils {

/**
 * Converts a single entry to text.
 *
 * @param entry
 * @return
 */
   public static String toString(Entry entry) {
       StringBuilder builder = new StringBuilder();

       // Put comments
       for(String comment : entry.getComments()) {
         builder.append(comment);
         builder.append(NEWLINE);
       }

       // Put value
       AppValue value = entry.getOptionValue();

       if(value.getType().isList()) {
         for(String singleValue : value.getList()) {
            String line = buildLine(entry.getOptionName(), singleValue, value.getType());
            builder.append(line);
            builder.append(NEWLINE);
         }
       } else {
          String line = buildLine(entry.getOptionName(), value.get(), value.getType());
            builder.append(line);
            builder.append(NEWLINE);
       }

      return builder.toString();
   }


   /**
    * Builds a string representation of an option line.
    * 
    * @param optionName
    * @param singleValue
    * @param appValueType
    * @return
    */
   public static String buildLine(String optionName, String singleValue, AppValueType appValueType) {
      StringBuilder builder = new StringBuilder();
            builder.append(optionName);
            builder.append(SPACE);
            builder.append(appValueType.getAttributionString());
            builder.append(SPACE);
            builder.append(singleValue);

            return builder.toString();
   }

      /**
    * @param line
    * @return true if line is either empty after trimming spaces, or if it
    * starts with comment prefix
    */
   public static boolean isComment(String line) {
      String trimmedLine = line.trim();
      if(trimmedLine.isEmpty()) {
         return true;
      }

      if(trimmedLine.startsWith(COMMENT_PREFIX)) {
         return true;
      }

      return false;
   }


  /**
   *
   * @param line
   * @return String array with two elements; the first is the name of the option,
   * the second is the value.
   */
   public static String[] parseLine(String line) {
      String[] values = new String[2];

      int spaceIndex = line.indexOf(SPACE);
      if(spaceIndex == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find a space separating the name of the option from the "
                 + "operator in line '"+line+"'.");
         return null;
      }
      values[0] = line.substring(0, spaceIndex).trim();

      line = line.substring(spaceIndex+1);
      spaceIndex = line.indexOf(SPACE);
      if(spaceIndex == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find a second space separating the operator "
                 + "from the value in line '"+line+"'.");
         return null;
      }

      // Ignore separator. Is only "decoration"
      values[1] = line.substring(spaceIndex+1).trim();

      return values;
   }

      /**
    * Builds the Entry list.
    *
    * @param lineReader
    * @param enumMap
    * @return
    */
   //private static List<Entry> buildEntries(LineReader lineReader, Map<String, AppOptionEnum> enumMap) {
   public static EntryList buildEntries(LineReader lineReader, Map<String, AppOptionEnum> enumMap) {
      //List<Entry> entries = new ArrayList<Entry>();
      EntryList entries = new EntryList();

      // Start a new comment list
      List<String> comments = new ArrayList<String>();

      String line = null;
      boolean incorrectOption = false;
      while((line = lineReader.nextLine()) != null) {
         // Check if line is comment
         if(Utils.isComment(line)) {
            comments.add(line);
            continue;
         }

         // Check if line is option
         String[] parsedLine = Utils.parseLine(line);
         if(parsedLine == null) {
            LoggingUtils.getLogger().
                    warning("Ignoring line "+lineReader.getLastLineIndex()+".");
            continue;
         }

         String optionName = parsedLine[0];
         String optionValue = parsedLine[1];

         // Check if option is part of the options class
         AppOptionEnum optionEnum = enumMap.get(optionName);
         if(optionEnum == null) {
            LoggingUtils.getLogger().
                    warning("Option '"+optionName+"' does not belong to the application list of options. "
                    + "Ignoring line "+lineReader.getLastLineIndex()+".");
            incorrectOption = true;
            continue;
         }

         // Add new entry
         entries.addEntry(optionName, optionValue, optionEnum, comments);
         comments = new ArrayList<String>();

      }

      if(incorrectOption) {
         showOptions(enumMap);
      }

      return entries;
   }


   public static void showOptions(Map<String, AppOptionEnum> enumMap) {
      Logger logger = LoggingUtils.getLogger();
      logger.info("Available options for this program:");
      for(String key : enumMap.keySet()) {
         logger.info(" -> "+key);
      }
   }

   /**
    * Transforms 
    * @param appOptionEnum
    * @return
    */
   public static Map<String, AppOptionEnum> getEnumMap(Class appOptionEnum) {
       // Check class
      if (!appOptionEnum.isEnum()) {
         LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not represent an enum.");
         return null;
      }

      // Build set with interfaces of the given class
      Set<Class> interfaces = new HashSet<Class>(Arrays.asList(appOptionEnum.getInterfaces()));
      Class appOption = AppOptionEnum.class;
      if(!interfaces.contains(appOption)){
           LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not implement "
                 + "interface '"+appOption+"'.");
         return null;
      }

      // Get map of enums
       Map<String, AppOptionEnum> enumMap = AppUtils.buildMap((AppOptionEnum[]) appOptionEnum.getEnumConstants());

       return enumMap;
   }

   /**
    * 
    * @param appOptionType
    * @return default values for the given AppValueType
    */
   public static String getDefaultValue(AppValueType appOptionType) {
      if(appOptionType == AppValueType.bool) {
         return "false";
      }

      if(appOptionType == appOptionType.integer) {
         return "0";
      }

      return "";
   }

   public static final String COMMENT_PREFIX = "//";
   public static final String SPACE = " ";
   public static final String NEWLINE = "\n";

}
