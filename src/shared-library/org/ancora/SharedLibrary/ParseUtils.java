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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.Files.LineReader;

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
                 log(Level.WARNING, "Couldn''t parse '"+integer+"' into an integer. Returning "+intResult+".");
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
                 log(Level.WARNING, "Couldn''t parse '"+longNumber+"' into an long. Returning "+longResult+".");
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

   /**
    * Adds spaces to the end of the given string until it has the desired size.
    * @param string a string
    * @param length the size we want the string to be
    * @return the string, with the desired size
    */
   public static String padRight(String string, int length) {
      return String.format("%1$-" + length + "s", string);
   }

   /**
    * Adds spaces to the beginning of the given string until it has the
    * desired size.
    *
    * @param string a string
    * @param length length the size we want the string to be
    * @return the string, with the desired size
    */
   public static String padLeft(String string, int length) {
      return String.format("%1$#" + length + "s", string);
   }

   /**
    * Adds an arbitrary character to the beginning of the given string until it has the
    * desired size.
    * 
    * @param string a string
    * @param length length the size we want the string to be
    * @return the string, with the desired size
    */
   public static String padLeft(String string, int length, char c) {
      if(string.length() >= length) {
         return string;
      }

      String returnString = string;
      int missingChars = length - string.length();
      for(int i=0; i<missingChars; i++) {
         returnString = c + returnString;
      }

      return returnString;
   }

   public static <T> List<T> getSortedList(Collection<T> collection) {
      List<T> list = new ArrayList<T>(collection);
      Collections.sort((List)list);
      return list;
   }


   /**
    * Reads a Table file and returns a table with the key-value pairs.
    *
    * <p> Any line with one or more parameters, as defined by the object LineParser
    * is put in the table. The first parameters is used as the key, and
    * the second as the value.
    * <br>If a line has more than two parameters, they are
    * ignored.
    * <br>If a line has only a single parameters, the second parameters is assumed
    * to be an empty string.
    *
    * @param tableFile
    * @param lineParser
    * @return a table with key-value pairs.
    */
   public Map<String, String> parseTableFromFile(File tableFile, LineParser lineParser) {
    LineReader lineReader = LineReader.createLineReader(tableFile);

      String line;
      Map<String, String> table = new HashMap<String, String>();

      while((line = lineReader.nextLine()) != null) {
         List<String> arguments = lineParser.splitCommand(line);
         
         if(arguments.isEmpty()) {
            continue;
         }
         
         String key = null;
         String value = null;

         if(arguments.size() > 1) {
            key = arguments.get(0);
         }

         if(arguments.size() > 2) {
            value = arguments.get(1);
         } else {
            value = "";
         }

         table.put(key, value);
      }

      return table;
   }
}
