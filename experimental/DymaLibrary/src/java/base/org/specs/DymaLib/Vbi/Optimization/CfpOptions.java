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

package org.specs.DymaLib.Vbi.Optimization;

import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;

/**
 *
 * @author Joao Bispo
 */
public enum CfpOptions implements EnumKey {

   SolveArithmeticAndLogic(OptionType.bool),
   SolveLoads(OptionType.bool),
   SolveBranches(OptionType.bool);

   private CfpOptions(OptionType optionType) {
      this.optionType = optionType;
   }


   public OptionType getType() {
      return optionType;
   }

   private final OptionType optionType;
}
