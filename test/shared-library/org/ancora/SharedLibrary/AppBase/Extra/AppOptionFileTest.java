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

package org.ancora.SharedLibrary.AppBase.Extra;

import org.ancora.SharedLibrary.AppBase.AppOptionFile.Entry;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import java.util.HashMap;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.OptionFileUtils;
import java.io.File;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppValue;
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
public class AppOptionFileTest {

    public AppOptionFileTest() {
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
    * Test of setOptionFile method, of class AppOptionFile.
    */
    /*
   @Test
   public void testSetOptionFile() {
      System.out.println("setOptionFile");
      File optionFile = null;
      AppOptionFile instance = null;
      instance.setOptionFile(optionFile);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of getOptionFile method, of class AppOptionFile.
    */
    /*
   @Test
   public void testGetOptionFile() {
      System.out.println("getOptionFile");
      AppOptionFile instance = null;
      File expResult = null;
      File result = instance.getOptionFile();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
     *
     */

   /**
    * Test of parseFile method, of class AppOptionFile.
    */
    
   //@Test
   public void testParseFile() {
      System.out.println("parseFile - not a test");
      File optionFile = new File("./test/AppBase/sample.option");
      Class appOptionEnum = SampleOption.class;
      //AppOptionFile expResult = null;
      AppOptionFile result = AppOptionFile.parseFile(optionFile, appOptionEnum);
      for(Entry entry : result.getEntries()) {
         System.out.print(OptionFileUtils.toString(entry));
      }

      //assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      //fail("The test case is a prototype.");
   }

   //@Test
   public void testWriteEmptyFile() {
      File file = new File("./test/AppBase/empty.option");
      Class appOptionEnum = SampleOption.class;

      AppOptionFile.writeEmptyFile(file, appOptionEnum);
   }

   //@Test
   public void testWriteFile() {
      File file = new File("./test/AppBase/sample.option");
      Class appOptionEnum = SampleOption.class;

      AppOptionFile appFile = AppOptionFile.parseFile(file, appOptionEnum);
      appFile.setOptionFile(new File("./test/AppBase/sample.option.copy"));
      appFile.write();
   }

   @Test
   public void testUpdate() {
      File file = new File("./test/AppBase/sample.option");
      Class appOptionEnum = SampleOption.class;

      Map<String, AppValue> otherMap = new HashMap<String, AppValue>();
      otherMap.put(SampleOption.compiler.getName(), new AppValue("experimentalCompiler"));
      otherMap.put(SampleOption.target.getName(), new AppValue("experimentalTarget"));

      AppOptionFile appFile = AppOptionFile.parseFile(file, appOptionEnum);
      appFile.update(otherMap);
      appFile.setOptionFile(new File("./test/AppBase/sample.option.updated"));
      appFile.write();
   }

   @Test
   public void testDummy() {
      
   }

   /**
    * Test of getMap method, of class AppOptionFile.
    */
   /*
   @Test
   public void testGetMap() {
      System.out.println("getMap");
      AppOptionFile instance = null;
      Map expResult = null;
      Map result = instance.getMap();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of update method, of class AppOptionFile.
    */
   /*
   @Test
   public void testUpdate() {
      System.out.println("update");
      Map<String, AppValue> options = null;
      AppOptionFile instance = null;
      instance.update(options);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

   /**
    * Test of write method, of class AppOptionFile.
    */
   /*
   @Test
   public void testWrite() {
      System.out.println("write");
      AppOptionFile instance = null;
      instance.write();
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
    *
    */

}