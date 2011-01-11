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

package org.specs.DymaLib.DataStructures;

import java.util.List;

/**
 *
 * @author Joao Bispo
 */
public class VeryBigInstruction32 {

   //public VeryBigInstruction32(int address, String op, List<Operand> originalOperands,
   //        List<Operand> supportOperands, boolean isMappable) {
   public VeryBigInstruction32(int address, String op, List<VbiOperand> originalOperands,
           List<VbiOperand> supportOperands, boolean isMappable) {
      this.address = address;
      this.op = op;
      this.originalOperands = originalOperands;
      this.supportOperands = supportOperands;
      this.isMappable = isMappable;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      if (isMappable) {
         builder.append("M");
      } else {
         builder.append("!M");
      }


      builder.append("(");
      builder.append(address);
      builder.append(")");
      builder.append(op);
      builder.append("|");

      for(VbiOperand vbiOp : originalOperands) {
         builder.append(vbiOp);
         builder.append("|");
      }

      for(VbiOperand vbiOp : supportOperands) {
         builder.append(vbiOp);
         builder.append("|");
      }

      return builder.toString();
   }



   public Integer address;
   public String op;
   //public List<Operand> originalOperands;
   //public List<Operand> supportOperands;
   public List<VbiOperand> originalOperands;
   public List<VbiOperand> supportOperands;
   // FLAGS
   public boolean isMappable;
/*
   public class Operand {

      public Operand(String id, Integer value, boolean isInput, boolean isRegister, boolean isConstant,
              boolean isLiveIn, boolean isLiveOut, boolean isAuxiliarOperand) {
         this.id = id;
         this.value = value;
         this.isInput = isInput;
         this.isRegister = isRegister;
         this.isConstant = isConstant;
         this.isLiveIn = isLiveIn;
         this.isLiveOut = isLiveOut;
         this.isAuxiliarOperand = isAuxiliarOperand;
      }


      public String id;
      public Integer value;
      public boolean isInput;
      public boolean isRegister;
      public boolean isConstant;
      public boolean isLiveIn;
      public boolean isLiveOut;
      public boolean isAuxiliarOperand;
   }
 *
 */
}
