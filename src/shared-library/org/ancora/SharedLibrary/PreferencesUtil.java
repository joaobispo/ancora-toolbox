/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.SharedLibrary;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Interfaces.EnumKey;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;
import org.ancora.SharedLibrary.Preferences.PropertiesFile;
import org.ancora.SharedLibrary.Preferences.PropertiesFile.Section;

/**
 * Methods for the Java Preferences API.
 *
 * @author Joao Bispo
 */
public class PreferencesUtil {

   /**
    * If an object  PropertiesFile is associated to the object EnumPreferences,
    * builds a String with represents the contents of a Properties file.
    * Otherwise, returns an empty String and logs the cause.
    *
    * <p>The properties file is built from the data in the EnumPreferences
    * object and PropertiesFile object.
    *
    * @param preferences the EnumPreferences from which to generate the contents
    * of the Properties file.
    * @return a String representing the content of the Properties file.
    */
   public static String generateProperties(EnumPreferences preferences) {
      // Get Properties File
      PropertiesFile propertiesDef = preferences.getPropertiesFile();

      // Check if PropertiesFile exists
      if(propertiesDef == null) {
         Logger.getLogger(PreferencesUtil.class.getName()).
                 warning("There is no PropertiesFile associated with input 'preferences'.");
         return "";
      }

      int builderCapacity = 1000;
      StringBuilder builder = new StringBuilder(builderCapacity);

      // Get Sections
      List<Section> sections = propertiesDef.getSections();

      for(Section section : sections) {
         // For each section, check if there is a key
         EnumKey key = section.getKey();
         String sectionContent;

         if(key == null) {
            // If there is no key, just print the text
            sectionContent = section.toString();
         } else {
            // If there is a key, get the value in Preferences
            String value = preferences.getPreference(section.getKey());
            sectionContent = section.toString(value);
         }

         builder.append(sectionContent);
      }

      return builder.toString();
   }

   /**
    * If a PropertiesFile object is associated to the EnumPreferences object,
    * loads the contents of the corresponding Properties file into the
    * Preferences.
    * 
    * @param preferences the EnumPreferences to where the values from the
    * Properties file will be loaded.
    * @return true if load was successful. False otherwise.
    */
   public static boolean loadProperties(EnumPreferences preferences) {
      // Get the Properties object
      PropertiesFile propertiesFile = preferences.getPropertiesFile();

      // Check if PropertiesFile exists
      if (propertiesFile == null) {
         Logger.getLogger(PreferencesUtil.class.getName()).
                 warning("There is no PropertiesFile associated with input 'preferences'.");
         return false;
      }

      String propertiesFilename = propertiesFile.getPropertiesFilename();
      File file = IoUtils.existingFile(propertiesFilename);
      Properties properties = IoUtils.loadProperties(file);

      if(properties == null) {
         return false;
      }

      // Before storing values, save autosave state
      boolean autosaveState = propertiesFile.isAutoSaveEnabled();
      // Disable autosave
      propertiesFile.setAutoSave(false);

      // Get keys
      Set<String> propertyKeys = properties.stringPropertyNames();
      for(String key : propertyKeys) {
         // Check if key exists in PropertiesDefinition
         EnumKey enumKey = propertiesFile.valueOf(key);
         if(enumKey == null) {
            Logger.getLogger(PreferencesUtil.class.getName()).
                    warning("Key '"+key+"' in properties '"+propertiesFilename+"' " +
                    "doesn't exist in the program definitions.");
         } else {
            // Get the value
            String value = properties.getProperty(key);
            // Store it in the preferences
            preferences.putPreference(enumKey, value);
         }
      }

      // Restore autosave state
      propertiesFile.setAutoSave(autosaveState);

      return true;
   }

   /**
    * If a PropertiesFile object is associated to the EnumPreferences object,
    * saves the content of the Preferences to a Properties file.
    *
    * @param preferences the EnumPreferences with the values we want to save in
    * a Properties file.
    * @return true if save was successful. False otherwise.
    */
   public static boolean saveProperties(EnumPreferences preferences) {
      // Create a new properties file.
      String propertiesContents = PreferencesUtil.generateProperties(preferences);

      // Save file
      PropertiesFile propertiesDef = preferences.getPropertiesFile();
      String propFilename = propertiesDef.getPropertiesFilename();
      File propFile = new File(propFilename);


      return IoUtils.write(propFile, propertiesContents);

   }
}
