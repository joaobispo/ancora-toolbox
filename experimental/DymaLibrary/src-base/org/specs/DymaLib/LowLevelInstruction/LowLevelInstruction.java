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

package org.specs.DymaLib.LowLevelInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a low-level instruction.
 *
 * @author Joao Bispo
 */
public class LowLevelInstruction {

   public LowLevelInstruction() {
      operands = new ArrayList<Operand>(3);
      carries = new ArrayList<Carry>(2);

      for(int i=0; i<NUM_MAX_OPERANDS; i++) {
         operands.add(new Operand());
      }

      carries.add(new Carry());
      carries.add(new Carry());
   }



   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private int op;
   private List<Operand> operands;
   private List<Carry> carries;

   public final static int NUM_MAX_OPERANDS = 3;
   public final static int OPERAND_1_INDEX = 0;
   public final static int OPERAND_2_INDEX = 1;
   public final static int OPERAND_3_INDEX = 2;

   public final static int CARRY_IN_INDEX = 0;
   public final static int CARRY_OUT_INDEX = 1;
}
