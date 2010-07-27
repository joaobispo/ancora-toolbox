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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;

/**
 * Simple program which uses a Scripter to run programs.
 *
 * <p>Includes the base programs (Add, Set, etc...) and can be invoked with
 * the method 'start'. It has two modes: when the String[] args is empty, starts
 * the "shell mode"; otherwise, interprets the first argument as a file with a
 * script (the other arguments are ignored).
 *
 *
 * @author Joao Bispo
 */
public class SimpleScripter {

   /**
    * Creates a SimpleScripter.
    *
    * @param scripterPrograms the programs this scripter will support.
    * Automatically adds Base programs (Add, Set, etc...)
    * @param lineParser rules for parsing the commands
    * @param optionsTable state to be shared between the programs
    */
   public SimpleScripter(Map<String, String> scripterPrograms, LineParser lineParser, OptionsTable optionsTable) {
      // Add base programs to scripter programs
      addBasePrograms(scripterPrograms);

      scripter = new Scripter(scripterPrograms, lineParser, optionsTable);
      //public SimpleScripter(ProgramLauncher programLauncher, OptionsTable optionsTable, File programsFile) {
//      this.programLauncher = programLauncher;
//      this.optionsTable = new OptionsTable();

      // We need to set the information about where is file with the supported programs
 //     optionsTable.set(ProgramOption.programsFilename, programsFile.getPath());
   }

   /*
   public static SimpleScripter newSimpleProgram(File programsFile, File optionsFile)
   {
      ProgramLauncher programLauncher = ProgramLauncher.newProgramLauncher(programsFile);
      //OptionsTable optionsTable = OptionsTable.newOptionsTable(optionsFile);
      OptionsTable optionsTable = OptionsTableFactory.fromFile(optionsFile);

      return new SimpleScripter(programLauncher, optionsTable, programsFile);
   }
    *
    */

   public boolean start(String[] args) {
      if(args.length == 0) {
         return scripter.runShell();
      }

      File script =IoUtils.existingFile(args[0]);
      if(script == null) {
         return false;
      }

      String message = "Running file '"+script.getName()+".'";

      if(args.length > 1) {
         message += " Ignoring remaining arguments.";
      }

      LoggingUtils.getLogger(this).
              info(message);
      return scripter.runScript(script);
   }


   public Scripter getScripter() {
      return scripter;
   }



   /**
    * INSTANCE VARIABLES
    */
   private final Scripter scripter;
   private static final Map<String, String> basePrograms;

   //private ProgramLauncher programLauncher;
   //private OptionsTable optionsTable;
   static {
      basePrograms = new HashMap<String, String>();
      basePrograms.put("exit", "org.specs.Scripter.Base.Exit");
      basePrograms.put("help", "org.specs.Scripter.Base.Help");
      basePrograms.put("runscript", "org.specs.Scripter.Base.RunScript");
      basePrograms.put("set", "org.specs.Scripter.Base.Set");
      basePrograms.put("add", "org.specs.Scripter.Base.Add");
   }

   public static Map<String, String> getBasePrograms() {
      return basePrograms;
   }

   private void addBasePrograms(Map<String, String> scripterPrograms) {
      for(String key : basePrograms.keySet()) {
         String currentValue = basePrograms.get(key);
         String previousValue = scripterPrograms.put(key, currentValue);
         if(previousValue != null) {
            Logger.getLogger(SimpleScripter.class.getName()).
                    warning("Program '"+key+"' already defined! Substituted class "
                    + previousValue+ " for class "+currentValue);
         }
      }

   }
}
