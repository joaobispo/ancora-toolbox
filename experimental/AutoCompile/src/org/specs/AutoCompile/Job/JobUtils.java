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

package org.specs.AutoCompile.Job;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppOption;
import org.ancora.SharedLibrary.AppBase.Extra.AppUtils;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Options.JobOption;
import org.specs.AutoCompile.Options.TargetOption;

/**
 * Utility methods related to job options.
 *
 * @author Joao Bispo
 */
public class JobUtils {

   public static List<Job> buildJobs(Map<String,AppOption> jobOptions,
           Map<String,AppOption> targetOptions) {

      List<Job> jobs = new ArrayList<Job>();

      // Get program executable
      String programExecutable = AppUtils.getString(targetOptions, TargetOption.launchCommand);
      // Get output flag
      String outputFlag = AppUtils.getString(targetOptions, TargetOption.outputFlag);

      // Get compiler flags
      List<String> otherFlags = AppUtils.getStringList(jobOptions, JobOption.compilerFlags);

      // Get list of programs to compile
      List<ProgramSource> sources = getProgramSources(jobOptions, targetOptions);
      if(sources == null) {
         LoggingUtils.getLogger().
                 warning("Could not obtain source files.");
         return null;
      }

      // Get optimization flags
      List<String> optimizationFlags = getOptimizationFlags(jobOptions, targetOptions);
      if(optimizationFlags == null) {
         LoggingUtils.getLogger().
                 warning("Optimization flags list is null.");
         return null;
      }

      // Get output folder
      String outputFoldername = AppUtils.getString(jobOptions, JobOption.outputFolder);
      File outputFolder = IoUtils.safeFolder(outputFoldername);
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder '"+outputFoldername+"'.");
         return null;
      }

      // Build jobs
      for(ProgramSource source : sources) {
         for(String optimizationFlag : optimizationFlags) {
            // Get output folder
            File outputOptFolder = getOptimizationFolder(outputFolder, optimizationFlag);

            // Get complete path for output file
            
            File outputFile = new File(outputOptFolder, buildOutputfilename(source, optimizationFlag, targetOptions));
            String outputFilename = "\""+outputFile.getPath()+"\"";
            

            List<String> inputFiles = source.getSourceFilenames();
            String workingDir = source.getSourceFolder();

            Job job = new Job(programExecutable, outputFilename, inputFiles, optimizationFlag, otherFlags,outputFlag, workingDir);
            jobs.add(job);
         }
      }

      return jobs;
   }

   private static List<ProgramSource> getProgramSources(Map<String, AppOption> jobOptions, Map<String, AppOption> targetOptions) {
      // Get mode
      String modeString = AppUtils.getString(jobOptions, JobOption.inputFolderMode);
      SourceMode mode = EnumUtils.valueOf(SourceMode.class, modeString);
      if(mode == null) {
         LoggingUtils.getLogger().
                 warning("Specified mode '"+modeString+"' not available.");
         printAvailableModes();
         return null;
      }

      if(mode == SourceMode.folder) {
         return getSourcesFolderMode(jobOptions, targetOptions);
      }

      if(mode == SourceMode.singleFile) {
         return getSourcesFileMode(jobOptions, targetOptions);
      }

      LoggingUtils.getLogger().
              warning("Case for mode '"+mode+"' not defined.");
      return null;
   }

   /**
    * Extracts and verifies the optimization flags.
    *
    * @param jobOptions
    * @param targetOptions
    * @return
    */
   private static List<String> getOptimizationFlags(Map<String, AppOption> jobOptions, Map<String, AppOption> targetOptions) {
      // Get available optimizations
      List<String> availableOpts = AppUtils.getStringList(targetOptions, TargetOption.optimizationFlags);
      // Get job optimizations
      List<String> jobOpts = AppUtils.getStringList(jobOptions, JobOption.optimizationFlags);

      List<String> parsedOpts = new ArrayList<String>();

      boolean optError = false;
      for(String jobOpt : jobOpts) {
         // Check if optimization is valid
         if(!availableOpts.contains(jobOpt)) {
            LoggingUtils.getLogger().
                    warning("Optimization flag '"+jobOpt+"' not available for this target.");
            optError = true;
            continue;
         }
         parsedOpts.add(jobOpt);
      }

      if(optError) {
         printAvailableOpt(availableOpts);
      }
      return parsedOpts;
   }

   /**
    * Builds the /output/Optimization/ folder.
    *
    * @param outputFolder
    * @param optimizationFlag
    * @return
    */
   private static File getOptimizationFolder(File outputFolder, String optimizationFlag) {
      // Replace '-' character, in case it is inside.
      // Ideally, should be any non-alphanumerical character.
      String optimizationFoldername = optimizationFlag.replaceAll("-", "");
      File optimizationFolder = new File(outputFolder, optimizationFoldername);
      optimizationFolder = IoUtils.safeFolder(optimizationFolder.getPath());

      if(optimizationFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder '"
                 + optimizationFolder.getPath() + "'.");
         return null;
      }

      /*
      optimizationFolder.mkdirs();
      if (!optimizationFolder.isDirectory()) {
         LoggingUtils.getLogger().
                 warning("Could not open folder '"
                 + optimizationFolder.getPath() + "'.");
         return null;
      }
       *
       */
      

      return optimizationFolder;
   }

   private static String buildOutputfilename(ProgramSource source, String optimizationFlag, Map<String, AppOption> targetOptions) {
      String outputExtension = AppUtils.getString(targetOptions, TargetOption.outputExtension);
      return source.getBaseOutputFilename() + optimizationFlag
              + IoUtils.DEFAULT_EXTENSION_SEPARATOR + outputExtension;
   }

   private static void printAvailableOpt(List<String> availableOpts) {
      Logger logger = LoggingUtils.getLogger();
      logger.info("Available optimizations for this target:");
      for(String opt : availableOpts) {
         logger.info(" -> "+opt);
      }
   }

   private static void printAvailableModes() {
      Logger logger = LoggingUtils.getLogger();
      logger.info("Available modes:");
      for(SourceMode mode : SourceMode.values()) {
         logger.info(" -> "+mode.name());
      }
   }

   private static List<ProgramSource> getSourcesFolderMode(Map<String, AppOption> jobOptions, Map<String, AppOption> targetOptions) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   /**
    * Inside the source folder, each .c file is a program.
    * 
    * @param jobOptions
    * @param targetOptions
    * @return
    */
   private static List<ProgramSource> getSourcesFileMode(Map<String, AppOption> jobOptions, Map<String, AppOption> targetOptions) {
      // Get extensions
      List<String> extensions = AppUtils.getStringList(targetOptions, TargetOption.inputExtensions);
      // Get source folder
      String sourceFoldername = AppUtils.getString(jobOptions, JobOption.sourceFilesFolder);
      File sourceFolder = new File(sourceFoldername);
      if (!sourceFolder.isDirectory()) {
         LoggingUtils.getLogger().
                 warning("Could not open '" + sourceFolder.getPath() + "' as folder.");
         return null;
      }

      // Get sources
      List<File> files = IoUtils.getFilesRecursive(sourceFolder, new HashSet<String>(extensions));

      // Each file is a program
      List<ProgramSource> programSources = new ArrayList<ProgramSource>();
      for (File file : files) {
         List<String> sourceFilenames = new ArrayList<String>();
         sourceFilenames.add(file.getName());
         
         String baseFilename = IoUtils.removeExtension(file.getName());
         programSources.add(new ProgramSource(sourceFilenames, sourceFoldername, baseFilename));
      }


      return programSources;
   }



}
