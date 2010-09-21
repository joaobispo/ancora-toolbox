/*
 *  Copyright 2010 Ancora Research Group.
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

package org.specs.AutoCompile;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Options.TargetOption;

/**
 * Represents the available targets for compilation. A target is a pair composed
 * by a processor and a compiler for that processor.
 *
 * @author Joao Bispo
 */
public class Targets {

   private Targets() {
      targets = new HashMap<String, Set<String>>();
      targetFiles = new HashMap<String, String>();

   }

   /*
   public Map<String, String> getTargetFiles() {
      return targetFiles;
   }
    *
    */
/*
   public Map<String, Set<String>> getTargets() {
      return targets;
   }
 * 
 */

   /**
    * @param processor
    * @param compiler
    * @return a file representing a configuration for a specific processor-compiler
    * pair (e.g.: MicroBlaze-mbgcc)
    */
   public File getTargetConfig(String processor, String compiler) {
      // Get target
      Set<String> compilerSet = targets.get(processor);
      if(compilerSet == null) {
         LoggingUtils.getLogger().
                 info("Could not find processor '"+processor+"'. Available processors:");
         printProcessors(Level.INFO);
         return null;
      }

      // Get compiler
      if(!compilerSet.contains(compiler)) {
         LoggingUtils.getLogger().
                 info("Could not find compiler '"+compiler+
                 "'. Available compilers for processor '"+processor+"':");
         printCompilers(processor, Level.INFO);
         return null;
      }

      // Confirm if file exists
      String targetName = getTargetName(processor, compiler);
      String targetFilename = targetFiles.get(targetName);
      File targetFile = new File(targetFilename);
      if(!targetFile.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not find file '" + targetFilename
                 + "' for target '" + getTargetName(processor, compiler) + "'.");
         return null;
      }

      return targetFile;
   }

   


   public static Targets buildTargets(String targetFolderpath) {
      // Read target folder and load targets
      File targetFolder = IoUtils.safeFolder(targetFolderpath);
      if(targetFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not access folder '"+targetFolder.getPath()+"'");
         return null;
      }

      Targets targets = new Targets();
      List<File> tFiles = IoUtils.getFilesRecursive(targetFolder);

      // For each found file, add to the tables
      for(File tFile : tFiles) {
          Map<String,AppOption> map = AppUtils.parseFile(tFile);
          if(map == null) {
             LoggingUtils.getLogger().
                     warning("Skipping file '"+tFile.getPath()+"'.");
             continue;
          }

          String target = AppUtils.getString(map, TargetOption.target);
          String compiler = AppUtils.getString(map, TargetOption.compiler);

          targets.addProcessorCompiler(target, compiler, tFile.getPath());
      }

      return targets;
   }

   private void addProcessorCompiler(String processor, String compiler, String path) {
      Set<String> compilerNames = targets.get(processor);
      if(compilerNames == null) {
         compilerNames = new HashSet<String>();
         targets.put(processor, compilerNames);
      }

      if(!compilerNames.add(compiler)) {
         LoggingUtils.getLogger().
                 warning("Duplicate entry for target '"+processor+"' - compiler '"+compiler+"'.");
      }

      targetFiles.put(getTargetName(processor, compiler), path);
   }


   /**
    * Given the name of the target processor and the compiler, returns a String
    * which can be used as an identifier.
    * 
    * @param target
    * @param compiler
    * @return
    */
   public static String getTargetName(String processor, String compiler) {
      return processor + "." + compiler;
   }


   /*
    * Prints the available processors.
    */
   private void printProcessors(Level level) {
      Logger logger = LoggingUtils.getLogger();
      for(String key : targets.keySet()) {
         logger.log(level, " -> "+key);
      }
   }


   private void printCompilers(String processor, Level level) {
      Logger logger = LoggingUtils.getLogger();
      Set<String> compilers = targets.get(processor);
      for(String compiler : compilers) {
         logger.log(level, " -> "+compiler);
      }
   }

   /**
    * INSTANCE VARIABLES
    */

   /**
    * Indexed by the target name, returns compiler names.
    */
   Map<String, Set<String>> targets;
   /**
    * Indexed by the full qualified name (e.g.: MicroBlaze.mb-gcc), returns
    * files.
    */
   Map<String, String> targetFiles;


}
