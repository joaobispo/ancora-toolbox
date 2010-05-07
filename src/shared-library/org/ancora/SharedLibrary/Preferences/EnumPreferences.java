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

package org.ancora.SharedLibrary.Preferences;

import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.ancora.SharedLibrary.Interfaces.EnumKey;
import org.ancora.SharedLibrary.PreferencesUtil;

/**
 * Wrapper for Preferences class, which uses EnumKey instead of Strings to
 * access its values, and provides additional properties such as saving to a
 * user-defined Properties file, through a PropertiesDefinition object.
 * 
 * @author Joao Bispo
 */
public class EnumPreferences {

   /**
    * Builds a EnumPreferences. If local is true, fetches a UserNode for package
    * of class c. If local is false, fetches a SystemNode for package of class c.
    *
    * @param c Preferences of package of Class c will be fetched.
    * @param local
    */
   public EnumPreferences(Class<?> c, boolean local) {
      if(local) {
         preferences = Preferences.userNodeForPackage(c);
      } else {
         preferences = Preferences.systemNodeForPackage(c);
      }

      this.propertiesDef = null;
      hasProperties = false;
      //initializeProperties(propertiesDef);
   }

   /**
    * Adds a PropertiesFile to the object, so Preferences are backed up by a
    * Properties file.
    *
    * <p>A PropertiesFile can influence the behavior of a Preferences object:
    *
    *<p> - when adding PropertiesFile, if the Properties file indicated by the
    * object exists, the values from the Properties file are automatically
    * loaded into the Preferences; otherwise, a new Properties file is created
    * with the current values of Preferences.
    *
    *<p> - if the autosave option is enabled in the PropertiesFile, everytime
    * there is a change in a Preferences field, the change is immediately
    * reflected on the corresponding field of the properties file.
    *
    * @param propertiesFile the PropertiesFile object to add to this
    * EnumPreferences
    */
   public void addProperties(PropertiesFile propertiesFile) {
      if(propertiesFile == null) {
          Logger.getLogger(EnumPreferences.class.getName()).
                 warning("Input 'propertiesDefinition' is null.");
          return;
      }

      // Check if there wasn't already a PropertiesDefinition associated with
      // PreferencesEnum
      if(hasProperties) {
         String oldFile = propertiesDef.getPropertiesFilename();
         String newFile = propertiesFile.getPropertiesFilename();
         Logger.getLogger(EnumPreferences.class.getName()).
                 warning("EnumPreferences already associated with file '"+oldFile+"'. " +
                 "Associating with new file '"+newFile+"'");
      }

      propertiesDef = propertiesFile;
      hasProperties = true;

      // Properties filename
      String propertiesFilename = propertiesFile.getPropertiesFilename();
      File properties = new File(propertiesFilename);

      // Check if the Properties file exists
      if (!properties.isFile()) {
         Logger.getLogger(EnumPreferences.class.getName()).
                 info("Properties file '"+propertiesFilename+"' doesn't exist. Creating a new one...");
          PreferencesUtil.saveProperties(this);
      } else {
         // Load properties values into preferences
         boolean couldLoad = PreferencesUtil.loadProperties(this);
         String prefix;
         if(!couldLoad) {
            prefix = "Couldn't load";
         } else {
            prefix = "Loaded";
         }

         Logger.getLogger(EnumPreferences.class.getName()).
                 info(prefix + " values from file '" + propertiesDef.getPropertiesFilename() + "'.");
      }
   }

   /**
    * @return the PropertiesFile object associated to this EnumPreferences.
    * If none is associated, null is returned.
    */
   public PropertiesFile getPropertiesFile() {
      return propertiesDef;
   }


    /**
    * @param key key whose associated value is to be returned.
    * @return the value associated with the specified key in this preference
    * node. If there is no value associated with the specified key,
    * the default value defined in EnumKey is returned.
    */
   public String getPreference(EnumKey key) {
      return preferences.get(key.getKey(), key.getDefaultValue());
   }


   /**
    * @param key key whose associated value is to be returned.
    * @return the value associated with the specified key in this preference 
    * node. If there is no value associated with the specified key, 
    * null is returned.
    */
   public String getPreferenceReal(EnumKey key) {
      return preferences.get(key.getKey(), null);
   }
   
   /**
    * Associates the specified value with the specified key in this preference node.
    *
    * <p>If there is a PropertiesFile associated with this EnumPreferences
    * and the PropertiesFile has the autoSave option enabled, everytime this 
    * method is called, the changes are reflected in the Properties file.
    *
    * @param key key with which the specified value is to be associated.
    * @param value value to be associated with the specified key.
    */
   public void putPreference(EnumKey key, String value) {
      preferences.put(key.getKey(), value);

      if(hasProperties) {
         if(propertiesDef.isAutoSaveEnabled()) {
            PreferencesUtil.saveProperties(this);
         }
      }
   }


   /**
    * If a PropertiesFile object is associated with this EnumPreferences,
    * updates the Properties file with the current values. 
    *
    * @return true if the file could be successfully written.
    */
   public boolean saveProperties() {
      if (!hasProperties) {
         Logger.getLogger(EnumPreferences.class.getName()).
                 info("There is no PropertiesFile class associated! Can't save properties.");
         return false;
      }

      return PreferencesUtil.saveProperties(this);
   }

    ///
    // INSTANCE VARIABLES
    ///
   private final Preferences preferences;
   private PropertiesFile propertiesDef;
   private boolean hasProperties;

}
