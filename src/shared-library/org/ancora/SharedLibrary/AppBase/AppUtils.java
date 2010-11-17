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



import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnumSetup;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.SharedLibrary.AppBase.AppOption.DefaultValues;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;



/**
 * Several utility methods related to the classes inside the 'Extras' package.
 *
 * @author Joao Bispo
 */
public class AppUtils {

   /**
    *
    * @param map a table with values
    * @param option an option
    * @return the string mapped to the given option
    */
   public static String getString(Map<String, AppValue> map, AppOptionEnum option) {
      if(!map.containsKey(option.getName())) {
         Object returnObject = null;
         // Check if it as a default value
         if(ProcessUtils.implementsInterface(option.getClass(), DefaultValues.class)) {
            returnObject = ((DefaultValues)option).getDefaultValue();
         }

         if(returnObject == null) {

            LoggingUtils.getLogger().
                    warning("Could not find a value for option '" + option.getName() + "'. "
                    + "Check if the option is set.");
            return null;
         }

         LoggingUtils.getLogger().
                 info("Using default value '"+returnObject.toString()+"' for "
                 + "option '"+option+"'");
         return returnObject.toString();
      }

      return map.get(option.getName()).get();
   }

   /**
    *
    * @param map a table with values
    * @param option an option
    * @return the boolean mapped to the given option
    */
   public static Boolean getBool(Map<String, AppValue> map, AppOptionEnum option) {
      /*
      if(!map.containsKey(option.getName())) {
      LoggingUtils.getLogger().
      warning("Could not find a value for option '"+option.getName()+"'. "
      + "Check if the option is set.");
      return null;
      }
       *
       */
      String value = getString(map, option);
      if (value == null) {
         return Boolean.FALSE;
      }

//      return Boolean.valueOf(map.get(option.getName()).get());
      return Boolean.valueOf(value);
   }

   /**
    * 
    *
    * @param map a table with values
    * @param option an option
    * @return the boolean mapped to the given option. If integer cannot be
    * parsed, returns null.
    */
   public static Integer getInteger(Map<String, AppValue> map, AppOptionEnum option) {
      /*
      if(!map.containsKey(option.getName())) {
         LoggingUtils.getLogger().
                 warning("Could not find a value for option '"+option.getName()+"'. "
                 + "Check if the option is set.");
         return null;
      }
       *
       */

      //String intString = map.get(option.getName()).get();
      String intString = getString(map, option);
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
    * @param map a table with values
    * @param option an option
    * @return the list of strings mapped to the given option. If the value
    * cannot be parsed, returns null.
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
    *
    * @param map a table with values
    * @param option an option
    * @return the folder mapped to the given option. If the folder does not
    * exist or could not be created, returns null.
    */
   public static File getFolder(Map<String, AppValue> map, AppOptionEnum option) {
      String foldername = getString(map, option);
      File folder = IoUtils.safeFolder(foldername);
      if(folder == null) {
         return null;
      }

      return folder;
   }

   /**
    *
    * @param map a table with values
    * @param option an option
    * @return the folder mapped to the given option. If the folder does not
    * exist, returns null.
    */
   public static File getExistingFolder(Map<String, AppValue> map, AppOptionEnum option) {
      String foldername = getString(map, option);
      File folder = new File(foldername);
      if(!folder.isDirectory()) {
         return null;
      }

      return folder;
   }

   /**
    *
    * @param map a table with values
    * @param option an option
    * @return the file mapped to the given option. If the file does not
    * exist, returns null.
    */
   public static File getExistingFile(Map<String, AppValue> map, AppOptionEnum option) {
      String filename = getString(map, option);
//      File newFile = new File(filename);
      File newFile = IoUtils.existingFile(filename);

      return newFile;
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
//      String s = ((Enum)enumValue).name();
      return s;
   }

   /**
    * Returns the "zero" value for a given type.
    * 
    * <p>Ex.: for integers, it is zero (0); for Strings, it is the empty String;
    * for booleans it is false;
    *
    * @param appOptionType
    * @return zero values for the given AppValueType
    */
   public static String getZeroValue(AppValueType appOptionType) {
      if(appOptionType == AppValueType.bool) {
         return "false";
      }

      if(appOptionType == appOptionType.integer) {
         return "0";
      }

      return "";
   }

   public static String getDefaultValue(AppValueType appOptionType, DefaultValues aDefault) {

      // Get default value
      String defaultValue = null;

      Object defaultValueObject = aDefault.getDefaultValue();
      if (defaultValueObject != null) {
         defaultValue = defaultValueObject.toString();
      }


      if (defaultValue == null) {
         defaultValue = AppUtils.getZeroValue(appOptionType);
      }

      return defaultValue;
   }

   /**
    * Extracts a String and a File objects from a String and returns a list with
    * the elements in the previous order.
    * 
    * @param value
    * @return the first element is the enum name for the setup
    */
   public static List<Object> unpackSetup(String value) {
      int separatorIndex = value.indexOf(SETUP_PACK_SEPARATOR);
      if(separatorIndex == -1) {
         LoggingUtils.getLogger().
                 warning("Malformed setup pack string:"+value);
         return null;
      }

      String enumName = value.substring(0, separatorIndex);
      String filename = value.substring(separatorIndex+1);
      File file = new File(filename);

      List<Object> returnList = new ArrayList<Object>(2);
      returnList.add(enumName);
      returnList.add(file);

      return returnList;
   }


   public static String packSetup(String choiceEnumName, File file) {
      return choiceEnumName+SETUP_PACK_SEPARATOR+file.getPath();
   }

   /**
    * Extracts the AppOptionEnum values from a enum setup class.
    * 
    * @param appOptionEnumSetup
    * @param enumName
    * @param setupFile
    * @return
    */
   public static AppOptionEnum[] parseSetup(Class<Enum> appOptionEnumSetup,
           String enumName, File setupFile) {

      // Get enum from setup class
      Enum anEnum = EnumUtils.valueOf(appOptionEnumSetup, enumName);
      if(anEnum == null) {
         LoggingUtils.getLogger().
                 warning("Could not find name '"+enumName+"' inside enum '"+appOptionEnumSetup+"'");
         LoggingUtils.getLogger().
                 warning("Available names:"+appOptionEnumSetup.getEnumConstants());
         return null;
      }
      
      // Check if it is an AppOptionEnumSetup
      AppOptionEnumSetup setupEnum;
      try {
          setupEnum = (AppOptionEnumSetup) anEnum;
      } catch(ClassCastException e) {
         LoggingUtils.getLogger().
                 warning("Enum '"+anEnum+"' does not implement interface '"+AppOptionEnumSetup.class+"'");
         return null;
      }

      return setupEnum.getSetupOptions();
   }

   public static final String ENUM_NAME_SEPARATOR = ".";
   public static final String SETUP_PACK_SEPARATOR = ";";


}
