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

package org.specs.GraphicalTraceV4;


import java.util.Collection;
import java.util.List;
import org.ancora.SharedLibrary.EnumUtils;
import org.specs.DToolPlus.Config.SystemOptionsV4;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleChoiceEnum;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;

/**
 * Options for this application.
 *
 * @author Joao Bispo
 */
public enum Options implements EnumKey, MultipleChoiceEnum, SingleSetupEnum {

   Input(OptionType.string),
   InputType(OptionType.multipleChoice),
   OutputFolder(OptionType.string),
   TraceUnit(OptionType.multipleChoice),
   SystemSetup(OptionType.setup);

   Options(OptionType type) {
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }

   private final OptionType type;

   public Collection<String> getChoices() {
      if(this == InputType) {
         return EnumUtils.buildListToString(org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType.values());
      }

      if(this == TraceUnit) {
         return EnumUtils.buildListToString(TraceUnits.values());
      }

      return null;
   }

   //public List<EnumKey> getSetupOptions() {
   public Collection<EnumKey> getSetupOptions() {
      if(this == SystemSetup) {
         return BaseUtils.extractEnumValues(SystemOptionsV4.class);
      }

      return null;
   }

}
