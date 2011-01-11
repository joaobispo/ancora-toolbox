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

/**
 *
 * @author Joao Bispo
 */
public class VbiOperand {

   public VbiOperand(String id, Integer value, boolean isInput, boolean isRegister, boolean isConstant, boolean isLiveIn, boolean isLiveOut, boolean isAuxiliarOperand) {
      this.id = id;
      this.value = value;
      this.isInput = isInput;
      this.isRegister = isRegister;
      this.isConstant = isConstant;
      this.isLiveIn = isLiveIn;
      this.isLiveOut = isLiveOut;
      this.isAuxiliarOperand = isAuxiliarOperand;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append(id);

      if (isConstant) {
         builder.append("=");
         builder.append(value);
      }

      builder.append("(");
      if(isInput) {
         builder.append("I");
      } else {
         builder.append("O");
      }

      if(isLiveIn) {
         builder.append("Li");
      }
      if(isLiveOut) {
         builder.append("Lo");
      }
      builder.append(")");


      return builder.toString();
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
