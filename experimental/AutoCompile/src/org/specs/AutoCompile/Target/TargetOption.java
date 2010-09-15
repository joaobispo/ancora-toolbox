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

package org.specs.AutoCompile.Target;

import org.ancora.SharedLibrary.AppBase.AppOptionType;
import org.ancora.SharedLibrary.AppBase.Extra.AppOptionEnum;

/**
 * Options available for targets
 *
 * @author Joao Bispo
 */
public enum TargetOption implements AppOptionEnum {

   target("target", AppOptionType.string),
   compiler("compiler", AppOptionType.string),
   optimizationFlags("optimizationFlags", AppOptionType.stringList),
   outputFlag("outputFlag", AppOptionType.string),
   launchCommand("launchCommand", AppOptionType.string),
   outputExtension("outputExtension", AppOptionType.string);

   private TargetOption(String name, AppOptionType type) {
      this.name = name;
      this.type = type;
   }

   public String getName() {
      return prefix + name;
   }

   public AppOptionType getType() {
      return type;
   }

   final private String name;
   final private AppOptionType type;

   final private static String prefix = "targetOption.";
}