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

package org.specs.MbGccScripter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;

/**
 *
 * @author Joao Bispo
 */
public class MbGccUtils {

   /**
    * Builds a list of inputs to process. Depending on the mode, the inputs
    * might be files or folders.
    * 
    * @return
    */
   /*
   public static List<File> getInputs(OptionsTable state) {
      // Determine mode
      String modeName = state.get(MbGccOption.gccMode);
      GccModes mode = EnumUtils.valueOf(GccModes.class, modeName);

      String inputFoldername = state.get(MbGccOption.inputFoldername);

      switch(mode) {
         case fileIsProgram:
            return getProgramFiles(inputFoldername);
         case folderIsProgram:
            return getProgramFolders(inputFoldername);
         default:
            Logger.getLogger(MbGccUtils.class.getName()).
                    warning("Case not defined: '"+mode+"'");
            return new ArrayList<File>();
      }

     
   }
*/

   public static List<File> getProgramFiles(String inputFoldername) {
      return IoUtils.getFilesRecursive(new File(inputFoldername), C_EXTENSION);
   }

   public static List<File> getProgramFolders(String inputFoldername) {
       List<File> programFolders = new ArrayList<File>();
      File inputFolder = new File(inputFoldername);

      for(File programFolder : inputFolder.listFiles()) {
         if(!programFolder.isDirectory()) {
            continue;
         }

         programFolders.add(programFolder);
      }

      return programFolders;
   }

   public static List<File> getFilesFromFolder(File input) {
      Set<String> extensions = new HashSet<String>();
      extensions.add(C_EXTENSION);
      extensions.add(HEADER_EXTENSION);

      List<File> fileList = IoUtils.getFilesRecursive(input, extensions);

      /*
      List<String> filenameList = new ArrayList<String>(fileList.size());


      for(int i=0; i<fileList.size(); i++) {
         filenameList.add(fileList.get(i).getName());
      }
*/
      return fileList;
   }

   public static final String C_EXTENSION = "c";
   public static final String HEADER_EXTENSION = "h";
}
