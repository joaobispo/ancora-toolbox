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

package org.specs.Scripter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;

/**
 * A Scripter can launch several Program objects, which work over an OptionsTable
 * which serves as a global state for the several programs. This OptionsTable is
 * kept by the Scripter object itself.
 *
 * @author Joao Bispo
 */
public class Scripter {

   /**
    * Builds a new Scripter.
    * 
    * @param scripterPrograms a table which maps the name by which the Program 
    * can be launched and the name of the class of the program. Ex.: set ->
    * org.specs.Scripter.Base.Set
    * <p>Use method "SharedLibrary.ParseUtils.parseTableFromFile" to load such
    * a table from a File.
    * @param lineParser rules by which the scripts will be parsed
    * @param optionsTable State that will be shared between the programs
    */
   public Scripter(Map<String, String> scripterPrograms, LineParser lineParser, OptionsTable optionsTable) {
      this.scripterPrograms = scripterPrograms;
      this.lineParser = lineParser;
      this.optionsTable = optionsTable;

      this.shellWelcome = DEFAULT_SHELL_WELCOME;

      // Add the options of the scripter to options table.
      ScripterOption.storeProgramsTable(scripterPrograms, optionsTable);
      ScripterOption.storeLineParser(lineParser, optionsTable);

   }

   /**
    * Builds a table compatible with the Scripter constructor from a settings file
    * which maps the name of the program to the class that implements it.
    *
    * @param file
    * @return
    */
   /*
   public static Map<String, String> loadScripterPrograms(File file, LineParser lineParser) {
      return ParseUtils.parseTableFromFile(file, lineParser);
   }
    *
    */

   /**
    * Builds a ProgramLauncher object from a settings file which maps the name
    * of the program to the class that implements it.
    *
    * @param supportedPrograms
    * @return a ProgramLauncher which supports the programs in the given properties
    * file, or null if it could not be built.
    */

   //public static Scripter newProgramLauncher(File supportedPrograms) {
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
/*
      //Map<String, String> programsTable = (new LineParser()).getTableFromFile(supportedPrograms);
      LineParser lineParser = new LineParser(" ", "\"", "//");
      Map<String, String> programsTable = ParseUtils.parseTableFromFile(supportedPrograms, lineParser);
      return new Scripter(programsTable);
   }
 *
 */

   public boolean runShell() {
   //public boolean runShell(OptionsTable state) {
      // Initialize Scanner
       Scanner scanner = new Scanner(System.in);


       // Show welcome message
       LoggingUtils.getLogger(this).
               info(shellWelcome);

       // Set main cycle state to true
       boolean cycleState = true;
       optionsTable.set(ScripterOption.isRunningShell, Boolean.toString(cycleState));

       // Start cycle
       while(cycleState) {
          String command = scanner.nextLine();
          executeProgramCall(command);
          cycleState = Boolean.parseBoolean(optionsTable.get(ScripterOption.isRunningShell));
       }

       return true;
   }

//   public boolean runScript(File script, OptionsTable state) {
   public boolean runScript(File script) {
      LineReader lineReader = LineReader.createLineReader(script);
      if(lineReader == null) {
         return false;
      }

      String command;
      int lineCounter = 1;
      while((command = lineReader.nextLine()) != null) {
         boolean success = executeProgramCall(command);
         if(!success) {
            LoggingUtils.getLogger(this).
                     warning("Problems on line "+lineCounter+", file '"+script.getAbsolutePath()+"'");
         }

         lineCounter++;
      }

      return true;
   }

   //public boolean executeProgramCall(String programCall, OptionsTable state) {
   public boolean executeProgramCall(String programCall) {

      // Parse command
      List<String> splitCommand = lineParser.splitCommand(programCall);

      // If there is no command, return
      if(splitCommand.isEmpty()) {
         return true;
      }

      // Get Program
      String programName = splitCommand.get(0);
      Program program = getProgram(programName, scripterPrograms);

      // Could not get program
      if(program == null) {
         return false;
      }


      // Get arguments for program
      List<String> arguments = new ArrayList<String>();
      arguments.addAll(splitCommand.subList(1, splitCommand.size()));
   


      return program.execute(arguments, optionsTable);

   }

   public void setShellWelcome(String shellWelcome) {
      this.shellWelcome = shellWelcome;
   }


   /**
    * Instantiates a new program object.
    *
    * @param programName the name by which the program is called
    * @param supportedPrograms a table which maps the name by which the Program
    * can be launched and the name of the class of the program.
    * @return a Program object
    */
   public static Program getProgram(String programName, Map<String, String> supportedPrograms) {
      String programClassName = supportedPrograms.get(programName);
      if(programClassName == null) {
         Logger.getLogger(Scripter.class.getName()).
                 warning("Program not supported: '"+programName+"'");
         return null;
      }

      Class programClass = null;
      try {
         programClass = Class.forName(programClassName);
      } catch (ClassNotFoundException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(Scripter.class.getName()).
                 warning("Could not find class '"+programClassName+"'");
         return null;
      }

      Program program = null;
      try {
         program = (Program) programClass.newInstance();
      } catch (InstantiationException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(Scripter.class.getName()).
                 warning("Could not instantiate class '"+programClass.getName()+"'. Message:"+ex.getMessage());
         return null;
      } catch (IllegalAccessException ex) {
         //LoggingUtils.getLogger(this).
         Logger.getLogger(Scripter.class.getName()).
                 warning("IllegalAccessException. Message:"+ex.getMessage());
         return null;
      }

      return program;
   }



   public OptionsTable getOptionsTable() {
      return optionsTable;
   }

   public LineParser getLineParser() {
      return lineParser;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   //private OptionsTable state;
   private final Map<String, String> scripterPrograms;
   private final LineParser lineParser;
   private final OptionsTable optionsTable;

   private String shellWelcome;
   private static final String DEFAULT_SHELL_WELCOME = "Welcome to SCRIPT InterpretER";



}
