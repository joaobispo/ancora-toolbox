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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
//         Logger.getLogger(IoUtils.class.getName()).log(Level.WARNING,"Path ''{0}" + "'' exists, but " +
//                 "doesn''t represent a folder.", folderpath);
         Logger.getLogger(IoUtils.class.getName()).log(Level.WARNING,"Path '"+folderpath+"' exists, but " +
                 "doesn''t represent a folder.");
         return null;
      }

      // Try to create folder.
      final boolean folderCreated = folder.mkdirs();
      if (folderCreated) {
         try{
         Logger.getLogger(IoUtils.class.getName()).
                 //log(Level.INFO, "Folder created ("+folder.getAbsolutePath()+").");
                 log(Level.INFO, "Folder created ("+folder.getCanonicalPath()+").");
         } catch(IOException ex) {
          Logger.getLogger(IoUtils.class.getName()).
                 log(Level.INFO, "Folder created ("+folder.getAbsolutePath()+").");
         }
         return folder;

      } else {
         // Couldn't create folder
         Logger.getLogger(IoUtils.class.getName()).
                 log(Level.WARNING,"Path '" + folderpath+"' does not exist and " +
                 "couldn''t be created.");
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
                 log(Level.WARNING,"Path '" +filepath+ "' exists, but doesn''t " +
                 "represent a file.");
         return null;
      } else {
         // File doesn't exist, return null.
         Logger.getLogger(IoUtils.class.getName()).
                 log(Level.WARNING, "Path '"+filepath+"' does not exist.");
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
                 log(Level.WARNING, "FileNotFoundException: "+ex.getMessage() );
         stringBuilder = new StringBuilder(0);

      } catch (IOException ex) {
         /*
         Logger.getLogger(IoUtils.class.getName()).
         warning("IOException while trying to read " +
         "file '" + file.getAbsolutePath() + "'");
          */
         Logger.getLogger(IoUtils.class.getName()).
                 log(Level.WARNING, "IOException: "+ ex.getMessage());
         stringBuilder = new StringBuilder(0);
      }

      if (stringBuilder.length() == 0) {
         try {
            Logger.getLogger(IoUtils.class.getName()).
                    //log(Level.INFO, "Read 0 characters from file '"+file.getAbsolutePath()+"'.");
                    log(Level.INFO, "Read 0 characters from file '" + file.getCanonicalPath() + "'.");
         } catch (IOException ex) {
            Logger.getLogger(IoUtils.class.getName()).
                    log(Level.INFO, "Read 0 characters from file '" + file.getAbsolutePath() + "'.");
         }
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
      // Get parent
      String parentName = file.getParent();
      if (parentName == null) {
         LoggingUtils.getLogger().
                 warning("Could not get parent of file '" + file.getPath() + "'.");
         return false;
      }

      // Check if folders for file exist
      File folder = new File(file.getParent());
      if (!folder.exists()) {
         folder.mkdirs();
      }
      

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
            //String filePath = file.getAbsolutePath();
            String filePath = file.getCanonicalPath();
            if(!filePath.equals(lastAppeddedFileCanonicalPath)) {
               lastAppeddedFileCanonicalPath = filePath;
               Logger.getLogger(IoUtils.class.getName()).
                       //log(Level.INFO, "File appended ("+file.getAbsolutePath()+").");
                       log(Level.INFO, "Appending file ("+file.getCanonicalPath()+").");
            }
         } else {
            Logger.getLogger(IoUtils.class.getName()).
                    log(Level.INFO, "File written ("+file.getCanonicalPath()+").");
         }

      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 log(Level.WARNING, "IOException: " + ex.getMessage());
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
                 log(Level.WARNING, "IOException: "+ ex.getMessage());
      }

      return null;
   }

   /**
    * Given a filename, removes the extension suffix and the separator.
    *
    * <p>Example:
    * <br>filename: 'readme.txt'
    * <br>separator: '.'
    * <br>result: 'readme'
    *
    * @param filename a string
    * @param separator the extension separator
    * @return the name of the file without the extension and the separator
    */
      public static String removeExtension(String filename, String separator) {
      int extIndex = filename.lastIndexOf(separator);
      return filename.substring(0, extIndex);
   }

   /**
    * Given a filename, removes the extension suffix and the separator.
    * 
    * <p>Uses '.' as default separator 
    *
    * <p>Example:
    * <br>filename: 'readme.txt'
    * <br>result: 'readme'
    *
    * @param filename a string
    * @return the name of the file without the extension and the separator
    */
   public static String removeExtension(String filename) {
      return removeExtension(filename, DEFAULT_EXTENSION_SEPARATOR);
   }

      /**
       * @param folder a File representing a folder.
       * @param extensions a set of strings
       * @return all the files inside the given folder, excluding other folders,
       * that have a certain extension as determined by the set.
       */
      public static List<File> getFilesRecursive(File folder, Set<String> extensions) {
      List<File> fileList = new ArrayList<File>();

      for(String extension : extensions) {
         fileList.addAll(getFilesRecursive(folder, extension));
      }

      return fileList;
   }

      /**
       * @param folder a File representing a folder.
       * @param extension a string
       * @return all the files inside the given folder, excluding other folders,
       * that have a certain extension.
       */
      public static List<File> getFilesRecursive(File folder, String extension) {
      List<File> fileList = new ArrayList<File>();
      File[] files = folder.listFiles(new ExtensionFilter(extension));
      
      fileList.addAll(Arrays.asList(files));

      files = folder.listFiles();
      for(File file : files) {
         if(file.isDirectory()) {
            fileList.addAll(getFilesRecursive(file, extension));
         }
      }

      return fileList;
   }


      /**
       * @param folder a File representing a folder.
       * @return all the files inside the given folder, excluding
       * other folders.
       */
      public static List<File> getFilesRecursive(File folder) {
      List<File> fileList = new ArrayList<File>();
      File[] files = folder.listFiles();

      for(File file : files) {
         if(file.isFile()) {
            fileList.add(file);
         }
      }

      for(File file : files) {
         if(file.isDirectory()) {
            fileList.addAll(getFilesRecursive(file));
         }
      }

      return fileList;
   }

   public static boolean copy(File source, File destination) {
      InputStream in = null;
      boolean success = true;
      try {
         File f1 = source;
         in = new FileInputStream(f1);
         return copy(in, destination);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(IoUtils.class.getName()).log(Level.SEVERE, null, ex);
         success = false;
      } finally {
         try {
            in.close();
         } catch (IOException ex) {
            Logger.getLogger(IoUtils.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

      return success;
   }

      /**
       * TODO: Clean method
       * @param source
       * @param destination
       * @return
       */
   public static boolean copy(InputStream source, File destination) {
      boolean success = true;
      try {
         //File f1 = source;
         File f2 = destination;
         // Create folders for f2
         f2.getParentFile().mkdirs();
         //InputStream in = new FileInputStream(f1);
         InputStream in = source;

         //For Append the file.
//      OutputStream out = new FileOutputStream(f2,true);

         //For Overwrite the file.
         OutputStream out = new FileOutputStream(f2);

         byte[] buf = new byte[1024];
         int len;
         while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
         }
         in.close();
         out.close();
         //System.out.println("'"+source.getName()+"' Copied file to '"+destination.getPath()+"'.");
         System.out.println("Copied file to '"+destination.getPath()+"'.");
      } catch (IOException e) {
         System.out.println(e.toString());
         success = false;
      }

      return success;
   }

   public static boolean deleteFolderContents(File folder) {
      if(!folder.isDirectory()) {
         System.err.println("Not a folder");
         return false;
      }

      System.out.println("Deleting contents of folder '"+folder.getName()+"'");
      for(File file : folder.listFiles()) {
         if(file.isDirectory()) {
            deleteFolderContents(file);
         }
         file.delete();
         System.out.println("Deleted '"+file.delete()+"'");
      }

      return true;
   }

   public static InputStream resourceToStream(String resourceName) {
   //public static File resourceToFile(String resourceName) {
      //Obtain the current classloader
      ClassLoader classLoader = IoUtils.class.getClassLoader();

      // Load the file as a resource
      //URL fileUrl = ClassLoader.getSystemResource(resourceName);
      //URL fileUrl = classLoader.getResource(resourceName);
      InputStream stream = classLoader.getResourceAsStream(resourceName);
      return stream;

      /*
      //if(fileUrl == null) {
      if(stream == null) {
         LoggingUtils.getLogger().
                 warning("Could not find resource '"+resourceName+"'");
         return null;
      }
      // TODO: WRITE AS STREAM
      // Turn the resource into a File object
      File file;
      try {
         file = new File(fileUrl.toURI());
      } catch (URISyntaxException ex) {
         LoggingUtils.getLogger().
                 warning(ex.toString());
         return null;
      }

      if(!file.exists()) {
         LoggingUtils.getLogger().
                 warning("Resource '"+file.getPath()+"' does not exist.");
      }
      
      return file;
       * 
       */
   }

    /**
     * Copies the given resources to the current path.
     *
     */
   /*
    public static void copyResourcesToLocal(List<String> filenames){
      for(String filename : filenames) {
         copyResourcesToLocal(filename);
         /*
         // Check if exists
         File file = new File(filename);
         if(file.exists()) {
            continue;
         }

         File resourceFile = IoUtils.systemResourceToFile(filename);
         IoUtils.copy(resourceFile, file);
          * 
          */
   /*
      }
    }
    *
    */

   /**
    * Copies the given resources to the current path.
    *
    */
   /*
   public static void copyResourcesToLocal(String filename) {
      // Check if exists
      File file = new File(filename);
      if (file.exists()) {
         return;
      }

      File resourceFile = IoUtils.resourceToFile(filename);
      IoUtils.copy(resourceFile, file);

   }
    * 
    */
   public static boolean extractZipResource(String resource, File folder) {
      boolean success = true;
      if (!folder.isDirectory()) {
         LoggingUtils.getLogger().
                 warning("Given folder '" + folder.getPath() + "' does not exist.");
         return false;
      }

      InputStream is = IoUtils.class.getResourceAsStream(resource);
      ZipInputStream zis = new ZipInputStream(is);

      ZipEntry entry;
      try {
         while ((entry = zis.getNextEntry()) != null) {
            File outFile = new File(folder, entry.getName());
            System.out.println("Unzipping '" + outFile.getPath() + "'.");

            int size;
            byte[] buffer = new byte[2048];


            FileOutputStream fos = new FileOutputStream(outFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

            while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
               bos.write(buffer, 0, size);
            }
            bos.flush();
            bos.close();

         }
         zis.close();
         is.close();

      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).log(Level.SEVERE, null, ex);
         success = false;
      }


      return success;
   }

      /**
    * Converts an object to an array of bytes.
    *
    * @param obj
    * @return
    */
   public static byte[] getBytes(Object obj) {
      ObjectOutputStream oos = null;
      try {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         oos = new ObjectOutputStream(bos);
         oos.writeObject(obj);
         oos.flush();
         oos.close();
         bos.close();
         byte[] data = bos.toByteArray();
         return data;
      } catch (IOException ex) {
         LoggingUtils.getLogger().
                 warning(ex.toString());
         return null;
      } finally {
         try {
            oos.close();
         } catch (IOException ex) {
         LoggingUtils.getLogger().
                 warning(ex.toString());
            return null;
         }
      }
   }

   /**
    * Recovers a String List from an array of bytes.
    * @param bytes
    * @return
    */
     public static Object getObject(byte[] bytes) {
      ObjectInputStream ois = null;
      try {
         ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         ois = new ObjectInputStream(bis);
         Object readObject = ois.readObject();
         ois.close();
         bis.close();
         return (List<String>) readObject;
         //bis.
         //byte[] data = bis.toByteArray();
         //return data;
      } catch (ClassNotFoundException ex) {
         LoggingUtils.getLogger().warning(ex.toString());
         return null;
      } catch (IOException ex) {
         LoggingUtils.getLogger().warning(ex.toString());
         return null;
      } finally {
         try {
            ois.close();
         } catch (IOException ex) {
            LoggingUtils.getLogger().warning(ex.toString());
            return null;
         }
      }
  }

   //
   //DEFINITIONS
   //
   /**
    * Default CharSet used in file operations.
    */
   final public static String DEFAULT_CHAR_SET = "UTF-8";
   final public static String DEFAULT_EXTENSION_SEPARATOR = ".";
   // Records the name of the last file appended
   //private static String lastAppeddedFileAbsolutePath = "";
   private static String lastAppeddedFileCanonicalPath = "";

   /**
    * INNER CLASS
    *
    * Accepts files with a certain extension.
    */
   static class ExtensionFilter implements FilenameFilter {

      public ExtensionFilter(String extension) {
         this.extension = extension;
         //this.separator = DEFAULT_EXTENSION_SEPARATOR;
         this.separator = "";
      }

      @Override
      public boolean accept(File dir, String name) {
         String suffix = separator + extension;
         return name.endsWith(suffix);
      }
      private String extension;
      private String separator;
   }



}
