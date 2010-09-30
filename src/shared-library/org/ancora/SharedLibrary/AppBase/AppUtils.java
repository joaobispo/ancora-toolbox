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



import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.SharedLibrary.LoggingUtils;



/**
 * Several utility methods related to the classes inside the 'Extras' package.
 *
 * @author Joao Bispo
 */
public class AppUtils {

   public static String getString(Map<String, AppValue> map, AppOptionEnum option) {
      if(!map.containsKey(option.getName())) {
         LoggingUtils.getLogger().
                 warning("Could not find a value for option '"+option.getName()+"'. "
                 + "Check if the option is set.");
         return null;
      }

      return map.get(option.getName()).get();
   }

   public static Boolean getBool(Map<String, AppValue> map, AppOptionEnum option) {
      if(!map.containsKey(option.getName())) {
         LoggingUtils.getLogger().
                 warning("Could not find a value for option '"+option.getName()+"'. "
                 + "Check if the option is set.");
         return null;
      }

      return Boolean.valueOf(map.get(option.getName()).get());
   }

   /**
    * If integer could not be parsed, returns null.
    *
    * @param map
    * @param option
    * @return
    */
   public static Integer getInteger(Map<String, AppValue> map, AppOptionEnum option) {
      if(!map.containsKey(option.getName())) {
         LoggingUtils.getLogger().
                 warning("Could not find a value for option '"+option.getName()+"'. "
                 + "Check if the option is set.");
         return null;
      }

      String intString = map.get(option.getName()).get();
      Integer value = null;
      try {
         value = Integer.valueOf(intString);
      } catch(NumberFormatException ex) {
         LoggingUtils.getLogger().
                 warning("Could not parse '"+intString+"' to int, for option '"+option.getName()+"'.");
      }

      return value;
   }

   /**
    * Given a map and an AppOptionEnum, returns the corresponding list.
    *
    * @param map
    * @param option
    * @return
    */
   public static List<String> getStringList(Map<String, AppValue> map, AppOptionEnum option) {
      AppValue value = map.get(option.getName());
      if (value == null) {
         LoggingUtils.getLogger().
                 warning("Could not find a list for option '" + option.getName() + "'. "
                 + "Check if the option is set.");
         //LoggingUtils.getLogger().
         //        warning("Option '"+option.getName()+"' not found in the given map.");
         return null;
      }

      return value.getList();
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


   public static AppOptionEnum[] getEnumValues(Class appOptionEnum) {
       // Check class
      if (!appOptionEnum.isEnum()) {
         LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not represent an enum.");
         return null;
      }

      // Build set with interfaces of the given class
      Set<Class> interfaces = new HashSet<Class>(Arrays.asList(appOptionEnum.getInterfaces()));
      Class appOption = AppOptionEnum.class;
      if(!interfaces.contains(appOption)){
           LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not implement "
                 + "interface '"+appOption+"'.");
         return null;
      }

      // Get map of enums
       return (AppOptionEnum[]) appOptionEnum.getEnumConstants();
   }

   /**
    * Reads the elements in an enum class implementing the AppOptionEnum interface,
    * and returns a mapping of the name of the options to the enum objects.
    * @param appOptionEnum
    * @return
    */
   public static Map<String, AppOptionEnum> getEnumMap(Class appOptionEnum) {
      /*
      // Check class
      if (!appOptionEnum.isEnum()) {
         LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not represent an enum.");
         return null;
      }

      // Build set with interfaces of the given class
      Set<Class> interfaces = new HashSet<Class>(Arrays.asList(appOptionEnum.getInterfaces()));
      Class appOption = AppOptionEnum.class;
      if(!interfaces.contains(appOption)){
           LoggingUtils.getLogger().
                 warning("Class '" + appOptionEnum.getName() + "' does not implement "
                 + "interface '"+appOption+"'.");
         return null;
      }
*/

      // Get map of enums
       //Map<String, AppOptionEnum> enumMap = AppUtils.buildMap((AppOptionEnum[]) appOptionEnum.getEnumConstants());
       Map<String, AppOptionEnum> enumMap = AppUtils.buildMap(getEnumValues(appOptionEnum));

       return enumMap;
   }

   /**
    * Locates the index of the last ENUM_NAME_SEPARATOR and returns everything
    * after it.
    *
    * @param enumValue
    * @return
    */
   public static String parseEnumName(AppOptionEnum enumValue) {
      return ((Enum)enumValue).name();
   }

   /**
    * Locates the index of the last ENUM_NAME_SEPARATOR and returns everything
    * after it.
    *
    * @param name
    * @return
    */
   /*
   public static String parseEnumName(String name) {
      int index = name.lastIndexOf(ENUM_NAME_SEPARATOR);
      if(index == -1) {
         return name;
      }

      if(index == name.length()-1) {
         return name;
      }

      return name.substring(index+1, name.length());
   }
    * 
    */

   public static String buildEnumName(AppOptionEnum enumValue) {
      String s = enumValue.getClass().getSimpleName() + ENUM_NAME_SEPARATOR + ((Enum)enumValue).name();
      return s;
   }

   public static final String ENUM_NAME_SEPARATOR = ".";
}
