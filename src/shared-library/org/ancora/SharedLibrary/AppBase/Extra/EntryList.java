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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Represents a list of entries for the AppOptionFile.
 *
 * <p> Lists are handled specially. After the first value is added,
 * there cannot be spaces or other lines between other declared values of the list.
 *
 * @author Joao Bispo
 */
public class EntryList {

   public EntryList() {
      entries = new ArrayList<Entry>();
      entriesMapping = new HashMap<String, Entry>();
   }

   public List<Entry> getEntries() {
      return entries;
   }

   public Entry getEntry(String optionName) {
      return entriesMapping.get(optionName);
   }


/**
 *  Adds a new entry to the list.
 * @param entries
 * @param optionName
 * @param optionValue
 * @param optionEnum
 * @param comments
 */
   public void addEntry(String optionName, String optionValue, AppOptionEnum optionEnum, List<String> comments) {
     // Create AppValue
      AppValue appValue = AppValue.newAppOption(optionEnum.getType());
      if(optionEnum.getType().isList()) {
         //System.out.println("List:"+appValue.getList());
         //System.out.println("Option Value:"+optionValue);
         appValue.getList().add(optionValue);
      } else {
         appValue.set(optionValue);
      }


      Entry newEntry = new Entry(comments, optionName, appValue);
      // Add new entry to map and to list
      addNewEntry(newEntry);
      /*
      // Check if entry is list
      if(optionEnum.getType().isList()) {
         addListEntry(optionName, optionValue, optionEnum, comments);
         return;
      }

      addRegularEntry(optionName, optionValue, optionEnum, comments);
       *
       */
   }

   /*
   private void addListEntry(String optionName, String optionValue, AppOptionEnum optionEnum, List<String> comments) {


      if(optionEnum.getType() != AppValueType.stringList) {
         LoggingUtils.getLogger().
                 warning("Case not defined: '"+optionEnum.getType()+"'.");
         return;
      }

      AppValue appValue = AppValue.newAppOption(AppValueType.stringList);
      appValue.getList().add(optionValue);

      Entry newEntry = new Entry(comments, optionName, appValue);
      // Add new entry to map and to list
      addNewEntry(newEntry);
   }
    *
    */


   private void addNewEntry(Entry newEntry) {
      String newEntryOptionName = newEntry.getOptionName();
      // Check if entry is "duplicated"
      Entry duplicatedEntry = entriesMapping.get(newEntryOptionName);
      
      if (duplicatedEntry != null) {
         handleDuplicatedEntry(newEntry, duplicatedEntry);
         return;
      }

      // Add new entry
      entries.add(newEntry);
      entriesMapping.put(newEntryOptionName, newEntry);
   }


   private void handleDuplicatedEntry(Entry newEntry, Entry duplicatedEntry) {
      String newEntryOptionName = newEntry.getOptionName();

      // Check if this entry is a list
      if (!newEntry.getOptionValue().getType().isList()) {
         LoggingUtils.getLogger().
                 warning("Ignored duplicated entry for '" + newEntryOptionName + "'.");
         return;
      }

      String listValue = newEntry.getOptionValue().getList().get(0);

      // Check if previous entry is a list
      Entry previousEntry = entries.get(entries.size() - 1);
      if (!previousEntry.getOptionName().equals(newEntryOptionName)) {
         LoggingUtils.getLogger().
                 warning("Addition to list '" + newEntryOptionName + "' with value '" + listValue + "' far from previous addition. "
                 + "Additions must be next to each other. Any non-options lines between "
                 + "this entry and the previous will be deleted if this file is saved.");
         return;
      }

      // Just add value to list
      duplicatedEntry.getOptionValue().getList().add(listValue);
   }

   /**
    * Updates a single entry.
    *
    * @param optionName
    * @param value
    */
   public void updateEntry(String optionName, AppValue value) {
      Entry entry = entriesMapping.get(optionName);
      if(entry == null) {
         LoggingUtils.getLogger().
                 warning("Could not find option '"+optionName+"'.");
         return;
      }

      entry.getOptionValue().set(value);
   }

   /**
    * Updates a number of entries.
    *
    * @param optionValues
    */
   public void updateEntries(Map<String, AppValue> optionValues) {
      for(String optionName : optionValues.keySet()) {
         updateEntry(optionName, optionValues.get(optionName));
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("List:");

      return builder.toString();
   }


   // Ordered list, to maintain reading order of entries
   private List<Entry> entries;
   // Mapping of entries, to quickly locate them
   private Map<String, Entry> entriesMapping;




   /*
   class Entry {

     
   }
    *
    */
}
