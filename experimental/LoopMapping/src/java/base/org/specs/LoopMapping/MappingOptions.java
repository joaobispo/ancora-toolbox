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

package org.specs.LoopMapping;

import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleChoice;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType;

/**
 *
 * @author Joao Bispo
 */
public enum MappingOptions implements AppOptionEnum, AppOptionMultipleChoice {

   InputBlockFile(AppValueType.string),
   TypeOfInput(AppValueType.multipleChoice),
   OutputFolder(AppValueType.string);

   private MappingOptions(AppValueType type) {
      this.type = type;
   }


   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }

   final private AppValueType type;

   public Enum[] getChoices() {
      if(this == TypeOfInput) {
         return InputType.values();
      }
      return null;
   }
}
