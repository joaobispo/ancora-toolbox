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
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.OptionsTable.OptionsTable;
import org.specs.OptionsTable.OptionsTableFactory;

/**
 *
 * @author Joao Bispo
 */
public class SimpleProgram {

   public SimpleProgram(ProgramLauncher programLauncher, OptionsTable optionsTable, File programsFile) {
      this.programLauncher = programLauncher;
      this.optionsTable = optionsTable;

      // We need to set the information about where is file with the supported programs
      optionsTable.set(ProgramOption.supportedPrograms, programsFile.getPath());
   }

   public static SimpleProgram newSimpleProgram(File programsFile, File optionsFile)
   {
      ProgramLauncher programLauncher = ProgramLauncher.newProgramLauncher(programsFile);
      //OptionsTable optionsTable = OptionsTable.newOptionsTable(optionsFile);
      OptionsTable optionsTable = OptionsTableFactory.fromFile(optionsFile);

      return new SimpleProgram(programLauncher, optionsTable, programsFile);
   }

   public boolean start(String[] args) {
      if(args.length == 0) {
         return programLauncher.runShell(optionsTable);
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
      return programLauncher.runScript(script, optionsTable);
   }


   public OptionsTable getOptionsTable() {
      return optionsTable;
   }

   public ProgramLauncher getProgramLauncher() {
      return programLauncher;
   }



   /**
    * INSTANCE VARIABLES
    */
   private ProgramLauncher programLauncher;
   private OptionsTable optionsTable;
}
