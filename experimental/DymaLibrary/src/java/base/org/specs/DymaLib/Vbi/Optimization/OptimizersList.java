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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.Vbi.Optimization.Analysis.MemoryAccessAnalyser;
import org.specs.DymaLib.Vbi.Utils.Solver;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;
import org.suikasoft.SharedLibrary.LoggingUtils;

/**
 * List of supported optimizations for use with OptionType.setupList.
 *
 * @author Joao Bispo
 */
public enum OptimizersList implements SingleSetupEnum {

   ConstantFoldingAndPropagation(BaseUtils.extractEnumValues(CfpOptions.class)),
   MemoryAccessAnalyser(new ArrayList<EnumKey>());

   private OptimizersList(List<EnumKey> options) {
      this.options = options;
   }

   public Collection<EnumKey> getSetupOptions() {
      return options;
   }
   
   public VbiOptimizer getOptimizer(List<Object> arguments) {
      
      if(this == ConstantFoldingAndPropagation) {
         Solver solver = (Solver) arguments.get(0);
         return new ConstantFoldingAndPropagation(solver);
      }

      if(this == MemoryAccessAnalyser) {
         return new MemoryAccessAnalyser();
      }
      
      LoggingUtils.getLogger().
              warning("Case not defined:"+this);
      return null;
   }

   private final List<EnumKey> options;
}
