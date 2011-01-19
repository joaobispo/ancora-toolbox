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

package org.specs.MbGccScripter.Programs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.specs.MbGccScripter.GccModes;
import org.specs.MbGccScripter.MbGccOption;
import org.specs.MbGccScripter.MbGccRun;
import org.specs.MbGccScripter.MbGccUtils;
import org.specs.Scripter.Program;

/**
 *
 * @author Joao Bispo
 */
public class Compile implements Program {

   @Override
   public boolean execute(List<String> arguments, OptionsTable state) {
      try {
         /*
         // Prepare flags
         List<String> flagsArray = state.getList(MbGccOption.flagList);
         // Prepare optimizations
         List<String> optArray = state.getList(MbGccOption.optionList);
         List<File> inputs = MbGccUtils.getInputs(state);
         boolean result = true;
          */
         // Determine mode
         String modeName = state.get(MbGccOption.gccMode);
         GccModes mode = EnumUtils.valueOf(GccModes.class, modeName);
         switch (mode) {
            case fileIsProgram:
               return runFileIsProgram(state);
            case folderIsProgram:
               return runFolderIsProgram(state);
            default:
               LoggingUtils.getLogger(this).warning("Case not defined: '" + mode + "'");
               return false;
         }
         /*
         for(File input : inputs) {
         for(String optimization : optArray) {
         // Get files
         List<File> inputFiles = getFiles(mode, input);
         String baseFilename = getBaseFilename(mode, input);
         }
         }
          */
         // Prepare files
         //File[] files = (new File(DEFAULT_INPUT_FOLDER)).listFiles();
         //List<File> files = IoUtils.getFilesRecursive(new File(DEFAULT_INPUT_FOLDER));
         /*
         List<File> programFolders = getProgramFolders();
         for(File programFolder : programFolders) {
         //for(List<File> fileArray : programs) {
         for(String optimization : optArray) {
         // Get files
         String[] inputFiles = getProgramFiles(programFolder);
         //String baseFilename = IoUtils.removeExtension(inputFiles[0], IoUtils.DEFAULT_EXTENSION_SEPARATOR);
         String baseFilename = programFolder.getName();
         String parentInputFolder = programFolder.getParent();
         String outputFolder = parentInputFolder+"\\elf\\"+optimization.substring(1)+"\\";
         //String outputFolder = DEFAULT_INPUT_FOLDER+"\\..\\elf\\"+optimization.substring(1)+"\\";
         //String outputFile = baseFilename + optimization + ".elf";
         String outputFile = outputFolder + baseFilename + optimization + ".elf";
         //System.out.println("Program Folder:"+programFolder.getName());
         //System.out.println("Program Folder:"+programFolder.getAbsolutePath());
         MbGccRun run = new MbGccRun(outputFile, inputFiles, optimization, flagsArray, programFolder.getAbsolutePath());
         int intResult = run.run();
         if(intResult != 0) {
         LoggingUtils.getLogger(this).
         warning("MbGcc returned result other than 0 ("+intResult+")");
         result = false;
         }
         }
         }
         return result;
          *
          */
      } catch (InterruptedException ex) {
         Logger.getLogger(Compile.class.getName()).log(Level.SEVERE, null, ex);
         Thread.currentThread().interrupt();
      }

      return false;
   }

   @Override
   public String getHelpMessage(OptionsTable state) {
      return "Compiles the C files with mbgcc using the current configuration.";
   }
/*
   private List<File> getFiles(GccModes gccMode, File input) {
      List<File> result = new ArrayList<File>();
      switch(gccMode) {
         case fileIsProgram:
            result.add(input);
            break;
         case folderIsProgram:
            result.addAll(MbGccUtils.getFilesFromFolder(input));
            break;
          default:
             LoggingUtils.getLogger(this).
                     warning("Case not defined: '"+gccMode+"'");
             break;

      }

      return result;
   }
*/
   private boolean runFolderIsProgram(OptionsTable state) throws InterruptedException {
      // Prepare flags
      List<String> flagsArray = state.getList(MbGccOption.flagList);
      // Prepare optimizations
      List<String> optArray = state.getList(MbGccOption.optionList);
      String inputFoldername = state.get(MbGccOption.inputFoldername);
      List<File> inputs = MbGccUtils.getProgramFolders(inputFoldername);

      boolean result = true;

      for(File input : inputs) {
         for(String optimization : optArray) {
            // Get files
            List<File> inputFiles = MbGccUtils.getFilesFromFolder(input);
            String baseFilename = input.getName();

            String parentInputFolder = input.getParent();
            String outputFolder = parentInputFolder+'/'+ELF+'/'+optimization.substring(1)+'/';

            String outputFile = outputFolder + baseFilename + optimization + '.' + ELF;
            //System.out.println("Program Folder:"+programFolder.getName());
            //System.out.println("Program Folder:"+programFolder.getAbsolutePath());
            MbGccRun run = new MbGccRun(outputFile, inputFiles, optimization, flagsArray, input.getAbsolutePath());
            int intResult = run.run();
            if(intResult != 0) {
               LoggingUtils.getLogger(this).
                       warning("MbGcc returned result other than 0 ("+intResult+")");
               result = false;
            }

         }
      }


      return result;
   }

   private boolean runFileIsProgram(OptionsTable state) throws InterruptedException {
            // Prepare flags
      List<String> flagsArray = state.getList(MbGccOption.flagList);
      // Prepare optimizations
      List<String> optArray = state.getList(MbGccOption.optionList);
      String inputFoldername = state.get(MbGccOption.inputFoldername);
      List<File> inputs = MbGccUtils.getProgramFiles(inputFoldername);
      
     boolean result = true;

      for(File input : inputs) {
         for(String optimization : optArray) {
            List<File> inputFiles = new ArrayList<File>();
            inputFiles.add(input);

            String baseFilename = IoUtils.removeExtension(input.getName(), IoUtils.DEFAULT_EXTENSION_SEPARATOR);
            String parentInputFolder = (new File(inputFoldername)).getParent();
            String outputFolder = parentInputFolder+'/'+ELF+'/'+optimization.substring(1)+'/';
            //String outputFolder = DEFAULT_INPUT_FOLDER+"\\..\\elf\\"+optimization.substring(1)+"\\";
            //String outputFile = baseFilename + optimization + ".elf";
            String outputFile = outputFolder + baseFilename + optimization + '.' + ELF;

            MbGccRun run = new MbGccRun(outputFile, inputFiles, optimization, flagsArray, inputFoldername);
            int intResult = run.run();
            if(intResult != 0) {
               LoggingUtils.getLogger(this).
                       warning("MbGcc returned result other than 0 ("+intResult+")");
               result = false;
            }
         }
      }

      return result;
   }


   private static final String ELF = "elf";
}
