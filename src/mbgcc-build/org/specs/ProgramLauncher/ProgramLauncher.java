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

package org.specs.ProgramLauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Files.LineParser;
import org.specs.OptionsTable.OptionsTable;

/**
 * Launches several programs over a program state
 *
 * @author Joao Bispo
 */
public class ProgramLauncher {

   private ProgramLauncher(Map<String, String> supportedPrograms) {
   //private ProgramLauncher(OptionsTable state, Map<String, String> supportedPrograms) {
      //this.state = state;
      this.supportedPrograms = supportedPrograms;

      this.shellWelcome = DEFAULT_SHELL_WELCOME;
      this.commandParser = new LineParser();
   }


   /**
    * Builds a ProgramLauncher object from a settings file which maps the name
    * of the program to the class that implements it.
    *
    * @param supportedPrograms
    * @return a ProgramLauncher which supports the programs in the given properties
    * file, or null if it could not be built.
    */
   public static ProgramLauncher newProgramLauncher(File supportedPrograms) {
     /*
      // Get properties file
      Properties properties = IoUtils.loadProperties(supportedPrograms);
      if(properties == null) {
         Logger.getLogger(ProgramLauncher.class.getName()).
                 warning("Could not load properties object for file '"+supportedPrograms.getAbsolutePath()+"'.");
         return null;
      }

      Map<String, String> programsTable = new HashMap<String, String>();
      for(String key : properties.stringPropertyNames()) {
         String value = properties.getProperty(key);
         programsTable.put(key, value);

      }
*/

      Map<String, String> programsTable = (new LineParser()).getTableFromFile(supportedPrograms);
      return new ProgramLauncher(programsTable);
   }

   public boolean runShell(OptionsTable state) {
      // Initialize Scanner
       Scanner scanner = new Scanner(System.in);


       // Show welcome message
       LoggingUtils.getLogger(this).
       //Logger.getLogger(ProgramLauncher.class.getName())
               info(shellWelcome);

       // Get main cycle state
       boolean cycleState = Boolean.parseBoolean(state.get(ProgramOption.shellCycleState));

       // Start cycle
       while(cycleState) {
          String command = scanner.nextLine();
          executeProgramCall(command, state);
          cycleState = Boolean.parseBoolean(state.get(ProgramOption.shellCycleState));
       }

       return true;
   }

   public boolean runScript(File script, OptionsTable state) {
      LineReader lineReader = LineReader.createLineReader(script);
      if(lineReader == null) {
         return false;
      }

      String command;
      int lineCounter = 1;
      while((command = lineReader.nextLine()) != null) {
         boolean success = executeProgramCall(command, state);
         if(!success) {
            LoggingUtils.getLogger(this).
                     warning("Problems on line "+lineCounter+", file '"+script.getAbsolutePath()+"'");
            //Logger.getLogger(ProgramLauncher.class.getName()).
            //        warning("Test2(Problems on line "+lineCounter+", file "+script.getAbsolutePath()+")");
         }

         lineCounter++;
      }

      return true;
   }

   public boolean executeProgramCall(String programCall, OptionsTable state) {

      // Parse command
      List<String> splitCommand = commandParser.splitCommand(programCall);

      // If there is no command, return
      if(splitCommand.isEmpty()) {
         return true;
      }

      // Get Program
      String programName = splitCommand.get(0);
      Program program = getProgram(programName, supportedPrograms);

      // Could not get program
      if(program == null) {
         return false;
      }


      // Get arguments for program
      List<String> arguments = new ArrayList<String>();
      arguments.addAll(splitCommand.subList(1, splitCommand.size()));

      /// Check simple commands (exit, runscript)
      // Check simple commands (exit)
      /*
      if (program.getProgramName() == ProgramName.exit) {
         logger.info("Bye!");
         System.exit(0);
      }

      if(program.getProgramName() == ProgramName.runScript) {
         // Run script given in the arguments
         runScript(arguments);
         return true;
      }
       *
       */


      return program.execute(arguments, state);

   }

   public void setShellWelcome(String shellWelcome) {
      this.shellWelcome = shellWelcome;
   }


   public static Program getProgram(String programName, Map<String, String> supportedPrograms) {
      String programClassName = supportedPrograms.get(programName);
      if(programClassName == null) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(ProgramLauncher.class.getName()).
                 warning("Program not supported: '"+programName+"'");
         return null;
      }

      Class programClass = null;
      try {
         programClass = Class.forName(programClassName);
      } catch (ClassNotFoundException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(ProgramLauncher.class.getName()).
                 warning("Could not find class '"+programClassName+"'");
         return null;
      }

      Program program = null;
      try {
         program = (Program) programClass.newInstance();
      } catch (InstantiationException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(ProgramLauncher.class.getName()).
                 warning("Could not instantiate class '"+programClass.getName()+"'. Message:"+ex.getMessage());
         return null;
      } catch (IllegalAccessException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(ProgramLauncher.class.getName()).
                 warning("IllegalAccessException. Message:"+ex.getMessage());
         return null;
      }

      return program;
   }

   /**
    * INSTANCE VARIABLES
    */
   //private OptionsTable state;
   private Map<String, String> supportedPrograms;

   private LineParser commandParser;
   private String shellWelcome;
   private static final String DEFAULT_SHELL_WELCOME = "Shell Mode";

}
