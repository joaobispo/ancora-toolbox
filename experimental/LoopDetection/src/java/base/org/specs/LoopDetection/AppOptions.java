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

import java.util.Arrays;
import java.util.Collection;
import org.ancora.SharedLibrary.EnumUtils;
import org.specs.CoverageData.ChartOptionsV4;
import org.specs.DToolPlus.Config.SystemOptionsV4;
import org.specs.DymaLib.LoopDetection.LoopDetectorsV4;
import org.specs.DymaLib.Utils.LoopDiskWriter.DiskWriterOptionsV4;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleChoiceEnum;
import org.suikasoft.Jani.EnumKeyOptions.MultipleSetupEnum;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;

/**
 *
 * @author Joao Bispo
 */
//public enum AppOptions implements AppOptionEnum, AppOptionEnumSetup,
//        AppOptionMultipleSetup, AppOptionMultipleChoice {
public enum AppOptions implements EnumKey, SingleSetupEnum,
        MultipleSetupEnum, MultipleChoiceEnum {

   ProgramFileOrFolder(OptionType.string),
   InputType(OptionType.multipleChoice),
   OutputFolder(OptionType.string),
   WriteDotFilesForEachElfProgram(OptionType.bool),
   LoopWriterSetup(OptionType.setup),
   LoopDetector(OptionType.setupList),
   ChartSetup(OptionType.setup),
   SystemSetup(OptionType.setup);

   AppOptions(OptionType type) {
      this.type = type;
   }

   /*
   public String getName() {
      return AppUtils.buildEnumName(this);
   }
    *
    */

   public OptionType getType() {
      return type;
   }

   private final OptionType type;

//   public List<EnumKey> getSetupOptions() {
   public Collection<EnumKey> getSetupOptions() {
      if (this == SystemSetup) {
         return BaseUtils.extractEnumValues(SystemOptionsV4.class);
      }
      if (this == LoopWriterSetup) {
                    return BaseUtils.extractEnumValues(DiskWriterOptionsV4.class);
      }
      if (this == ChartSetup) {
                  return BaseUtils.extractEnumValues(ChartOptionsV4.class);
      }

      return null;
   }


   //public List<SingleSetupEnum> getSetups() {
   public Collection<SingleSetupEnum> getSetups() {
      if(this == LoopDetector) {
         return Arrays.asList((SingleSetupEnum[])LoopDetectorsV4.values());
      }

      return null;
   }

   public Collection<String> getChoices() {
      if (this == InputType) {
         return EnumUtils.buildListToString(org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType.values());
      }

      return null;
   }


}
