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

import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleChoice;
import org.ancora.SharedLibrary.AppBase.AppUtils;

/**
 * Options available for targets
 *
 * @author Joao Bispo
 */
public enum JobOption implements AppOptionEnum, AppOptionMultipleChoice {

   //inttest(AppValueType.integer),
   //booltest(AppValueType.bool),
   OutputFolder(AppValueType.string),
   SourcePath(AppValueType.string),
   //inputFolderMode(AppValueType.string),
   SourcePathMode(AppValueType.multipleChoice),
   //processor(AppValueType.string),
   //compiler(AppValueType.string),
   CompilerFlags(AppValueType.multipleChoiceStringList),
   OptimizationFlags(AppValueType.multipleChoiceStringList),
   StackSize(AppValueType.string);
/*
   inttest("inttest", AppValueType.integer),
   booltest("booltest", AppValueType.bool),
   outputFolder("outputFolder", AppValueType.string),
   sourceFilesFolder("sourceFilesFolder", AppValueType.string),
   inputFolderMode("inputFolderMode", AppValueType.string),
   processor("processor", AppValueType.string),
   compiler("compiler", AppValueType.string),
   compilerFlags("compilerFlags", AppValueType.stringList),
   optimizationFlags("optimizationLevels", AppValueType.stringList);
*/

   private JobOption(AppValueType type) {
   //private JobOption(String name, AppValueType type) {
      //this.name = name;
      this.type = type;
   }

   public String getName() {
      return AppUtils.buildEnumName(this);
      //return prefix + name;
   }

   public AppValueType getType() {
      return type;
   }

   public Enum[] getChoices() {
      switch(this) {
         case SourcePathMode:
            return SourceMode.values();
         case OptimizationFlags:
            return  OptimizationFlag.values();
         case CompilerFlags:
            return MbGccFlag.values();
         default:
            return null;
      }
   }

   public enum InputFolderModeChoices {

   }

   //final private String name;
   final private AppValueType type;



   //final private static String prefix = "job.";
}
