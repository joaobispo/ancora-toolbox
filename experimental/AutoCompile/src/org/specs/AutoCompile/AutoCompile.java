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
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Options.Config;
import org.specs.AutoCompile.Options.TargetOption;

/**
 * Automates compilation of benchmarks.
 *
 * @author Joao Bispo
 */
public class AutoCompile implements App {

   public AutoCompile() {
   }


   /**
    *
    * @param options
    * @return
    */
   public int execute(Map<String, AppOption> options) {
      // Build target tables
      String targetFolder = AppUtils.getString(options, Config.targetFolder);
      buildTargetTables(targetFolder);

      // Parse Command Line
      /*
      List<String> args = AppUtils.getStringList(options, Config.arguments);
      Map<String, AppOption> jobOptions = parseArguments(args);

      if(jobOptions == null) {
         LoggingUtils.getLogger().
                 warning("Could not find a job.");
         return 1;
      }
       * 
       */

      // Perform job
      String jobFilepath = AppUtils.getString(options, Config.jobFile);
      Map<String,AppOption> jobOptions = AppUtils.parseFile(new File(jobFilepath));

      

      System.out.println(options);
      System.out.println(targets);
      System.out.println(targetFiles);
      System.out.println(jobOptions);
      return 0;
   }

   /**
    * INSTANCE VARIABLES
    */
   private void buildTargetTables(String targetFolderpath) {
      targets = new HashMap<String, Set<String>>();
      targetFiles = new HashMap<String, String>();

      // Read target folder and load targets
      File targetFolder = IoUtils.safeFolder(targetFolderpath);
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

          addTargetCompiler(target, compiler, tFile.getPath());
      }
   }


   private void addTargetCompiler(String target, String compiler, String path) {
      Set<String> compilerNames = targets.get(target);
      if(compilerNames == null) {
         compilerNames = new HashSet<String>();
         targets.put(target, compilerNames);
      }

      if(!compilerNames.add(compiler)) {
         LoggingUtils.getLogger().
                 warning("Duplicate entry for target '"+target+"' - compiler '"+compiler+"'.");
      }

      targetFiles.put(buildTargetCompilerName(target, compiler), path);
   }

   private String buildTargetCompilerName(String target, String compiler) {
      return target + "." + compiler;
   }


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
