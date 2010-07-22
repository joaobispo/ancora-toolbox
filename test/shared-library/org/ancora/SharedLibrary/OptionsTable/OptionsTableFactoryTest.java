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

package org.ancora.SharedLibrary.OptionsTable;

import org.ancora.SharedLibrary.LoggingUtils;
import java.util.Map;
import java.io.File;
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
public class OptionsTableFactoryTest {

    public OptionsTableFactoryTest() {
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
    * Test of fromFile method, of class OptionsTableFactory.
    */
   @Test
   public void testFromFile() {
      System.out.println("fromFile");
      File optionsConfig = null;
      OptionsTable result = null;

      // Check if table returns default values for the loaded options
      optionsConfig = new File("./test/mbgccOptions.table");
      result = OptionsTableFactory.fromFile(optionsConfig);
      testDefaultValues(result, TestOption.values());
      testDefaultValues(result, ProgramOption.values());

      // Check when duplicated entries
      optionsConfig = new File("./test/duplicatedEntries.table");
      result = OptionsTableFactory.fromFile(optionsConfig);
      testDefaultValues(result, TestOption.values());

      // Check when duplicated entries
      optionsConfig = new File("./test/unsupportedKey.table");
      result = OptionsTableFactory.fromFile(optionsConfig);
      testDefaultValues(result, TestOption.values());


   }

   @Test
   public void testGetOptionNamesFromClass() {
      System.out.println("getOptionNamesFromClass");
      String enumClassName = null;

      // Class does not exist
      enumClassName = "mcboogerballs1434242";
      assertNull(OptionsTableFactory.getOptionNamesFromClass(enumClassName));

      // Class which does not implement enum
      enumClassName = "org.specs.OptionsTable.OptionsTableFactoryTest";
      assertNull(OptionsTableFactory.getOptionNamesFromClass(enumClassName));

      // Class implements enum but does not implement OptionName
      enumClassName = "org.specs.OptionsTable.OneFieldEnum";
      assertNull(OptionsTableFactory.getOptionNamesFromClass(enumClassName));

      // Enum is empty
      enumClassName = "org.specs.OptionsTable.EmptyEnum";
      Map<String, OptionName> result = OptionsTableFactory.getOptionNamesFromClass(enumClassName);
      assertEquals(0, result.size());

      // Malformed Enum name (first letter lower case)
      enumClassName = "org.specs.OptionsTable.emptyEnum";
      assertNull(OptionsTableFactory.getOptionNamesFromClass(enumClassName));
   }


   private void testDefaultValues(OptionsTable result, OptionName[] values) {
      for(int i=0; i<values.length; i++) {
         assertEquals(values[i].getDefaultValue(), result.get(values[i]));
      }
   }


}