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

import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.specs.DymaLib.Vbi.VbiUtils;
import org.specs.DymaLib.Vbi.VbiOperandIOView;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;

/**
 *
 * @author Joao Bispo
 */
public class ConstantLoadsRemoval implements VbiOptimizer {

   public ConstantLoadsRemoval() {
      hasLiteralLoads = false;
      hasStores = false;
   }



   public void optimize(VeryBigInstruction32 vbi) {
      MbInstructionName instName = (MbInstructionName) MbInstructionName.add.getEnum(vbi.op);
      // Check for literal loads
      if(InstructionProperties.LOAD_INSTRUCTIONS.contains(instName)) {
         VbiOperandIOView io = new VbiOperandIOView(vbi.originalOperands, vbi.supportOperands);
         if(VbiUtils.areConstant(io.baseInputs) && VbiUtils.areConstant(io.additionalInputs)) {
            hasLiteralLoads = true;
         }
      }

      if(InstructionProperties.STORE_INSTRUCTIONS.contains(instName)) {
         hasStores = true;
      }


   }

   public void close() {
      System.out.println("Has literal loads:"+hasLiteralLoads);
      System.out.println("Has stores:"+hasStores);
      /*
      if(hasLiteralLoads && !hasStores) {
         System.out.println("Has Literal Loads and no stores");
      }
       * 
       */
   }

   private boolean hasLiteralLoads;
   private boolean hasStores;
}
