/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.SharedLibrary.Utilities;

import java.io.File;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Similar to a StringBuilder, but buffered, for writing large files.
 *
 * <p>Default buffer size is close to 1.5MBytes.
 *
 * @author Joao Bispo
 */
public class BufferedWriter {

   /**
    * WARNING: The contents of the file given to this class will be erased when
    * the object is created.
    * 
    * @param outputFile
    */
   public BufferedWriter(File outputFile) {
      this.writeFile = outputFile;

      // Erase last trace
      IoUtils.write(outputFile, "");

      builder = newStringBuilder();
   }


   public void close() {
      IoUtils.append(writeFile, builder.toString());
      builder = null;
   }

   public BufferedWriter append(int integer) {
      return append(Integer.toString(integer));
   }

   public BufferedWriter append(Object object) {
      return append(object.toString());
   }

   public BufferedWriter append(String string) {
      if(builder == null) {
         LoggingUtils.getLogger().
                 warning("Object has already been closed.");
         return null;
      }

      // Add to StringBuilder
      builder.append(string);
      if (builder.length() < BUFFER_CAPACITY) {
         return this;
      }

      IoUtils.append(writeFile, builder.toString());
      builder = newStringBuilder();

      return this;
   }


   private StringBuilder newStringBuilder() {
      return new StringBuilder((int)(BUFFER_CAPACITY*1.10));
   }


   /**
    * INSTANCE VARIABLES
    */
   private File writeFile;
   private StringBuilder builder;
   public final static int BUFFER_CAPACITY = 800000;

}
