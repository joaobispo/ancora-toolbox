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

import java.util.List;

/**
 * Represents the files and root folder of a set of files for compilation.
 *
 * @author Joao Bispo
 */
public class ProgramSource {

   public ProgramSource(List<String> sourceFiles, String sourceFolder, String outputFilename) {
      this.sourceFilenames = sourceFiles;
      this.sourceFolder = sourceFolder;
      this.baseOutputFilename = outputFilename;
   }

   public List<String> getSourceFilenames() {
      return sourceFilenames;
   }

   public String getSourceFolder() {
      return sourceFolder;
   }

   public String getBaseOutputFilename() {
      return baseOutputFilename;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private List<String> sourceFilenames;
   private String sourceFolder;
   private String baseOutputFilename;
}
