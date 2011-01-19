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

import java.util.Arrays;
import java.util.Collection;
import org.specs.DymaLib.MicroBlaze.MbWeightsSetup;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleSetupEnum;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;
import org.specs.DymaLib.Vbi.Optimization.OptimizersList;
import org.suikasoft.Jani.Base.BaseUtils;

/**
 * Bridge between MbLoopAnalyser and Jani API.
 *
 * @author Joao Bispo
 */
public enum MbLoopAnalysisSetup implements EnumKey, MultipleSetupEnum, SingleSetupEnum {

   MbWeights(OptionType.integratedSetup),
   Optimizations(OptionType.setupList);


   public Collection<SingleSetupEnum> getSetups() {
      if(this == Optimizations) {
         return Arrays.asList((SingleSetupEnum[]) OptimizersList.values());
      }

      return null;
   }

   private MbLoopAnalysisSetup(OptionType optionType) {
      this.optionType = optionType;
   }

   public Collection<EnumKey> getSetupOptions() {
      if(this == MbWeights) {
         return BaseUtils.extractEnumValues(MbWeightsSetup.class);
      }

      return null;
   }

   public OptionType getType() {
      return optionType;
   }

   private final OptionType optionType;

}
