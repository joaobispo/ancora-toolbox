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

package org.specs.CoverageOverIterations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.Partitioning.SupportedPartitioners;

/**
 * Several utility methods for this program.
 *
 * @author Joao Bispo
 */
public class Utils {

   /**
    * Returns a list with the programs to execute. From the input folder,
    * returns all the files inside that folder
    * 
    * @param options
    * @return
    */
   public static List<File> getInputPrograms(Map<String, AppValue> options) {     
      String programsFoldername = AppUtils.getString(options, Options.FolderWithPrograms);
      File programsFolder = IoUtils.safeFolder(programsFoldername);
      if(programsFoldername == null) {
         return null;
      }

      return IoUtils.getFilesRecursive(programsFolder);
   }

   /**
    *
    * @param options
    * @return a list with the selected partitioners
    */
   public static List<SupportedPartitioners> getPartitioners(Map<String, AppValue> options) {
      List<String> partitioners = AppUtils.getStringList(options, Options.Partitioners);

      List<SupportedPartitioners> parts = new ArrayList<SupportedPartitioners>();
      for (String partitionerName : partitioners) {
         // Decode name into enum
         SupportedPartitioners partEnum =
                 EnumUtils.valueOf(SupportedPartitioners.class, partitionerName);
         if (partEnum == null) {
            LoggingUtils.getLogger().
                    warning("Ignoring partitioner '" + partitionerName + "'");
            continue;
         }
         parts.add(partEnum);
      }


      return parts;
   }

}
