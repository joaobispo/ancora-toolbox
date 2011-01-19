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

package org.specs.LoopOptimization.V2;

import java.util.Collection;
import org.specs.DToolPlus.Config.SystemOptionsV4;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockOptionsV4;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;

/**
 * Bridge between MbSimulator and Jani API.
 *
 * @author Joao Bispo
 */
public enum MbSimulatorSetup implements EnumKey, SingleSetupEnum {

   IterationThreshold(OptionType.integer),
   //MegaBlockSetup(OptionType.setup),
   MegaBlockSetup(OptionType.integratedSetup),
   SystemSetup(OptionType.setup);

   private MbSimulatorSetup(OptionType optionType) {
      this.optionType = optionType;
   }

   public OptionType getType() {
      return optionType;
   }

   private final OptionType optionType;

   public Collection<EnumKey> getSetupOptions() {
      if(this == MegaBlockSetup) {
         return BaseUtils.extractEnumValues(MegaBlockOptionsV4.class);
      }

      if(this == SystemSetup) {
         return BaseUtils.extractEnumValues(SystemOptionsV4.class);
      }

      return null;
   }
}
