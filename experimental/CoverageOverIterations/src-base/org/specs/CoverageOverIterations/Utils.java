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
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DToolPlus.Utilities.EasySystem;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DToolPlus.DToolReader;
import system.SysteM;

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
   public static List<LoopDetectors> getPartitioners(Map<String, AppValue> options) {
      List<String> partitioners = AppUtils.getStringList(options, Options.LoopDetectors);

      List<LoopDetectors> parts = new ArrayList<LoopDetectors>();
      for (String partitionerName : partitioners) {
         // Decode name into enum
         LoopDetectors partEnum =
                 EnumUtils.valueOf(LoopDetectors.class, partitionerName);
         if (partEnum == null) {
            LoggingUtils.getLogger().
                    warning("Ignoring partitioner '" + partitionerName + "'");
            continue;
         }
         parts.add(partEnum);
      }


      return parts;
   }


   /**
    * Instantiates a DToolReader loaded with an Elf file and default SysteM
    * configuration.
    *
    * @param elfFile
    * @return
    */
   /*
   public static DToolReader newTraceReader(File elfFile) {
      return newTraceReader(elfFile, null);
   }
    * 
    */

   /**
    * Instantiates a DToolReader loaded with an Elf file.
    *
    * @param elfFile
    * @param config
    * @return a DToolReader loaded with the given file, or null if the object
    * could not be created
    */
   public static DToolReader newTraceReader(File elfFile, SystemSetup config) {
      String systemConfig = "./Configuration Files/systemconfig.xml";
      String elfFilename = elfFile.getPath();

      SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, config);
      if(originalSystem == null) {
         LoggingUtils.getLogger().
                 warning("Could not create SysteM object.");
         return null;
      }
      EasySystem system = new EasySystem(originalSystem);
      DToolReader dtoolReader = new DToolReader(system);

      return dtoolReader;
   }

   public static final String MICROBLAZE_SYSTEM_CONFIG = "./Configuration Files/systemconfig.xml";

}
