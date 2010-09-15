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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads lines from a File, one by one.
 *
 * @author Joao Bispo
 */
public class LineReader {

   /**
    * Private constructor for static creator method.
    *
    * @param reader
    */
    private LineReader(BufferedReader reader) {
       this.reader = reader;
       this.currentLine = 0;
    }

    /**
    * Builds a LineReader from the given file. If the object could
    * not be created, returns null.
    *
    * <p>Creating a LineReader involves operations which can lead
    * to failure in creation of the object. That is why a public
    * static method is used instead of a constructor.
    *
    * @return a LineReader If the object could not be created, returns null.
     */
   public static LineReader createLineReader(File file) {
      FileInputStream stream = null;
      try {
         stream = new FileInputStream(file);
         return createLineReader(stream);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(LineReader.class.getName()).
                 log(Level.WARNING, "FileNotFoundException: " + ex.getMessage());
      }

      return null;
   }

   /**
    * Builds a LineReader from the given InputStream. If the object could
    * not be created, returns null.
    *
    * <p>Creating a LineReader involves operations which can lead
    * to failure in creation of the object. That is why a public
    * static method is used instead of a constructor.
    *
    * @return a LineReader If the object could not be created, returns null.
    */
   //public static LineReader createLineReader(File file) {
   public static LineReader createLineReader(InputStream inputStream) {

      //FileInputStream stream = null;
      InputStreamReader streamReader = null;

        try {
            // Try to read the contents of the file into the StringBuilder
            streamReader = new InputStreamReader(inputStream, DEFAULT_CHAR_SET);
            BufferedReader newReader = new BufferedReader(streamReader);
            return new LineReader(newReader);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LineReader.class.getName()).
                    log(Level.WARNING, "UnsupportedEncodingException: "+ ex.getMessage());
        }

      return null;
   }

   public int getLastLineIndex() {
      return currentLine;
   }



    /**
     * @return the next line in the file, or
     * null if the end of the stream has been reached.
     */
    public String nextLine() {
           try {
               // Read next line
              String line = reader.readLine();
              if(line != null) {
                 this.currentLine++;
              }
              return line;
           } catch (IOException ex) {
              Logger.getLogger(LineReader.class.getName()).
                      log(Level.WARNING, "IOException: "+ ex.getMessage());
              return null;
           }
    }

    /**
     * @return the next line which is not empty, or
     * null if the end of the stream has been reached.
     */
    public String nextNonEmptyLine() {
       boolean foundAnswer = false;
       while(!foundAnswer) {
         String line = nextLine();

         if(line == null) {
            return line;
         }

         if(line.length() > 0) {
            return line;
         }
         
       }


       return null;
    }

   /**
    * INSTANCE VARIABLES
    */
    private final BufferedReader reader;
    private int currentLine;

   /**
    * Default CharSet used in file operations.
    */
   public static final String DEFAULT_CHAR_SET = "UTF-8";



}
