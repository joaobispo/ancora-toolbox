/*
 *  Copyright 2011 SuikaSoft.
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

package org.specs.LoopOptimization;

import java.util.Collection;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleChoiceEnum;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.SharedLibrary.EnumUtils;

/**
 *
 * @author Joao Bispo
 */
public enum LoopOptimizationOptions implements EnumKey, MultipleChoiceEnum {

   InputPath(OptionType.string),
   PathType(OptionType.multipleChoice),
   OutputFolder(OptionType.string),
   PropertiesFileWithInstructionCycles(OptionType.string);

   private LoopOptimizationOptions(OptionType optionType) {
      this.optionType = optionType;
   }


   public OptionType getType() {
      return optionType;
   }

   private final OptionType optionType;

   public Collection<String> getChoices() {
      if(this == PathType) {
         return EnumUtils.buildListToString(InputType.values());
      }

      return null;
   }

}
