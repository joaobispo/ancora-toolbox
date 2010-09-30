/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.specs.MicroBlazeSimulatorTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import system.Util;

/**
 *
 * @author Joao Bispo
 */
public class BufferedWriter {

   /*
   public TraceFile(PrintStream output, File writeFile, FileOutputStream outputFileStream) {
      this.output = output;
      this.writeFile = writeFile;
      this.outputFileStream = outputFileStream;
   }
    *
    */


   private BufferedWriter(File traceFile) {
      this.writeFile = traceFile;

      // Erase last trace
      IoUtils.write(traceFile, "");

      builder = newStringBuilder();
   }

   public static BufferedWriter newTraceFile(Map<String, AppValue> options) {
      boolean writeTrace = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.WriteTrace));
      if (!writeTrace) {
         return null;
      }

      String traceFilename = AppUtils.getString(options, TesterOption.TraceFile);
      File traceFile = new File(traceFilename);

      return new BufferedWriter(traceFile);
   }

   /*
   public static TraceFile newTraceFile(Map<String, AppValue> options) {
      FileOutputStream file_output = null;
      try {

         boolean writeTrace = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.WriteTrace));
         if (!writeTrace) {
            return null;
         }

         String traceFilename = AppUtils.getString(options, TesterOption.TraceFile);
         File traceFile = new File(traceFilename);
         // Erase last trace
         IoUtils.write(traceFile, "");

         file_output = new FileOutputStream(traceFile);
         PrintStream output = new PrintStream(file_output);

         return new TraceFile(output, traceFile, file_output);
      }
      //output.println("0x" + Util.toHexString(pc,8) + "  " + instruction);
      catch (FileNotFoundException ex) {
         LoggingUtils.getLogger().
                 warning(ex.getMessage());
      } finally {
         try {
            file_output.close();
         } catch (IOException ex) {
            LoggingUtils.getLogger().
                 warning(ex.getMessage());
         }
      }

      return null;
   }
*/

   public void writeInstruction(int pc, String instruction) {

      //builder.append(builder)
      String traceLine = "0x" + Util.toHexString(pc,8) + "  " + instruction+"\n";
      writeStringBuilder(traceLine);
      //IoUtils.append(writeFile, traceLine+"\n");
      //output.println("0x" + Util.toHexString(pc,8) + "  " + instruction);
   }

   public void close() {
      IoUtils.append(writeFile, builder.toString());
      builder = null;
 /*
      try {
         output.flush();
         outputFileStream.close();
         LoggingUtils.getLogger().info("File written (" + writeFile.getAbsolutePath() + ").");
      } catch (IOException ex) {
         LoggingUtils.getLogger().
                 warning(ex.getMessage());
      }
  *
  */
   }


   private StringBuilder newStringBuilder() {
      return new StringBuilder((int)(BUFFER_CAPACITY*1.10));
   }


   private void writeStringBuilder(String traceLine) {
      if(builder == null) {
         LoggingUtils.getLogger().
                 warning("TraceFile is already closed.");
         return;
      }

      // Add to StringBuilder
      builder.append(traceLine);
      if(builder.length() < BUFFER_CAPACITY) {
         return;
      }

      IoUtils.append(writeFile, builder.toString());
      builder = newStringBuilder();
   }

   /**
    * INSTANCE VARIABLES
    */
   //private FileOutputStream outputFileStream;
   //private PrintStream output;
   private File writeFile;
   private StringBuilder builder;
   public final static int BUFFER_CAPACITY = 800000;



}
