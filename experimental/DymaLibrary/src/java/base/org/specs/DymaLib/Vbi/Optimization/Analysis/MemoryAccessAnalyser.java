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

package org.specs.DymaLib.Vbi.Optimization.Analysis;

import org.specs.DymaLib.Vbi.Optimization.VbiOptimizer;
import org.specs.DymaLib.Vbi.VbiOperandIOView;
import org.specs.DymaLib.Vbi.VbiUtils;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;

/**
 * Shows memory instructions and properties about them.
 *
 * @author Joao Bispo
 */
public class MemoryAccessAnalyser implements VbiOptimizer {

   public void optimize(VeryBigInstruction32 vbi) {
            MbInstructionName instName = (MbInstructionName) MbInstructionName.add.getEnum(vbi.op);

      if (InstructionProperties.LOAD_INSTRUCTIONS.contains(instName)) {
         show(vbi);
      }

      if (InstructionProperties.STORE_INSTRUCTIONS.contains(instName)) {
         show(vbi);
      }
   }

   private void show(VeryBigInstruction32 vbi) {
      StringBuilder builder = new StringBuilder();

      VbiOperandIOView io = new VbiOperandIOView(vbi.originalOperands, vbi.supportOperands);
      // Name
      builder.append(vbi.op);
      // Inputs
      builder.append(" ");
      builder.append(io.baseInputs);
      if(!io.additionalInputs.isEmpty()) {
         System.err.println("Addit. In not empty");
      }

      // Inputs
      builder.append(" ");
      builder.append(io.baseOutputs);
      if(!io.additionalOutputs.isEmpty()) {
         System.err.println("Addit. Out not empty");
      }

      System.out.println(builder);
   }


}
