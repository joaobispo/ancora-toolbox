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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.ancora.SharedLibrary.Identification.ByteIdentifier;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
       init();
       //testSafeFolder();
       //testExistingFile();
       //testRead();
       //testWrite();
       //testLoadProperties();
       testByteIdentifier();

       //testUrl();
    }

   private static void testSafeFolder() {
      String s;
      s = "testFiles/empty.txt";
      
      System.out.println("Result: " +IoUtils.safeFolder(s));

   }

   private static void testExistingFile() {
      String file;
      file = "testFiles/nonexistant";

      IoUtils.existingFile(file);
   }

   private static void testUrl() throws MalformedURLException, IOException {
      String urlString = "http://www.google.pt";
      URL url = new URL(urlString);
      InputStream in = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String aLine = "";
      while ((aLine = br.readLine()) != null) {
         //do something with aLines
         System.out.println(aLine);
      }
   }

   private static void testRead() {
      String folderpath = "testFiles";
      File folder = IoUtils.safeFolder(folderpath);


      String filename;
      //filename = "doesnotexist";
      filename = "empty.txt";
      //filename = "threeChars.txt";
      //filename = "twoEmptyLines.txt";

      
      File file = new File(folder.getPath()+File.separator+filename);
      String contents = IoUtils.read(file);

      System.out.println("Number of chars:"+contents.length());
      System.out.println(contents);
      //IoUtils.read(file);
   }

   private static void testWrite() {
      String filename;
      filename = "newFile.txt";

      File file = new File(filename);

      System.out.println("Result:"+IoUtils.write(file, "Hello"));
   }

   private static void testLoadProperties() {
      String filename;
      //filename = "testFiles/empty.txt";
      filename = "nonexistant";

      System.out.println("Result:"+IoUtils.loadProperties(new File(filename)));
   }

   private static void init() {
      LoggingUtils.setupConsoleOnly();
   }

   private static void testByteIdentifier() {
      ByteIdentifier id = new ByteIdentifier();

      int cycles = 513;
      for(int i=0; i<cycles; i++) {
         System.out.println(id.newByte());
      }
   }

}
