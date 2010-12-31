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

package org.specs.AutoCompileV4.Job;

import java.util.Collection;
import org.ancora.SharedLibrary.EnumUtils;
import org.specs.AutoCompile.Job.*;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleChoiceEnum;


/**
 * Options available for targets
 *
 * @author Joao Bispo
 */
public enum JobOption implements EnumKey, MultipleChoiceEnum {

   //inttest(OptionType.integer),
   //booltest(OptionType.bool),
   OutputFolder(OptionType.string),
   SourcePath(OptionType.string),
   //inputFolderMode(OptionType.string),
   SourcePathMode(OptionType.multipleChoice),
   //processor(OptionType.string),
   //compiler(OptionType.string),
   CompilerFlags(OptionType.multipleChoiceStringList),
   OptimizationFlags(OptionType.multipleChoiceStringList),
   StackSize(OptionType.string);
/*
   inttest("inttest", OptionType.integer),
   booltest("booltest", OptionType.bool),
   outputFolder("outputFolder", OptionType.string),
   sourceFilesFolder("sourceFilesFolder", OptionType.string),
   inputFolderMode("inputFolderMode", OptionType.string),
   processor("processor", OptionType.string),
   compiler("compiler", OptionType.string),
   compilerFlags("compilerFlags", OptionType.stringList),
   optimizationFlags("optimizationLevels", OptionType.stringList);
*/

   private JobOption(OptionType type) {
   //private JobOption(String name, OptionType type) {
      //this.name = name;
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }

   public Collection<String> getChoices() {
      switch(this) {
         case SourcePathMode:
            return EnumUtils.buildListToString(SourceMode.values());
         case OptimizationFlags:
            return  EnumUtils.buildListToString(OptimizationFlag.values());
         case CompilerFlags:
            return EnumUtils.buildListToString(MbGccFlag.values());
         default:
            return null;
      }
   }

   public enum InputFolderModeChoices {

   }

   //final private String name;
   final private OptionType type;



   //final private static String prefix = "job.";
}
