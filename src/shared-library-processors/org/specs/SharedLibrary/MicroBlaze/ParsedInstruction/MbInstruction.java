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

package org.specs.SharedLibrary.MicroBlaze.ParsedInstruction;

import java.util.List;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;

/**
 * Represents a Microblaze instruction.
 *
 * @author Joao Bispo
 */
public class MbInstruction {

   public MbInstruction(int address, InstructionName instructionName, List<MbOperand> operands) {
      this.address = address;
      this.instructionName = instructionName;
      this.operands = operands;
   }

   

   public int getAddress() {
      return address;
   }

   public InstructionName getInstructionName() {
      return instructionName;
   }

   public List<MbOperand> getOperands() {
      return operands;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append(instructionName);
      for(MbOperand operand : operands) {
         builder.append(" ");
         builder.append(operand);
      }

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private InstructionName instructionName;
   private List<MbOperand> operands;
}
