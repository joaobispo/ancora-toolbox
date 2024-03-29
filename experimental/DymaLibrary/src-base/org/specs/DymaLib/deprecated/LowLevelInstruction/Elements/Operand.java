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

import org.specs.DymaLib.deprecated.LowLevelInstruction.LliUtils;

/**
 * Represents an Operand.
 *
 * @author Joao Bispo
 */
public class Operand {

   //public Operand(int value, int type, int flow, int isImmutable, Integer isLiveIn) {
   public Operand(String value, int type, int flow, int isImmutable, Integer isLiveIn) {
   //public Operand(String id, Integer value, int type, int flow, int isImmutable, Integer isLiveIn) {
//      this.id = id;
      this.value = value;
      this.type = type;
      this.flow = flow;
      this.isConstant = isImmutable;
      this.isLiveIn = isLiveIn;
   }


   

   /**
    * Builds an inactive operand. 
    * 
    * @return
    */
   /*
   public static Operand newInactiveOperand() {

   }
    *
    */


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      if(type != TYPE_LITERAL) {
         if(isConstant == LliUtils.ENABLED) {
            builder.append("(i)");
         }
         builder.append("r");
      }
      builder.append(value);

      builder.append("(");
      if(flow == FLOW_INPUT) {
         builder.append("i");
      } else {
         builder.append("o");
      }
      builder.append(")");
      
      return builder.toString();
   }

   /**
    * INSTANCE VARIABLES
    */
   //public int value;
   //public String id;
   public String value;
   //public Integer value;
   public int type;
   public int flow;
   public int isConstant;
   public Integer isLiveIn;

   public final static int TYPE_REGISTER = 0;
   public final static int TYPE_LITERAL = 1;
   //public final static int TYPE_INTERNAL_DATA = 2;
   
   public final static int FLOW_INPUT = 0;
   public final static int FLOW_OUTPUT = 1;
}
