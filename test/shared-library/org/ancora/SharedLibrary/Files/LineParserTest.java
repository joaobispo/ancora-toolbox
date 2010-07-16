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

package org.ancora.SharedLibrary.Files;

import java.io.File;
import java.util.List;
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
public class LineParserTest {

    public LineParserTest() {
       commandSeparator = " ";
       commandGatherer = "\"";
       commentPrefix = "//";
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
    * Test of splitCommand method, of class LineParser.
    */
   @Test
   public void testSplitCommand() {
      System.out.println("splitCommand");
      

      // LineParser is immutable
      LineParser instance = new LineParser(commandSeparator, commandGatherer, commentPrefix);

      File inputs = new File("./test/lineParserTestInputs.txt");
      File expected = new File("./test/lineParserTestExpected.txt");

      LineReader inputLines = LineReader.createLineReader(inputs);
      LineReader expectedLines = LineReader.createLineReader(expected);

      String input = null;
      while((input = inputLines.nextLine()) != null) {
         // Parse input
         List<String> arguments = instance.splitCommand(input);

         // Parse expected line
         String expectedLine = expectedLines.nextLine();
         String[] expectedArray = expectedLine.split(";");

         // First position is number of arguments
         String actualSize = Integer.toString(arguments.size());


         // Test number of arguments
         assertEquals(expectedArray[0], actualSize);

         // Test arguments
         for(int i=0; i<arguments.size(); i++) {
            assertEquals(expectedArray[i+1], arguments.get(i));
         }
      }

     // List expResult = null;
      //List result = instance.splitCommand(command);
      //assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      //fail("The test case is a prototype.");
   }

   private String commandSeparator;
   private String commandGatherer;
   private String commentPrefix;
}