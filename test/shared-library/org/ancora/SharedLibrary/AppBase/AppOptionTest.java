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

package org.ancora.SharedLibrary.AppBase;

import org.ancora.SharedLibrary.LoggingUtils;
import java.util.List;
import java.util.logging.Level;
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
public class AppOptionTest {

    public AppOptionTest() {
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
    * Test of newAppOption method, of class AppOption.
    */
   @Test
   public void testNewAppOption() {
      System.out.println("newAppOption");
      AppValueType type;
      //AppOption expResult;
      AppValue result;

      type = AppValueType.bool;
      result = AppValue.newAppOption(type);
      assertEquals(Boolean.FALSE.toString(), result.get());

      type = AppValueType.stringList;
      result = AppValue.newAppOption(type);
      
      assertEquals(0, result.getList().size());

      //type = AppOptionType.stringList;
      //result = AppOption.newAppOption(type);


      //assertEquals(expResult, result);
   }

   /**
    * Test of set method, of class AppOption.
    */
   /*
   @Test
   public void testSet() {
      System.out.println("set");
      String value = "";
      AppOption instance = null;
      instance.set(value);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of get method, of class AppOption.
    */
   /*
   @Test
   public void testGet() {
      System.out.println("get");
      AppOption instance = null;
      String expResult = "";
      String result = instance.get();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of getList method, of class AppOption.
    */
   /*
   @Test
   public void testGetList() {
      System.out.println("getList");
      AppOption instance = null;
      List expResult = null;
      List result = instance.getList();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of setList method, of class AppOption.
    */
   /*
   @Test
   public void testSetList() {
      System.out.println("setList");
      List<String> list = null;
      AppOption instance = null;
      instance.setList(list);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    * 
    */

}