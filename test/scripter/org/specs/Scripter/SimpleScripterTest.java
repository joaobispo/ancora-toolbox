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

import java.util.HashMap;
import org.ancora.SharedLibrary.Utilities.LineParser;
import org.ancora.SharedLibrary.OptionsTable.OptionsTableFactory;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import java.io.File;
import org.ancora.SharedLibrary.LoggingUtils;
import java.util.Map;
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
public class SimpleScripterTest {

    public SimpleScripterTest() {
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
    * Test of start method, of class SimpleScripter.
    */
   @Test
   public void testStart() {
      System.out.println("start");
      String[] args = {"./test/test.script"};
      File optionsConfig = new File("./test/test.options");
      OptionsTable optionsTable = OptionsTableFactory.fromFile(optionsConfig);
      Map<String, String> programs = new HashMap<String, String>();
      LineParser lineParser = LineParser.getDefaultLineParser();

      SimpleScripter instance = new SimpleScripter(programs, lineParser, optionsTable);
      boolean expResult = true;
      boolean result = instance.start(args);
      assertEquals(expResult, result);
   }



}