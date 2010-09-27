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

import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Logging.ConsoleFormatter;
import org.ancora.SharedLibrary.Logging.LoggingOutputStream;

/**
 * Methods for the Java Logger API.
 * 
 * @author Joao Bispo
 */
public class LoggingUtils {

   /**
    * Helper method to automatically get the Logger correspondent to the class
    * of the given object.
    * 
    * @param object
    * @return logger specific to the given object
    */
   public static Logger getLogger(Object object) {
      return Logger.getLogger(object.getClass().getName());
   }

   /**
    * 
    * @return
    */
   public static Logger getRootLogger() {
      return Logger.getLogger("");
   }

   /**
    * Helper method to automatically get the Logger correspondent to the class
    * which calls this method.
    *
    * <p>Thes method uses the "heavy" StackTrace to determine what object called
    * it. This should only be used for logging "warnings" which are not called
    * often, or in situation such as constructors, were we do not want to leak
    * the object reference before it exists.
    *
    * @param object
    * @return logger specific to the given object
    */
   public static Logger getLogger() {
      StackTraceElement stackElement = ProcessUtils.getCallerMethod();
      return Logger.getLogger(stackElement.getClassName());
   }

   /**
    * Redirects the System.out stream to the logger.
    *
    * <p>Anything written to System.out is recorded as a log at info level.
    */
   public static void redirectSystemOut() {
      // Get Root Logger
      //Logger logger = Logger.getLogger("");
      Logger logger = getRootLogger();

      // Build Printstream for System.out
      LoggingOutputStream los = new LoggingOutputStream(logger, Level.INFO);
      //LoggingOutputStream los = new LoggingOutputStream(logger, Level.FINEST);
      PrintStream outPrint = new PrintStream(los, true);

      // Set System.out
      System.setOut(outPrint);
   }

   /**
    * Redirects the System.err stream to the logger.
    *
    * <p>Anything written to System.err is recorded as a log at warning level.
    */
   public static void redirectSystemErr() {
      // Get Root Logger
//      Logger logger = Logger.getLogger("");
      Logger logger = getRootLogger();

      // Build Printstream for System.out
      LoggingOutputStream los = new LoggingOutputStream(logger, Level.WARNING);
      PrintStream outPrint = new PrintStream(los, true);

      // Set System.out
      System.setErr(outPrint);
   }



   /**
    * Removes current handlers and adds the given Handlers to the root logger.
    *
    * @param handlers the Handlers we want to set as the root Handlers.
    */
   public static void setRootHandlers(Handler[] handlers) {
      //Logger logger = Logger.getLogger("");
      Logger logger = getRootLogger();

      // Remove all previous handlers
      Handler[] handlersTemp = logger.getHandlers();
      for(Handler handler : handlersTemp) {
         logger.removeHandler(handler);
      }

      // Add Handlers
      for(Handler handler : handlers) {
         logger.addHandler(handler);
      }
   }

   /**
    * builds a Console Handler which uses as formatter, ConsoleFormatter.
    *
    * @return a Console Hanlder formatted by ConsoleFormatter.
    */
   public static Handler buildConsoleHandler() {    
         ConsoleHandler cHandler = new ConsoleHandler();
         cHandler.setFormatter(new ConsoleFormatter());
         cHandler.setLevel(Level.ALL);

         return cHandler;
   }

   /**
    * Automatically setups the root logger for output to the console. Redirects
    * System.out and System.err to the logger as well.
    */
   public static void setupConsoleOnly() {      
      // Build ConsoleHandler
      Handler[] handlers = new Handler[1];
      handlers[0] = buildConsoleHandler();
      setRootHandlers(handlers);
      
      // Redirect System.out
      redirectSystemOut();

      // Redirect System.err
      redirectSystemErr();
   }


   public static Level parseLevel(String levelString) {
      Level defaultLevel = Level.ALL;
      Level level = defaultLevel;
      // Parse Logger Level
      try {
         level = Level.parse(levelString);
      } catch(IllegalArgumentException ex) {
         Logger.getLogger(LoggingUtils.class.getName()).
                 info("Could not parse logger level '"+levelString+"'. " +
                 "Setting level to '"+defaultLevel+"'");
      }

      return level;
   }

   // Preserving a reference to original stdout/stderr streams,
   // in case they might be useful.
   public final static PrintStream stdout = System.out;
   public final static PrintStream stderr = System.err;
}
