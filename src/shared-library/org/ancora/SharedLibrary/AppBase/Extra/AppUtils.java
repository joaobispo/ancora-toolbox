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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.AppOptionType;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Parsing.ParsingConstants;



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
      builder.append(CLASS_PREFIX);
      builder.append(c.getName());
      builder.append(ParsingConstants.NEWLINE);

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

         String line = generateAppOptionLine(optionObject);
         builder.append(line);
         builder.append(ParsingConstants.NEWLINE);
      }
      
      return builder.toString();
   }

   /**
    * Parses the given file into a Map of AppOption objects.
    * @return
    */
   
   public static Map<String, AppOption> parseFile(File file) {

      AppOptionParser parser = new AppOptionParser();
      return parser.parse(file);

      //return null;
      /*
      LineReader lineReader = LineReader.createLineReader(file);
      if(lineReader == null) {
         LoggingUtils.getLogger().
                 warning("LineReader is null.");
         return null;
      }
      
      // Build map
      Map<String, AppOption> map = new HashMap<String, AppOption>();
      // Maintain table with lists

      
      String line = null;
      while((line = lineReader.nextLine()) != null) {
         // Parse line
         AppOption appOption = parseAppOptionLine(line);

      }
       
      
      return null;
       *
       */
   }

   /**
    * Builds an unmmodifiable table which maps the name of the AppOptionEnum to
    * the object itself.
    *
    * <p>This table can be useful to get the enum correspondent to a particular
    * option in String format which was collected from, for example, a config file.
    *
    * @param values
    * @return
    */
   public static Map<String, AppOptionEnum> buildMap(AppOptionEnum[] values) {
      Map<String, AppOptionEnum> aMap = new HashMap<String, AppOptionEnum>();

      for (AppOptionEnum enume : values) {
         aMap.put(enume.getName(), enume);
      }

      return Collections.unmodifiableMap(aMap);
   }

   //
   // PRIVATE METHODS
   //

   /*
   private static String getSeparator(AppOptionType appOptionType) {
      if(appOptionType.isList()) {
         return LIST_SEPARATOR;
      } else {
         return SET_SEPARATOR;
      }
   }
    *
    */


   private static String generateAppOptionLine(AppOptionEnum optionObject) {
      StringBuilder builder = new StringBuilder();

      // Add line for enum
      builder.append(optionObject.getName());
      builder.append(ParsingConstants.SPACE);

      builder.append(optionObject.getType().getAttributionString());
      builder.append(ParsingConstants.SPACE);

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
/*
   private static AppOption parseAppOptionLine(String line) {
      int endIndex = line.indexOf(SPACE);
      String appOptionName = line.substring(0, endIndex);
      System.out.println("Name:"+appOptionName);
      // Get type from name

      line = line.substring(endIndex+1);
      line = line.trim();
      endIndex = line.indexOf(SPACE);
      String attribution = line.substring(0, endIndex);
      // Get
   }
*/
   //private static final String SET_SEPARATOR = "=";
   //private static final String LIST_SEPARATOR = "+=";

   public static final String CLASS_PREFIX = "class = ";










}
