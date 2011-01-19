/*
 *  Copyright 2011 SPeCS Research Group.
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

package org.specs.DymaLib.MicroBlaze;

import java.util.HashMap;
import java.util.Map;
import org.specs.DymaLib.Weights.WeightTable;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;

/**
 *
 * @author Joao Bispo
 */
public enum MbWeightsSetup implements EnumKey {

   LoadCycles(OptionType.integer),
   StoreCycles(OptionType.integer),
   OtherCycles(OptionType.integer);

   private MbWeightsSetup(OptionType optionType) {
      this.optionType = optionType;
   }

   public OptionType getType() {
      return optionType;
   }

   /**
    * Builds a WeightTable object from a MbWeightsSetup
    * @param mbWeightSetup
    * @return
    */
   public static WeightTable buildTable(Setup mbWeightsSetup) {
      // Get default cycles
      int defaultCycles = BaseUtils.getInteger(mbWeightsSetup.get(OtherCycles));

      // Build table with default values
      Map<String, Integer> weights = new HashMap<String, Integer>();
      for(MbInstructionName instName : MbInstructionName.values()) {
         weights.put(instName.getName(), defaultCycles);
      }

      // Add load cycles
      int loadCycles = BaseUtils.getInteger(mbWeightsSetup.get(LoadCycles));
      for(MbInstructionName instName : InstructionProperties.LOAD_INSTRUCTIONS) {
         weights.put(instName.getName(), loadCycles);
      }

      // Add store cycles
      int storeCycles = BaseUtils.getInteger(mbWeightsSetup.get(StoreCycles));
      for(MbInstructionName instName : InstructionProperties.STORE_INSTRUCTIONS) {
         weights.put(instName.getName(), storeCycles);
      }
      

      return new WeightTable(weights, defaultCycles);
   }

   private final OptionType optionType;
}
