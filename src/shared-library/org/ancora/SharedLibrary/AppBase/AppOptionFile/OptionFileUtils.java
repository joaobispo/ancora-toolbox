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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.DefaultValues;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.Utilities.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;


/**
 * Several utility methods related to handling of AppOptionFile objects.
 *
 * @author Joao Bispo
 */
public class OptionFileUtils {

   /**
    * Converts a single entry to text.
    *
    * @param entry
    * @return
    */
   public static String toString(Entry entry) {
      StringBuilder builder = new StringBuilder();

      // Put comments
      for (String comment : entry.getComments()) {
         builder.append(comment);
         builder.append(NEWLINE);
      }

      // Put value
      AppValue value = entry.getOptionValue();

      if (value.getType().isList()) {
         for (String singleValue : value.getList()) {
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
         if(OptionFileUtils.isComment(line)) {
            comments.add(line);
            continue;
         }

         // Check if line is option
         String[] parsedLine = OptionFileUtils.parseLine(line);
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
         entries.addEntry(optionName, optionValue, optionEnum.getType(), comments);
         comments = new ArrayList<String>();

      }

      if(incorrectOption) {
         showOptions(enumMap);
      }

      // Check if there are entries missing, and fill them with default values
      addMissingEntries(entries, enumMap);

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
    * Convert the last non-empty lines of the list of Strings in a string of 
    * text.
    *  
    * @param comments
    * @return
    */
    public static String parseComments(List<String> comments) {
      // Check how many non-empty lines there is counting from bottom.
      int counter = 0;
      for (int i = comments.size() - 1; i >= 0; i--) {
         String line = comments.get(i);
         if (line.isEmpty()) {
            break;
         }
         counter++;
      }

       StringBuilder builder = new StringBuilder();
       for (int i = comments.size() - counter; i < comments.size(); i++) {
         String trimmedLine = comments.get(i).trim();
         trimmedLine = trimmedLine.substring(OptionFileUtils.COMMENT_PREFIX.length());
         builder.append(trimmedLine);
         builder.append(" ");
      }

       return builder.toString();
    }


    /**
     * Checks if there is an entry for all enumMap options.
        *
    * @param entries
    * @param enumMap
    */
   private static void addMissingEntries(EntryList entries, Map<String, AppOptionEnum> enumMap) {
      Set<String> entryKeys = entries.getEntriesMapping().keySet();
      Set<String> enumMapKeys = new HashSet<String>(enumMap.keySet());
      enumMapKeys.removeAll(entryKeys);



      for (String key : enumMapKeys) {
         AppOptionEnum optionEnum = enumMap.get(key);
         boolean hasDefaults = ProcessUtils.implementsInterface(optionEnum.getClass(), DefaultValues.class);
         String optionValue = null;
         if (hasDefaults) {
            optionValue = AppUtils.getDefaultValue(optionEnum.getType(), (DefaultValues) optionEnum);
         } else {
            optionValue = AppUtils.getZeroValue(optionEnum.getType());
         }

         // Add new entry
         entries.addEntry(key, optionValue, optionEnum.getType(), new ArrayList<String>());

      }

   }

   public static final String COMMENT_PREFIX = "//";
   public static final String SPACE = " ";
   public static final String NEWLINE = "\n";


}
