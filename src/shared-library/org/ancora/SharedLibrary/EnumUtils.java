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

}
