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

import org.ancora.SharedLibrary.AppBase.AppOptionType;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Several utility methods related to the classes inside the 'Extras' package.
 *
 * @author Joao Bispo
 */
public class AppUtils {

   /**
    * Given the class of an enum which implements AppOptionEnum, builds a String
    * which represents the contents of a file with the options represented by the
    * enum.
    * 
    * @param c
    * @return null if there was an error
    */
   public static String generateFile(Class c) {
      // Check if class is enum
      if (!c.isEnum()) {
         LoggingUtils.getLogger().
                 warning("Class '" + c.getName() + "' is not an enum.");
         return null;
      }

      // Get enum constants and start building file
      Object[] enums = c.getEnumConstants();
      StringBuilder builder = new StringBuilder();

      // Add a line in the file for each enum
      for (Object option : enums) {
         AppOptionEnum optionObject = null;

         // Check
         try {
            optionObject = (AppOptionEnum) option;
         } catch (ClassCastException ex) {
            LoggingUtils.getLogger().
                    warning("Class '" + c.getName() + "' does not implement interface " +
                    AppOptionEnum.class + ".");
            return null;
         }

         String line = generateLine(optionObject);
         builder.append(line);
         builder.append(NEWLINE);
      }
      
      return builder.toString();
   }

   private static String getSeparator(AppOptionType appOptionType) {
      if(appOptionType.isList()) {
         return LIST_SEPARATOR;
      } else {
         return SET_SEPARATOR;
      }
   }


   private static String generateLine(AppOptionEnum optionObject) {
      StringBuilder builder = new StringBuilder();

      // Add line for enum
      builder.append(optionObject.getName());
      builder.append(SPACE);

      String separator = getSeparator(optionObject.getType());
      builder.append(separator);
      builder.append(SPACE);

      String defaultValue = getDefaultValue(optionObject.getType());
      builder.append(defaultValue);

      return builder.toString();
   }

   private static String getDefaultValue(AppOptionType appOptionType) {
      if(appOptionType == AppOptionType.bool) {
         return "false";
      }

      if(appOptionType == appOptionType.integer) {
         return "0";
      }

      return "";
   }


   private static final String SET_SEPARATOR = "=";
   private static final String LIST_SEPARATOR = "+=";
   private static final String SPACE = " ";
   private static final String NEWLINE = "\n";








}
