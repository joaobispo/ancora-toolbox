/*
 *  Copyright 2010 SPeCS Research Group.
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

package org.ancora.SharedLibrary.AppBase.PreBuiltTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Represents a multipleChoice string which be a path to a single file, or the
 * path to a folder with the files we want.
 *
 * @author Joao Bispo
 */
public enum InputType {
   SingleFile,
   FilesInFolder;

   /**
    * Check if input is is single file or folder
    *
    * @param options
    * @return
    */
   public static List<File> getFiles(Map<String, AppValue> options,
           AppOptionEnum option, InputType type) {
      //String inputTypeName = AppUtils.getString(options, Options.InputType);
      //InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      // Is Folder mode
      if (type == InputType.FilesInFolder) {
         File inputFolder = AppUtils.getExistingFolder(options, option);
         if (inputFolder == null) {
            LoggingUtils.getLogger().
                    warning("Could not open folder.");
            return null;
         }
         return IoUtils.getFilesRecursive(inputFolder);
      }

      if (type == InputType.SingleFile) {
         File inputFile = AppUtils.getExistingFile(options, option);
         if (inputFile == null) {
            LoggingUtils.getLogger().
                    warning("Could not open file.");
            return null;
         }
         List<File> files = new ArrayList<File>();
         files.add(inputFile);
         return files;
      }

      LoggingUtils.getLogger().
              warning("Case not defined:'" + type + "'");
      return null;
   }
}
