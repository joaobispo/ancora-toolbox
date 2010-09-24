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
import org.ancora.SharedLibrary.AppBase.AppOptionFile.Utils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Interface between files and AppOptions.
 *
 * @author Joao Bispo
 */
public class AppOptionFile {

   private AppOptionFile(EntryList entryList) {
      this.optionFile = null;
      this.entryList = entryList;
   }

   public void setOptionFile(File optionFile) {
      this.optionFile = optionFile;
   }

   public File getOptionFile() {
      return optionFile;
   }

   


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
       EntryList entryList = Utils.buildEntries(lineReader, enumMap);

       AppOptionFile appOptionFile = new AppOptionFile(entryList);
       appOptionFile.setOptionFile(optionFile);
      
       return appOptionFile;
   }

   public static void writeEmptyFile(File optionFile, Class appOptionEnum) {
      //return false;
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

   public List<Entry> getEntries() {
      return entryList.getEntries();
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


   //public static final String COMMENT_PREFIX = "//";
   //public static final String SPACE = " ";
   //public static final String NEWLINE = "\n";

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
