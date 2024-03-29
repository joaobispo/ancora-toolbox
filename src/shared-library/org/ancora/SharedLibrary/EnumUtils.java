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

package org.ancora.SharedLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods for Enumeration manipulation.
 *
 * @author Joao Bispo
 */
public class EnumUtils {

   /**
    * Transforms a String into a constant of the same name in a specific Enum.
    * Returns null instead of throwing exceptions.
    *
    * @param <T> The Enum where the constant is
    * @param enumType the Class object of the enum type from which to return a
    * constant
    * @param name the name of the constant to return
    * @return the constant of enum with the same name, or null if not found.
    */
   public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
      try{
         return Enum.valueOf(enumType, name);
      } catch (IllegalArgumentException ex) {
         return null;
      } catch (NullPointerException ex) {
         return null;
      }
   }

   /**
    * Checks if a specific enum contains a constant with the given name.
    *
    * @param <T> The Enum where the constant is
    * @param enumType the Class object of the enum type from which to return a
    * constant
    * @param name the name of the constant to return
    * @return true if the Enum contains a constant with the same name, false
    * otherwise
    */
   public static <T extends Enum<T>> boolean containsEnum(Class<T> enumType, String name) {
      Enum enumeration = valueOf(enumType, name);
      if (enumeration == null) {
         return false;
      } else {
         return true;
      }
   }

   /**
    * Builds an unmmodifiable table which maps the name of the enum to the
    * enum itself.
    *
    * <p>This table can be useful to get the enum correspondent to a particular
    * option in String format which was collected from, for example, a config file.
    *
    * @param <K>
    * @param values
    * @return
    */
   public static <K extends Enum> Map<String, K> buildMap(K[] values) {
      Map<String, K> aMap = new HashMap<String, K>();

      for (K enume : values) {
         aMap.put(enume.toString(), enume);
      }

      return Collections.unmodifiableMap(aMap);
   }

   /**
    *
    * @param <K>
    * @param values
    * @return a list with the names of the enums
    */
   public static <K extends Enum> List<String> buildList(K[] values) {
      List<String> aList = new ArrayList<String>();
      for (K anEnum : values) {
         aList.add(anEnum.name());
      }

      return aList;
   }

   /**
    *
    * @param <K>
    * @param values
    * @return a list with the string representation of the enums
    */
   public static <K extends Enum> List<String> buildListToString(K[] values) {
      List<String> aList = new ArrayList<String>();
      for (K anEnum : values) {
         aList.add(anEnum.toString());
      }

      return aList;
   }

   /**
    * Returns the class of the enum correspondent to the values of the given
    * array.
    *
    * @param <K>
    * @param values
    * @return the class correspondent to the given array of enums
    */
   public static <K extends Enum> Class getClass(K[] values) {
      if(values.length == 0) {
         LoggingUtils.getLogger().
                 warning("Given array is empty");
         return null;
      }

      return values[0].getClass();
   }

   /**
    * Returns the class of the enum correspondent to the values of the given
    * list.
    *
    * @param <K>
    * @param values
    * @return the class correspondent to the given array of enums
    */
   /*
   public static <K extends Enum> Class getClass(List<K> values) {
      if(values.isEmpty()) {
         LoggingUtils.getLogger().
                 warning("Given list is empty.");
         return null;
      }

      return values.get(0).getClass();
   }
    * 
    */

}
