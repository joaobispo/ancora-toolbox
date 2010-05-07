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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Methods for quick and simple manipulation of files, folders and other
 * input/output related operations.
 *
 * @author Joao Bispo
 */
public class IoUtils {

   /**
    * Given a string representing a filepath to a folder, returns a File object
    * representing the folder.
    *
    * <p>If the folder doesn’t exist, the method will try to create the folder
    * and necessary sub-folders. If an error occurs (ex.: the folder could
    * not be created, the given path does not represent a folder), returns null
    * and logs the cause.
    *
    * <p>If an object different than null is returned it is guaranteed that the
    * folder exists.
    *
    * @param folderpath String representing a folder.
    * @return a File object representing a folder, or null if unsuccessful.
    */
   public static File safeFolder(String folderpath) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (folderpath == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'folderpath' is null.");
         return null;
      }

      // Create File object
      final File folder = new File(folderpath);


      // The following checks where done in that sequence to avoid having
      // more than one level of if-nesting.

      // Check if File is a folder
      final boolean isFolder = folder.isDirectory();
      if (isFolder) {
         return folder;
      }

      // Check if File exists. If true, is not a folder.
      final boolean folderExists = folder.exists();
      if (folderExists) {
         Logger.getLogger(IoUtils.class.getName()).warning("Path '" + folderpath + "' exists, but " +
                 "doesn't represent a folder.");
         return null;
      }

      // Try to create folder.
      final boolean folderCreated = folder.mkdirs();
      if (folderCreated) {
         Logger.getLogger(IoUtils.class.getName()).
                 info("Folder created (" + folder.getAbsolutePath() + ").");
         return folder;

      } else {
         // Couldn't create folder
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Path '" + folderpath + "' doesn't exist and " +
                 "couldn't be created.");
         return null;
      }
   }

   /**
    * Method to create a File object for a file which should exist.
    *
    * <p>The method does some common checks (ex.: if the file given by filepath
    * exists, if it is a file). If any of the checks fail, returns null and
    * logs the cause.
    *
    * @param filepath String representing an existing file.
    * @return a File object representing a file, or null if unsuccessful.
    */
   public static File existingFile(String filepath) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (filepath == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'filepath' is null.");
         return null;
      }

      // Create File object
      final File file = new File(filepath);


      // Check if File is a file
      final boolean isFile = file.isFile();
      if (isFile) {
         return file;
      }

      // Check if File exists. If true, is not a file.
      final boolean fileExists = file.exists();
      if (fileExists) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Path '" + filepath + "' exists, but doesn't " +
                 "represent a file.");
         return null;
      } else {
         // File doesn't exist, return null.
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Path '" + filepath + "' does not exist.");
         return null;

      }

   }

   /**
    * Given a File object, returns a String with the contents of the file.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file)
    * returns an empty string and logs the cause.
    *
    * @param file a File object representing a file.
    * @return a String with the contents of the file.
    */
   public static String read(File file) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return "";
      }

      StringBuilder stringBuilder = new StringBuilder();

      FileInputStream stream = null;
      InputStreamReader streamReader = null;

      // Try to read the contents of the file into the StringBuilder
      try {
         stream = new FileInputStream(file);
         streamReader = new InputStreamReader(stream, DEFAULT_CHAR_SET);
         final BufferedReader bufferedReader = new BufferedReader(streamReader);

         // Read first character. It can't be cast to "char", otherwise the
         // -1 will be converted in a character.
         // First test for -1, then cast.
         int intChar = bufferedReader.read();
         while (intChar != -1) {
            char character = (char) intChar;
            stringBuilder.append(character);
            intChar = bufferedReader.read();
         }

         // Read file. Close StreamReader
         streamReader.close();

      } catch (FileNotFoundException ex) {
         /*
         Logger.getLogger(IoUtils.class.getName()).
         warning("FileNotFoundException while trying to read " +
         "file '" + file.getAbsolutePath() + "'");
          */
         Logger.getLogger(IoUtils.class.getName()).
                 warning("FileNotFoundException: " + ex.getMessage());
         stringBuilder = new StringBuilder(0);

      } catch (IOException ex) {
         /*
         Logger.getLogger(IoUtils.class.getName()).
         warning("IOException while trying to read " +
         "file '" + file.getAbsolutePath() + "'");
          */
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
         stringBuilder = new StringBuilder(0);
      }

      if (stringBuilder.length() == 0) {
         Logger.getLogger(IoUtils.class.getName()).
                 info("Read 0 characters from file '" + file.getAbsolutePath() + "'.");
      }


      return stringBuilder.toString();
   }

   /**
    * Given a File object and a String, writes the contents of the String in the
    * file, overwriting everything that was previously in that file.
    *
    * <p>If successful, returns true. If an error occurs (ex.: the File argument
    * does not represent a file) returns false, logs the cause and nothing is
    * written.
    *
    * @param file a File object representing a file.
    * @param contents a String with the content to write
    * @return true if write is successful. False otherwise.
    */
   public static boolean write(File file, String contents) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return false;
      }

      if (contents == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'contents' is null.");
         return false;
      }

      return writeAppendHelper(file, contents, false);
   }

   /**
    * Given a File object and a String, writes the contents of the String at the
    * end of the file. If successful, returns true.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file)
    * returns false, logs the cause and nothing is written.
    *
    * @param file a File object representing a file.
    * @param contents a String with the content to write
    * @return true if write is successful. False otherwise.
    */
   public static boolean append(File file, String contents) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return false;
      }

      if (contents == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'contents' is null.");
         return false;
      }

      return writeAppendHelper(file, contents, true);
   }

   /**
    * Method shared among write and append.
    *
    * @param file
    * @param contents
    * @param append
    * @return
    */
   private static boolean writeAppendHelper(File file, String contents, boolean append) {

      FileOutputStream stream = null;
      OutputStreamWriter streamWriter = null;
      try {
         stream = new FileOutputStream(file, append);
         streamWriter = new OutputStreamWriter(stream, DEFAULT_CHAR_SET);
         final BufferedWriter writer = new BufferedWriter(streamWriter);
         writer.write(contents, 0, contents.length());
         writer.close();
         // Inform about the operation
         if (append) {
            // Check if this is the same file as the last time
            String filePath = file.getAbsolutePath();
            if(!filePath.equals(lastAppeddedFileAbsolutePath)) {
               lastAppeddedFileAbsolutePath = filePath;
               Logger.getLogger(IoUtils.class.getName()).
                       info("File appended (" + file.getAbsolutePath() + ").");
            }
         } else {
            Logger.getLogger(IoUtils.class.getName()).
                    info("File written (" + file.getAbsolutePath() + ").");
         }

      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
         return false;
      }

      return true;
   }

   /**
    * Given a File object, loads the contents of the file into a Java Properties
    * object.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file,
    * could not load the Properties object) returns null and logs the cause.
    * 
    * @param file a File object representing a file.
    * @return If successfull, a Properties objects with the contents of the
    * file. Null otherwise.
    */
   public static Properties loadProperties(File file) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return null;
      }


      try {
         Properties props = new Properties();
         props.load(new java.io.FileReader(file));
         return props;
      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
      }

      return null;
   }

   
   //
   //DEFINITIONS
   //
   /**
    * Default CharSet used in file operations.
    */
   final public static String DEFAULT_CHAR_SET = "UTF-8";

   // Records the name of the last file appended
   private static String lastAppeddedFileAbsolutePath = "";
}
