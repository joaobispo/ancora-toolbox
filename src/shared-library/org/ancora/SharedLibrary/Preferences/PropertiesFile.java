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

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.Interfaces.EnumKey;

/**
 * A class that extends this abstract class can define what contents a 
 * Properties file should have, under the form of Section objects, which define 
 * the comments and keys of the Properties file. 
 * 
 * <p>This class is used by EnumPreferences to implement support for 
 * loading/saving properties files.
 *
 * @author Joao Bispo
 */
public abstract class PropertiesFile {


   /**
    * @return the filename of the Properties file.
    */
   public abstract String getPropertiesFilename();

   /**
    * Builds the sections which will define the properties files.
    *
    * <p>An implementation of this method should be composed by consecutive
    * calls to protected methods addSection().
    */
   public abstract void buildSections();

   /**
    *
    * @param keyName the name of the desired EnumKey.
    * @return the EnumKey with the specified name. The string must match exactly
    * an identifier used to declare the EnumKey constant in this type.
    * (Extraneous whitespace characters are not permitted.)
    */
   public abstract EnumKey valueOf(String keyName);

   /**
    * @return a list with Section objects, which define the properties file.
    */
   public List<Section> getSections() {
      return sections;
   }

   /**
    * Indicates the autosave status.
    *
    * <p>When autosave is enabled, any modifications in the EnumPreferences are
    * immediately reflected in the Properties file.
    *
    * @return true if autosave is enabled. False otherwise.
    */
   public boolean isAutoSaveEnabled() {
      return autosaveStatus;
   }

   /**
    * Sets the status of autosave. By default, it is true.
    *
    * @param status status of Autosave.
    */
   public void setAutoSave(boolean status) {
      autosaveStatus = status;
   }


   ///
   // CONSTRUCTORS
   ///

   /**
    * Enables autosave by default. Initializes sections list with an initial
    * capacity of ten.
    */
   public PropertiesFile() {
      this(10);
   }

   /**
    * Enables autosave by default. Initializes sections list with the given
    * capacity.
    */
   public PropertiesFile(int capacity) {
      autosaveStatus = true;
      sections = new ArrayList<Section>(capacity);
      buildSections();
   }

   /**
    * Helper method for adding a new section to a list.
    *
    * @param comment the string that will be commented and appear just before
    * the parameter
    * @param propertyName an EnumKey, representing a field of the Properties
    * file
    */
   protected void addSection(String comment, EnumKey propertyName) {
      Section section = new Section(comment, propertyName);
      sections.add(section);
   }

   /**
    * Helper method for adding a new section to a list.
    *
    * @param comment the string that will be commented and appear just before
    * the parameter
    */
   protected void addSection(String comment) {
      Section section = new Section(comment);
      sections.add(section);
   }


    ///
    // INSTANCE VARIABLES
    ///
   private boolean autosaveStatus;
   private List<Section> sections;

   // Definitions for Section
   public static String PROPERTIES_COMMENT_PREFIX = "#";
   private static String LINE_SEPARATOR = System.getProperty("line.separator");
   private static String PROPERTY_SEPARATOR = " = ";

   /**
    * Class which stores a String[] and a EnumKey. The String[] represents
    * comments, which should appear before the EnumKey.
    */
   public class Section {

      public Section(String comment, EnumKey key) {
         this.comment = comment;
         this.key = key;
      }

      public Section(String comment) {
         this.comment = comment;
         this.key = null;
      }

      public String getComment() {
         return comment;
      }

      public EnumKey getKey() {
         return key;
      }

      /**
       * Returns the text of the properties file for this section
       *
       * @param value if this section contains a field, the value of that field.
       * @return the content of this section
       */
      public String toString(String value) {
         int capacity = 200;
         StringBuilder builder = new StringBuilder(capacity);

         // Process comment
         // If empty, put an empty line
         if (comment.length() == 0) {
            builder.append(LINE_SEPARATOR);
         } else {
            // Otherwise, put the line with a comment prefix
            builder.append(PROPERTIES_COMMENT_PREFIX);
            builder.append(comment);
            builder.append(LINE_SEPARATOR);
         }


         // Check if property is not null and process it.
         if (key != null) {
            builder.append(key.getKey());
            builder.append(PROPERTY_SEPARATOR);
            builder.append(value);
            builder.append(LINE_SEPARATOR);
         }

         return builder.toString();
      }

      /**
       * @return the text of the properties file for this section, using
       * default values.
       */
      @Override
      public String toString() {
         if(key == null) {
            return toString(null);
         }
         else {
            return toString(key.getDefaultValue());
         }
      }


      ///
      // INSTANCE VARIABLES
      ///
      private final String comment;
      private final EnumKey key;
   }
}
