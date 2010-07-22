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

import java.util.Collections;
import java.util.ArrayList;
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
public class OptionsTableTest {

    public OptionsTableTest() {
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
    * Test of get method, of class OptionsTable.
    */
   @Test
   public void testGet() {
      System.out.println("get");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionName key = TestOption.option1;
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);
      String expResult = key.getDefaultValue();


      String result = instance.get(key);
      assertEquals(expResult, result);
   }

   /**
    * Test of getList method, of class OptionsTable.
    */
   @Test
   public void testGetList() {
      System.out.println("getList");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionName key = TestOption.list;
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);
      
      List expResult = new ArrayList<String>();
      expResult.add("element1");
      expResult.add("element2");

      List result = instance.getList(key);
      assertEquals(expResult, result);
   }

   /**
    * Test of set method, of class OptionsTable.
    */
   @Test
   public void testSet() {
      System.out.println("set");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionName key = TestOption.option1;
      String value = "newValue";
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);
      instance.set(key, value);

      // Check if value is the same
      assertEquals(value, instance.get(key));
   }

   /**
    * Test of add method, of class OptionsTable.
    */
   @Test
   public void testAdd() {
      System.out.println("add");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionName key = TestOption.option1;
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);

      // An add operation overides the default value. If there was no value before,
      // the add will be the first element of the list


      String[] values = {"value1", "value2"};
      for(String value : values) {
         instance.add(key, value);
      }

      List<String> list = instance.getList(key);
      assertEquals(values.length, list.size());
      for(int i=0; i<values.length; i++) {
         assertEquals(values[i], list.get(i));
      }

   }

   /**
    * Test of getAvaliableOptions method, of class OptionsTable.
    */
   @Test
   public void testGetAvaliableOptions() {
      System.out.println("getAvaliableOptions");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);

      List<String> expResult = new ArrayList<String>();
      expResult.addAll(getOptionNameStrings(TestOption.values()));
      expResult.addAll(getOptionNameStrings(ProgramOption.values()));
      Collections.sort(expResult);
 
      List<OptionName> result = instance.getAvaliableOptions();
      List<String> stringResult = new ArrayList<String>();
      for(OptionName option : result) {
         stringResult.add(option.getOptionName());
      }

      Collections.sort(stringResult);

    assertEquals(expResult, stringResult);

   }

   /**
    * Test of getOption method, of class OptionsTable.
    */
   @Test
   public void testGetOption() {
      System.out.println("getOption");
      OptionName option = TestOption.list;
      String optionName = option.getOptionName();

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);

      OptionName expResult = option;
      OptionName result = instance.getOption(optionName);
      assertEquals(expResult, result);

   }

   /**
    * Test of getListSeparator method, of class OptionsTable.
    */
   @Test
   public void testGetListSeparator() {
      System.out.println("getListSeparator");

      File optionsConfig = new File("./test/testOptionsTable.table");
      OptionsTable instance = OptionsTableFactory.fromFile(optionsConfig);
      String expResult = TestOption.testListSeparator();
      String result = instance.getListSeparator();
      assertEquals(expResult, result);
   }


   private List<String> getOptionNameStrings(OptionName[] values) {
      List<String> names = new ArrayList<String>();
      for(int i=0; i<values.length; i++) {
         names.add(values[i].getOptionName());
      }
      return names;
   }
}