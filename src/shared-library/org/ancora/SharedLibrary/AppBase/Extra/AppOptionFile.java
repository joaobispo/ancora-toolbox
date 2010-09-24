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

package org.ancora.SharedLibrary.AppBase.Extra;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Interface between files and AppOptions.
 *
 * @author Joao Bispo
 */
public class AppOptionFile {



   /**
    * Parses an option file. Needs an appOptionEnum class correspondent to the
    * options inside the file.
    * 
    * @param optionFile
    * @param appOptionEnum
    * @return
    */
   public static AppOptionFile parseFile(File optionFile, Class appOptionEnum) {
      // Check file
      LineReader lineReader = LineReader.createLineReader(optionFile);
      if(lineReader == null) {
         LoggingUtils.getLogger().
                 warning("LineReader is null.");
         return null;
      }
      
      // Check class
      if (!appOptionEnum.isEnum()) {
         LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not represent an enum.");
         return null;
      }

      // Get map of enums
       Map<String, AppOptionEnum> enumMap = AppUtils.buildMap((AppOptionEnum[]) appOptionEnum.getEnumConstants());

       // Get list of entries
       //List<Entry> entries = buildEntries(lineReader, enumMap);
       EntryList entryList = buildEntries(lineReader, enumMap);

      return null;
   }

   /**
    * Builds the Entry list.
    *
    * @param lineReader
    * @param enumMap
    * @return
    */
   //private static List<Entry> buildEntries(LineReader lineReader, Map<String, AppOptionEnum> enumMap) {
   private static EntryList buildEntries(LineReader lineReader, Map<String, AppOptionEnum> enumMap) {
      //List<Entry> entries = new ArrayList<Entry>();
      EntryList entries = new EntryList();

      // Start a new comment list
      List<String> comments = new ArrayList<String>();

      String line = null;
      boolean incorrectOption = false;
      while((line = lineReader.nextLine()) != null) {
         // Check if line is comment
         if(isComment(line)) {
            comments.add(line);
            continue;
         }

         // Check if line is option
         String[] parsedLine = parseLine(line);
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

   /**
    * @param line
    * @return true if line is either empty after trimming spaces, or if it
    * starts with comment prefix
    */
   private static boolean isComment(String line) {
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
   private static String[] parseLine(String line) {
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

   private static void showOptions(Map<String, AppOptionEnum> enumMap) {
      Logger logger = LoggingUtils.getLogger();
      logger.info("Available options for this program:");
      for(String key : enumMap.keySet()) {
         logger.info(" -> "+key);
      }
   }


   /**
    * @return a map with the options inside this AppOptionFile.
    */
   public Map<String, AppValue> getMap() {
      return null;
   }

   /**
    * Updates the values of this object.
    */
   public void update(Map<String, AppValue> options) {

   }

   /**
    * Saves the contents of this object to a file.
    */
   public void write() {

   }

   /**
    * INSTANCE VARIABLES
    */
   private File optionFile;
   private EntryList entryList;
   // Ordered list, to maintain reading order of entries
   //private List<Entry> entries;
   // Mapping of entries, to quickly locate them
   //private Map<String, Entry> entriesMapping;


   public static final String COMMENT_PREFIX = "//";
   public static final String SPACE = " ";
   public static final String NEWLINE = "\n";

   /*
   class Entry {

      public Entry(List<String> comments, AppOption option) {
         this.comments = comments;
         this.option = option;
      }

      public List<String> getComments() {
         return comments;
      }

      public AppOption getOption() {
         return option;
      }

      private List<String> comments;
      private AppOption option;
   }
    *
    */
}
