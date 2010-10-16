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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.Utilities.LineParser;
import org.ancora.SharedLibrary.Utilities.LineReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ParseUtilsTest {

    public ParseUtilsTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of parseInt method, of class ParseUtils.
    */
   @Test
   public void testParseInt() {
      System.out.println("parseInt");
      String integer = "1";
      int expResult = 1;
      int result = ParseUtils.parseInt(integer);
      assertEquals(expResult, result);

      integer = "bogus int";
      expResult = 0;
      result = ParseUtils.parseInt(integer);
      assertEquals(expResult, result);
   }

   /**
    * Test of parseLong method, of class ParseUtils.
    */
   @Test
   public void testParseLong() {
      System.out.println("parseLong");
      String longNumber = "1000";
      long expResult = 1000L;
      long result = ParseUtils.parseLong(longNumber);
      assertEquals(expResult, result);

      longNumber = "bogus long";
      expResult = 0L;
      result = ParseUtils.parseLong(longNumber);
      assertEquals(expResult, result);
   }

   /**
    * Test of removeSuffix method, of class ParseUtils.
    */
   @Test
   public void testRemoveSuffix() {
      System.out.println("removeSuffix");
      String text = "readme.txt";
      String separator = ".";
      String expResult = "readme";
      String result = ParseUtils.removeSuffix(text, separator);
      assertEquals(expResult, result);

      // Boundaries
      text = "readme.";
      separator = ".";
      expResult = "readme";
      result = ParseUtils.removeSuffix(text, separator);
      assertEquals(expResult, result);

      text = ".readme";
      separator = ".";
      expResult = "";
      result = ParseUtils.removeSuffix(text, separator);
      assertEquals(expResult, result);

      // No occurences of separator
      text = "readme";
      separator = ".";
      expResult = "readme";
      result = ParseUtils.removeSuffix(text, separator);
      assertEquals(expResult, result);

      // Several occurences of separator
      text = ".readme.";
      separator = ".";
      expResult = ".readme";
      result = ParseUtils.removeSuffix(text, separator);
      assertEquals(expResult, result);
   }

   /**
    * Test of toHexString method, of class ParseUtils.
    */
   @Test
   public void testToHexString() {
      System.out.println("toHexString");
      long decimalLong = 10L;
      int stringSize = 2;
      String expResult = "0x0A";
      String result = ParseUtils.toHexString(decimalLong, stringSize);
      assertEquals(expResult, result);
   }

   /**
    * Test of indexOfFirstWhiteSpace method, of class ParseUtils.
    */
   @Test
   public void testIndexOfFirstWhiteSpace() {
      System.out.println("indexOfFirstWhiteSpace");
      String string = "0123 56";
      int expResult = 4;
      int result = ParseUtils.indexOfFirstWhiteSpace(string);
      assertEquals(expResult, result);

      // Boundaries
      string = " 123";
      expResult = 0;
      result = ParseUtils.indexOfFirstWhiteSpace(string);
      assertEquals(expResult, result);

      string = "012 ";
      expResult = 3;
      result = ParseUtils.indexOfFirstWhiteSpace(string);
      assertEquals(expResult, result);

      // No occurences
      string = "0123";
      expResult = -1;
      result = ParseUtils.indexOfFirstWhiteSpace(string);
      assertEquals(expResult, result);

      // Several occurences of separator
      string = "01 3 56";
      expResult = 2;
      result = ParseUtils.indexOfFirstWhiteSpace(string);
      assertEquals(expResult, result);
   }

   /**
    * Test of padRight method, of class ParseUtils.
    */
   @Test
   public void testPadRight() {
      System.out.println("padRight");
      String string = "abc";
      int length = 4;
      String expResult = "abc ";
      String result = ParseUtils.padRight(string, length);
      assertEquals(expResult, result);

      // Lenght is less than original string
      string = "abc";
      length = 2;
      expResult = "abc";
      result = ParseUtils.padRight(string, length);
      assertEquals(expResult, result);

      // Lenght is equal than original string
      string = "abc";
      length = 3;
      expResult = "abc";
      result = ParseUtils.padRight(string, length);
      assertEquals(expResult, result);
   }

   /**
    * Test of padLeft method, of class ParseUtils.
    */
   @Test
   public void testPadLeft_String_int() {
      System.out.println("padLeft");
      String string = "abc";
      int length = 4;
      String expResult = " abc";
      String result = ParseUtils.padLeft(string, length);
      assertEquals(expResult, result);

      // Lenght is less than original string
      string = "abc";
      length = 2;
      expResult = "abc";
      result = ParseUtils.padLeft(string, length);
      assertEquals(expResult, result);

      // Lenght is equal than original string
      string = "abc";
      length = 3;
      expResult = "abc";
      result = ParseUtils.padLeft(string, length);
      assertEquals(expResult, result);
   }

   /**
    * Test of padLeft method, of class ParseUtils.
    */
   @Test
   public void testPadLeft_3args() {
      System.out.println("padLeft");
      String string = "abc";
      int length = 4;
      char c = '%';
      String expResult = "%abc";
      String result = ParseUtils.padLeft(string, length, c);
      assertEquals(expResult, result);

      // Lenght is less than original string
      string = "abc";
      length = 2;
      c = '%';
      expResult = "abc";
      result = ParseUtils.padLeft(string, length, c);
      assertEquals(expResult, result);

      // Lenght is equal than original string
      string = "abc";
      length = 3;
      c = '%';
      expResult = "abc";
      result = ParseUtils.padLeft(string, length, c);
      assertEquals(expResult, result);
   }

   /**
    * Test of getSortedList method, of class ParseUtils.
    */
   @Test
   public void testGetSortedList() {
      System.out.println("getSortedList");

      // Output List
      List expResult = new ArrayList<String>();
      expResult.add("a");
      expResult.add("b");
      expResult.add("c");

      // Try a Set
      Collection<String> collection = new HashSet<String>();
      collection.add("b");
      collection.add("c");
      collection.add("a");
      
      List result = ParseUtils.getSortedList(collection);
      assertEquals(expResult, result);

   }

   /**
    * Test of parseTableFromFile method, of class ParseUtils.
    */
   @Test
   public void testParseTableFromFile() {
      System.out.println("parseTableFromFile");
      
      File tableFile = new File("./test/parseTable.table");
      LineParser lineParser = new LineParser(" ", "\"", "//");
      ParseUtils instance = new ParseUtils();
      Map expResult = loadTestMap(new File("./test/parseTableExpected.txt"));
      Map result = instance.parseTableFromFile(tableFile, lineParser);
      assertEquals(expResult, result);

   }

   private Map<String, String> loadTestMap(File file) {
      Map<String, String> map = new HashMap<String, String>();
      String splitChar = ";";

      LineReader lineReader = LineReader.createLineReader(file);
      String line = null;
      while ((line = lineReader.nextLine()) != null) {
         String[] args = line.split(splitChar);
         if (args.length > 1) {
            map.put(args[0], args[1]);
         } else {
            map.put(args[0], "");
         }
      }
      return map;
   }
}