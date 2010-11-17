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

package org.specs.LoopDetection;

import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnumSetup;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleChoice;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleSetup;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.specs.DToolPlus.Config.SystemOptions;
import org.specs.DymaLib.LoopDetection.LoopDetectors;

/**
 *
 * @author Joao Bispo
 */
public enum AppOptions implements AppOptionEnum, AppOptionEnumSetup,
        AppOptionMultipleSetup, AppOptionMultipleChoice {

   ProgramFileOrFolder(AppValueType.string),
   InputType(AppValueType.multipleChoice),
   OutputFolder(AppValueType.string),
   IterationsThreshold(AppValueType.integer),
   LoopDetector(AppValueType.multipleSetupList),
   SystemSetup(AppValueType.multipleSetup);

   AppOptions(AppValueType type) {
      this.type = type;
   }

   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }

   private final AppValueType type;

   public AppOptionEnum[] getSetupOptions() {
      if(this == SystemSetup) {
         return SystemOptions.values();
      }

      return null;
   }

   public AppOptionEnumSetup[] getSetups() {
      if(this == LoopDetector) {
         return LoopDetectors.values();
      }

      return null;
   }

   public Enum[] getChoices() {
      if (this == InputType) {
         return org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType.values();
      }

      return null;
   }

}
