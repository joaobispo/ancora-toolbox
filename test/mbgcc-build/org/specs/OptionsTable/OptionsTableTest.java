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

package org.specs.OptionsTable;

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
    * Test of newOptionsTable method, of class OptionsTable.
    */
   @Test
   public void testNewOptionsTable() {
      System.out.println("newOptionsTable");
      File enumOptions = null;
      OptionsTable expResult = null;
      OptionsTable result = OptionsTable.newOptionsTable(enumOptions);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of get method, of class OptionsTable.
    */
   @Test
   public void testGet() {
      System.out.println("get");
      OptionName key = null;
      OptionsTable instance = null;
      String expResult = "";
      String result = instance.get(key);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getList method, of class OptionsTable.
    */
   @Test
   public void testGetList() {
      System.out.println("getList");
      OptionName key = null;
      OptionsTable instance = null;
      List expResult = null;
      List result = instance.getList(key);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of set method, of class OptionsTable.
    */
   @Test
   public void testSet() {
      System.out.println("set");
      OptionName key = null;
      String value = "";
      OptionsTable instance = null;
      instance.set(key, value);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of add method, of class OptionsTable.
    */
   @Test
   public void testAdd() {
      System.out.println("add");
      OptionName key = null;
      String value = "";
      OptionsTable instance = null;
      instance.add(key, value);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getAvaliableOptions method, of class OptionsTable.
    */
   @Test
   public void testGetAvaliableOptions() {
      System.out.println("getAvaliableOptions");
      OptionsTable instance = null;
      List expResult = null;
      List result = instance.getAvaliableOptions();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getOption method, of class OptionsTable.
    */
   @Test
   public void testGetOption() {
      System.out.println("getOption");
      String optionName = "";
      OptionsTable instance = null;
      OptionName expResult = null;
      OptionName result = instance.getOption(optionName);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getListSeparator method, of class OptionsTable.
    */
   @Test
   public void testGetListSeparator() {
      System.out.println("getListSeparator");
      OptionsTable instance = null;
      String expResult = "";
      String result = instance.getListSeparator();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}