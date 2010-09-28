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

package org.specs.AutoCompile.Targets;

import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppUtils;

/**
 * Options available for targets
 *
 * @author Joao Bispo
 */
public enum TargetOption implements AppOptionEnum {

   target(AppValueType.string),
   compiler(AppValueType.string),
   optimizationFlags(AppValueType.stringList),
   inputExtensions(AppValueType.stringList),
   outputFlag(AppValueType.string),
   launchCommand(AppValueType.string),
   outputExtension(AppValueType.string);

   /*
   target("target", AppValueType.string),
   compiler("compiler", AppValueType.string),
   optimizationFlags("optimizationFlags", AppValueType.stringList),
   inputExtensions("inputExtensions", AppValueType.stringList),
   outputFlag("outputFlag", AppValueType.string),
   launchCommand("launchCommand", AppValueType.string),
   outputExtension("outputExtension", AppValueType.string);
    *
    */

   //private TargetOption(String name, AppValueType type) {
   private TargetOption(AppValueType type) {
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

   //final private String name;
   final private AppValueType type;

   //final private static String prefix = "targetOption.";
}
