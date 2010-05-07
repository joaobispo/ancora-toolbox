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

import java.util.logging.Logger;

/**
 * Utility methods for parsing of values which, instead of throwing an
 * exception, return a default value if a parsing error occurs.
 * 
 * @author Joao Bispo
 */
public class ParseUtils {

   /**
    * Tries to parse a String into a integer. If an exception happens, warns the
    * user and returns a 0.
    *
    * @param integer a String representing an integer.
    * @return the intenger represented by the string, or 0 if it couldn't be parsed.
    */
   public static int parseInt(String integer) {
      int intResult = 0;
      try {
         intResult = Integer.parseInt(integer);
      } catch (NumberFormatException e) {
         Logger.getLogger(ParseUtils.class.getName()).
                 warning("Couldn't parse '"+integer+"' into an integer. Returning "+intResult+".");
      }

      return intResult;
   }

   /**
    * Tries to parse a String into a long. If an exception happens, warns the
    * user and returns a 0.
    *
    * @param longNumber a String representing an long.
    * @return the long represented by the string, or 0L if it couldn't be parsed.
    */
   public static long parseLong(String longNumber) {
      long longResult = 0L;
      try {
         longResult = Long.parseLong(longNumber);
      } catch (NumberFormatException e) {
         Logger.getLogger(ParseUtils.class.getName()).
                 warning("Couldn't parse '"+longNumber+"' into an long. Returning "+longResult+".");
      }

      return longResult;
   }

   /**
    * Removes, from String text, the portion of text after the rightmost
    * occurrence of the specified separator.
    *
    * <p>Ex.: removeSuffix("readme.txt", ".")
    * <br> Returns "readme".
    * @param text a string
    * @param separator a string
    * @return a string
    */
   public static String removeSuffix(String text, String separator) {
      int index = text.lastIndexOf(separator);

      if(index == -1) {
         return text;
      }

      return text.substring(0, index);
   }

   /**
    * Transforms the given long in an hexadecimal string with the specified
    * size.
    *
    * <p>Ex.: toHexString(10, 2)
    * <br> Returns 0x0A.
    *
    * @param decimalLong a long
    * @param stringSize the final number of digits in the hexadecimal
    * representation
    * @return a string
    */
   public static String toHexString(long decimalLong, int stringSize) {
      String longString = Long.toHexString(decimalLong);
      longString = BitUtils.padHexString(longString, stringSize);

      return longString;
   }

   /**
    * @param string a string
    * @return the index of the first whitespace found in the given String, or
    * -1 if none is found.
    */
   public static int indexOfFirstWhiteSpace(String string) {
      int index = -1;

      for(int i=0; i<string.length(); i++) {
         if(Character.isWhitespace(string.charAt(i))) {
            return i;
         }
      }

      return index;
   }

}
