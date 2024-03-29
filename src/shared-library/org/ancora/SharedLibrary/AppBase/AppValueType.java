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

package org.ancora.SharedLibrary.AppBase;

/**
 * Enumeration of the supported types for the options.
 *
 * @author Joao Bispo
 */
public enum AppValueType {
   /**
    * A string
    */
   string("="),
   /**
    * A boolean
    */
   bool("="),
   /**
    * An integer
    */
   integer("="),
   /**
    * A list of custom strings
    */
   stringList("+="),
   /**
    * A list of predefined strings, from which we can choose only one.
    */
   multipleChoice("="),
   /**
    * A list of predefined strings, from which we can choose multiple strings.
    * <p>To use this option, AppOptionEnum must implement interface AppOptionMultipleChoice.
    */
   multipleChoiceStringList("+="),
   /**
    * One complete setup. Value stores the path to a file in AppOptionFile format.
    * <p>To use this option, AppOptionEnum must implement interface AppOptionEnumSetup.
    */
   multipleSetup("="),
   /**
    * A list of pre-defined setups. There can be several instances of the same
    * type of setup.
    */
   multipleSetupList("+=");
   

   private AppValueType(String attribution) {
      this.attribution = attribution;
   }


   public boolean isList() {
      if (this == stringList 
              || this == multipleChoiceStringList
              || this == multipleSetupList) {
         return true;
      } else {
         return false;
      }
   }

   public String getAttributionString() {
      return attribution;
   }


   /**
    * Parses the given value into an AppOption. If the value could not be
    * parsed or there is another error, returns null.
    * 
    * @param value
    * @return
    */
   /*
   public AppValue parseValue(String value) {
      if(isList()) {
         LoggingUtils.getLogger().
                 warning("This method is not available for list-type options.");
         return null;
      }

      if(this == string) {
         return new AppValue(value);
      }

      if(this == bool) {
         return new AppValue(Boolean.parseBoolean(value));
      }

      if(this == integer) {
         // Parse int
         try {
            int number = Integer.parseInt(value);
            return new AppValue(number);
         } catch(NumberFormatException ex) {
            LoggingUtils.getLogger().
                    warning("Could not parse integer '"+value+"'");
            return null;
         }
      }

      LoggingUtils.getLogger().
              warning("Type '"+this+"' not supported.");
      return null;
   }
*/
   
   final private String attribution;

}
