/*
 *  Copyright 2010 SPECS Research Group.
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

package org.specs.Scripter;

import org.ancora.SharedLibrary.LoggingUtils;
import java.io.File;
import java.util.Map;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.ancora.SharedLibrary.OptionsTable.OptionsTableFactory;
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
public class ScripterTest {

    public ScripterTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
      LoggingUtils.setupConsoleOnly();
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
    * Test of runShell method, of class Scripter.
    */
    /*
   @Test
   public void testRunShell() {
      System.out.println("runShell");
      File optionsConfig = new File("./test/test.options");
      OptionsTable optionsTable = OptionsTableFactory.fromFile(optionsConfig);
      Map<String, String> programs = SimpleScripter.getBasePrograms();
      LineParser lineParser = LineParser.getDefaultLineParser();

      Scripter instance = new Scripter(programs, lineParser, optionsTable);
      boolean expResult = true;
      boolean result = instance.runShell();
      assertEquals(expResult, result);
   }
     *
     */

   /**
    * Test of runScript method, of class Scripter.
    */
   @Test
   public void testRunScript() {
      System.out.println("runScript");
      File optionsConfig = new File("./test/test.options");
      OptionsTable optionsTable = OptionsTableFactory.fromFile(optionsConfig);
      Map<String, String> programs = SimpleScripter.getBasePrograms();
      LineParser lineParser = LineParser.getDefaultLineParser();

      File script = new File("./test/test.script");
      Scripter instance = new Scripter(programs, lineParser, optionsTable);
      

      boolean expResult = true;
      boolean result = instance.runScript(script);
      assertEquals(expResult, result);

      // Check if state was altered correctly
      String expValue = "value1";
      String curValue = instance.getOptionsTable().get(TestOption.option1);
      assertEquals(expValue, curValue);
   }



   /**
    * Test of getProgram method, of class Scripter.
    */
   @Test
   public void testGetProgram() {
      System.out.println("getProgram");
      Map<String, String> supportedPrograms = SimpleScripter.getBasePrograms();
      String programName = "";
      Program result = null;

      // Give a non-existing program
      programName = "mrmgboogerballs";
      result = Scripter.getProgram(programName, supportedPrograms);
      assertNull(result);

      // Give non-existante class name
      programName = "add";
      supportedPrograms.put(programName, "org.IdontExist");
      result = Scripter.getProgram(programName, supportedPrograms);
      assertNull(result);
      //String programName = "set";
   }

 

}