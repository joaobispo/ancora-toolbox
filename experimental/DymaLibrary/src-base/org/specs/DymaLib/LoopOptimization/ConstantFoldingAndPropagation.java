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

package org.specs.DymaLib.LoopOptimization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.DataStructures.VbiOperand;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.Solver;
import org.specs.DymaLib.Vbi.VbiOptimizer;
import org.suikasoft.SharedLibrary.LoggingUtils;

/**
 * Performs constant folding and propagation over VBIs.
 *
 * @author Joao Bispo
 */
public class ConstantFoldingAndPropagation implements VbiOptimizer {

   public ConstantFoldingAndPropagation(Solver solver) {
      this.solver = solver;
      
      registerConstantValues = new HashMap<String, Integer>();
   }



   public void optimize(VeryBigInstruction32 vbi) {
      // Ignore unmappable instructions
      if(!vbi.isMappable) {
         return;
      }

      substituteInputs(vbi.originalOperands);
      substituteInputs(vbi.supportOperands);

      removeOutputsFromTable(vbi.originalOperands);
      removeOutputsFromTable(vbi.supportOperands);
      
      boolean wasSolved = solver.solve(vbi);
      
      if(!wasSolved) {
         return;
      }
      
      // Update table with solved outputs
      updateOutputs(vbi.originalOperands);
      updateOutputs(vbi.supportOperands);
      
      // If instruction has no side-effects, mark as unmappable
      if(!vbi.hasSideEffects) {
         vbi.isMappable = false;
      }
   }


   private void substituteInputs(List<VbiOperand> operands) {
      for(VbiOperand op : operands) {
         substituteInput(op);
      }
   }

   private void substituteInput(VbiOperand op) {
      // Check if input
      if (!op.isInput) {
         return;
      }

      // Check if is it mutable
      if(op.isConstant) {
         return;
      }

      // Check if it is a there is a constant value for it
      Integer value = registerConstantValues.get(op.id);
      if(value == null) {
         return;
      }

      // Found constant value for this register. Change it
      op.isConstant = true;
      op.value = value;
   }

   private void removeOutputsFromTable(List<VbiOperand> operands) {
      for (VbiOperand op : operands) {
         removeOutputFromTable(op);
      }
   }

   /**
    * Removes from the table any references to the outputs of the given operation.
    *
    * <p> An operand is removed from the table by inserting a 'null' as its value.
    * When checking if there is a mapping between an operand name and another operand,
    * instead of 'containsKey', one should get the operand associated with the key
    * and test for null.
    *
    * @param op
    */
   private void removeOutputFromTable(VbiOperand op) {
      // Check if output
      if (op.isInput) {
         return;
      }

      // Check if is it mutable
      if(op.isConstant) {
         return;
      }

      // Check if it is a there is a constant value for it
      Integer value = registerConstantValues.get(op.id);
      if(value == null) {
         return;
      }

      //registerConstantValues.remove(op.id);
      registerConstantValues.put(op.id, null);
   }

   private void updateOutputs(List<VbiOperand> operands) {
      for (VbiOperand op : operands) {
         updateOutput(op);
      }
   }

   private void updateOutput(VbiOperand op) {
      // Check if output
      if (op.isInput) {
         return;
      }

      // Check if is it constant
      if(!op.isConstant) {
         LoggingUtils.getLogger().
                 warning("Solved VBI has mutable outputs! ");
         return;
      }

      registerConstantValues.put(op.id, op.value);
   }

   private final Map<String, Integer> registerConstantValues;
   private final Solver solver;






}
