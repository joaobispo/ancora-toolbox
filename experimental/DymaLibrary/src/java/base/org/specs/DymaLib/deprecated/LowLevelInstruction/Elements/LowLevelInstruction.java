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

package org.specs.DymaLib.deprecated.LowLevelInstruction.Elements;

import java.util.List;
import org.specs.DymaLib.deprecated.LowLevelInstruction.LliUtils;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;

/**
 * Represents a low-level instruction. Works as a "structure".
 *
 * @author Joao Bispo
 */
public class LowLevelInstruction {

   //public LowLevelInstruction(int mappable, int address, int op, Operand[] operands, Carry[] carries) {
//   public LowLevelInstruction(InstructionFlags flags, int address, int op, Operand[] operands, Carry[] carries) {
//   public LowLevelInstruction(InstructionFlags flags, int address, int op, Operand[] operands) {
   public LowLevelInstruction(InstructionFlags flags, int address, int op, List<Operand> operands) {
      //this.mappable = mappable;
      this.flags = flags;
      this.address = address;
      this.op = op;
      this.operands = operands;
      //this.carries = carries;
   }

   /**
    * For now, it decodes to MicroBlaze instructions as default
    * @return
    */
   @Override
   public String toString() {
      return toString(InstructionName.values());
   }



   public String toString(Enum[] values) {
      StringBuilder builder = new StringBuilder();

      //if(mappable == ENABLED) {
      if(flags.mappable == LliUtils.ENABLED) {
         builder.append("M");
      } else {
         builder.append("m");
      }
      builder.append("|");
      builder.append(address);
      builder.append("|");
      builder.append(values[op]);
      //for(int i=0; i<operands.length; i++) {
      for(int i=0; i<operands.size(); i++) {
         builder.append("|");
         //builder.append(operands[i].toString());
         builder.append(operands.get(i).toString());
      }

      /*
      for(int i=0; i<2; i++) {
         if(carries[i].enabled == ENABLED) {
            builder.append("|");
            if(i == 0) {
               builder.append("CarryIn");
            } else {
               builder.append("CarryOut");
            }

            if(carries[i].immutable == ENABLED) {
               builder.append(":");
               builder.append(carries[i].value);
            }
            
         }
      }
       * 
       */

      return builder.toString();
   }




   /**
    * INSTANCE VARIABLES
    */


   /**
    *
    */
   //public int mappable;
   public int address;
   public int op;
//   public Operand[] operands;
   public List<Operand> operands;
   //public Carry[] carries;
   public InstructionFlags flags;

   //public final static int NUM_MAX_OPERANDS = 3;

   //public final static int CARRY_IN_INDEX = 0;
   //public final static int CARRY_OUT_INDEX = 1;

   //public final static int ENABLED = 1;
   //public final static int DISABLED = 0;
}
