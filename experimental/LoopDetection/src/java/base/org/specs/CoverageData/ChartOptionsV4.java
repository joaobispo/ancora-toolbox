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

package org.specs.CoverageData;

import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;

/**
 *
 * @author Joao Bispo
 */
public enum ChartOptionsV4 implements EnumKey {

   SaveChart(OptionType.bool),
   DisplayChart(OptionType.bool),
   ChartName(OptionType.string),
   DrawingWidth(OptionType.integer),
   DrawingHeight(OptionType.integer),
   ScaleFactor(OptionType.integer);
   
   private ChartOptionsV4(OptionType type) {
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }
   private final OptionType type;

}
