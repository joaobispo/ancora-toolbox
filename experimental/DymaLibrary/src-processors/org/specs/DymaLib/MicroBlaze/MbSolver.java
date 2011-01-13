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

package org.specs.DymaLib.MicroBlaze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.Solver;
import org.specs.DymaLib.Utils.VbiUtils;
import org.specs.DymaLib.VbiUtils.OperandIO;
import org.suikasoft.SharedLibrary.DataStructures.ArithmeticResult32;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.ArgumentsProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.OperationProperties;
import org.suikasoft.SharedLibrary.OperationUtils;

/**
 *
 * @author Joao Bispo
 */
public class MbSolver implements Solver {

   public boolean solve(VeryBigInstruction32 vbi) {
      // Get Input/Output view
      OperandIO io = new OperandIO(vbi.originalOperands, vbi.supportOperands);

      // Get intputs
      //List<VbiOperand> baseInputs = VbiUtils.getInputs(vbi.originalOperands);
      //List<VbiOperand> additionalInputs = VbiUtils.getInputs(vbi.supportOperands);

      // Check if all inputs are constant
      boolean constantInputs = VbiUtils.areConstant(io.baseInputs) && VbiUtils.areConstant(io.additionalInputs);
      if(!constantInputs) {
         return false;
      }
      /*
      boolean areConstant1 = checkConstantInputs(vbi.originalOperands);
      boolean areConstant2 = checkConstantInputs(vbi.supportOperands);
      if(!areConstant1 || !areConstant2) {
         return false;
      }
*/
      // Get MbInstructionName
      MbInstructionName instName = (MbInstructionName) MbInstructionName.add.getEnum(vbi.op);
      if(instName == null) {
         LoggingUtils.getLogger().
                 warning("Could not find instruction '"+vbi.op+"'.");
         return false;
      }

      // Get Outputs
      //List<VbiOperand> baseOutputs = VbiUtils.getOutputs(vbi.originalOperands);
      //List<VbiOperand> additionalOutputs = VbiUtils.getOutputs(vbi.supportOperands);

      // Has constant inputs and we got the instruction name: solve it!
      // Get outputs
      //return solve(vbi, instName);
      boolean success = solve(io, instName);
      if(!success) {
         System.err.println("Problems in vbi:");
         System.err.println(vbi);
      }
      return success;
   }

/*
   private static boolean checkConstantInputs(List<VbiOperand> operands) {
      for(VbiOperand op : operands) {
         // Ignore outputs
         if(!op.isInput) {
            continue;
         }

         if(!op.isConstant) {
            return false;
         }
      }

      return true;
   }
*/

private boolean solve(OperandIO io, MbInstructionName mbInstructionName) {
   //private boolean solve(List<VbiOperand> baseInputs, List<VbiOperand> additionalInputs,
   //        List<VbiOperand> baseOutputs, List<VbiOperand> additionalInputs0, MbInstructionName mbInstructionName) {
   //private boolean solve(VeryBigInstruction32 vbi, MbInstructionName mbInstructionName) {
      // This might be done another way: use argument properties to extract the inputs
      // from the operation. Then give this inputs to an agnostic solver.

   if(OperationProperties.isAdd(mbInstructionName)) {
 //     if(InstructionProperties.ADD_INSTRUCTIONS.contains(mbInstructionName)) {
         //solveAdd(vbi, mbInstructionName);
         return solveAdd(io, mbInstructionName);
         //return true;
      }


   operationsNotSupported.add(mbInstructionName.getName());
      return true;
   }

   //private void solveAdd(VeryBigInstruction32 vbi, MbInstructionName mbInstructionName) {
   private boolean solveAdd(OperandIO io, MbInstructionName mbInstructionName) {
      List<Integer> inputs = MbSolverUtils.getInputs(io, mbInstructionName, 2);

      Integer carryInValue = MbSolverUtils.getCarryIn(io, mbInstructionName);

      ArithmeticResult32 result = OperationUtils.add32(inputs.get(0), inputs.get(1),
              carryInValue);

      MbSolverUtils.updateVbi(io, mbInstructionName, result);
      

      return true;

       // Get Inputs
       /*
      int[] inputs = new int[2];
      int counter = 0;
      for(VbiOperand op : vbi.originalOperands) {
         if(!op.isInput) {
            continue;
         }
         inputs[counter] = op.value;
         counter++;
      }
      
      if(counter != 2) {
         System.err.println("Found "+counter+" inputs instead of 2.");
      }
*/
     /*
      int carryValue = 0;
      if (usesCarryIn) {
         for (VbiOperand op : vbi.supportOperands) {
            if (!op.isInput) {
               continue;
            }
            carryValue = op.value;
            break;
         }
      }

      int result;
      if(usesCarryIn) {
         result = OperationUtils.add32(inputs[0], inputs[1], carryValue);
      } else {
         result = OperationUtils.add32(inputs[0], inputs[1]);
      }

     */
      /*
      int carryOutResult = 0;
      if(usesCarryOut) {
         carryOutResult = OperationUtils.getCarryOutAdd(inputs[0], inputs[1], carryValue);
      }

      // Replace output
      for (VbiOperand op : vbi.originalOperands) {
         if(op.isInput) {
            continue;
         }

         op.value = result;
         op.isConstant = true;
      }

      if (usesCarryOut) {
         for (VbiOperand op : vbi.supportOperands) {
            if (op.isInput) {
               continue;
            }

            op.value = carryOutResult;
            op.isConstant = true;
         }
      }
*/
   }


   public static final Set<String> operationsNotSupported = new HashSet<String>();






}
