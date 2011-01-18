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

import java.util.List;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.Solver;
import org.specs.DymaLib.Utils.VbiUtils;
import org.specs.DymaLib.VbiUtils.OperandIO;
import org.suikasoft.SharedLibrary.DataStructures.AccumulatorMap;
import org.suikasoft.SharedLibrary.DataStructures.ArithmeticResult32;
import org.suikasoft.SharedLibrary.LoggingUtils;
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


      // Check if all inputs are constant
      boolean constantInputs = VbiUtils.areConstant(io.baseInputs) && VbiUtils.areConstant(io.additionalInputs);
      if(!constantInputs) {
         return false;
      }

      // Get MbInstructionName
      MbInstructionName instName = (MbInstructionName) MbInstructionName.add.getEnum(vbi.op);
      if(instName == null) {
         LoggingUtils.getLogger().
                 warning("Could not find instruction '"+vbi.op+"'.");
         return false;
      }

      // Has constant inputs and we got the instruction name: solve it!
      boolean success = solve(io, instName, vbi);

      return success;
   }

   private boolean solve(OperandIO io, MbInstructionName mbInstructionName,
           VeryBigInstruction32 vbi) {
      // This might be done another way: use argument properties to extract the inputs
      // from the operation. Then give this inputs to an agnostic solver.

      if (OperationProperties.isAdd(mbInstructionName)) {
         return solveAdd(io, mbInstructionName);
      }

      if (OperationProperties.isRsub(mbInstructionName)) {
         return solveRsub(io, mbInstructionName);
      }

      if (OperationProperties.isBinaryLogical(mbInstructionName)) {
         return solveBinaryLogical(io, mbInstructionName);
      }

      if (OperationProperties.isUnaryLogical(mbInstructionName)) {
         return solveUnaryLogical(io, mbInstructionName);
      }

      if(OperationProperties.isJump(mbInstructionName)) {
         return solveJump(io, vbi);
      }

      
      if(OperationProperties.isLoad(mbInstructionName)) {
         return solveLoad(io, vbi);
      }
        
       

      operationsNotSupported.add(mbInstructionName.getName());
      return false;
   }

   private boolean solveAdd(OperandIO io, MbInstructionName mbInstructionName) {
      List<Integer> inputs = MbSolverUtils.getInputs(io, mbInstructionName, 2);
      Integer carryInValue = MbSolverUtils.getCarryIn(io, mbInstructionName);
      if(carryInValue == null) {
         carryInValue = OperationUtils.CARRY_NEUTRAL_ADD;
      }

      ArithmeticResult32 result = OperationUtils.add32(inputs.get(0), inputs.get(1),
              carryInValue);
      MbSolverUtils.updateVbi(io, mbInstructionName, result);
      
      return true;
   }

   private boolean solveRsub(OperandIO io, MbInstructionName mbInstructionName) {
      List<Integer> inputs = MbSolverUtils.getInputs(io, mbInstructionName, 2);
      Integer carryInValue = MbSolverUtils.getCarryIn(io, mbInstructionName);
      if(carryInValue == null) {
         carryInValue = OperationUtils.CARRY_NEUTRAL_SUB;
      }

      ArithmeticResult32 result = OperationUtils.rsub32(inputs.get(0), inputs.get(1),
              carryInValue);
      MbSolverUtils.updateVbi(io, mbInstructionName, result);
      
      return true;
   }

   private boolean solveBinaryLogical(OperandIO io, MbInstructionName mbInstructionName) {
      List<Integer> inputs = MbSolverUtils.getInputs(io, mbInstructionName, 2);

      int result = MbSolverUtils.solveBinaryLogic(inputs, mbInstructionName);
      MbSolverUtils.updateVbi(io, result);

      return true;
   }

   private boolean solveUnaryLogical(OperandIO io, MbInstructionName mbInstructionName) {
      List<Integer> inputs = MbSolverUtils.getInputs(io, mbInstructionName, 1);
      Integer carryInValue = MbSolverUtils.getCarryIn(io, mbInstructionName);
      
      ArithmeticResult32 result = MbSolverUtils.solveUnaryLogic(inputs, mbInstructionName, carryInValue);
      MbSolverUtils.updateVbi(io, mbInstructionName, result);

      return true;
   }

   /**
    * If jump has literal inputs, it can be automatically removed. Just check
    * if it has outputs which need to be updated.
    * 
    * @param io
    * @param mbInstructionName
    * @param mbInstructionName
    * @return
    */
   private boolean solveJump(OperandIO io, VeryBigInstruction32 vbi) {
      if(!io.baseOutputs.isEmpty()) {
         io.baseOutputs.get(0).value = vbi.address;
         io.baseOutputs.get(0).isConstant = true;         
      }

      // Check for cases I'm not taking into accound
      if(io.baseOutputs.size() > 1 || !io.additionalOutputs.isEmpty()) {
         System.err.println("Jump has outputs which where not considered:"+vbi+". Check if this is done right.");
         return false;
      }

      return true;
   }

   private boolean solveLoad(OperandIO io, VeryBigInstruction32 vbi) {
      // This optimization can only be applyed if there are no stores in the loop
      if(vbi.loopHasStores) {
         return false;
      }

      // Load has constant inputs; solver should read the memory position
      // of this instruction and substitute. For now, just mark instruction
      // as constant and put a value
      int dummyValue = 450;

      io.baseOutputs.get(0).value = dummyValue;
      io.baseOutputs.get(0).isConstant = true;
      //throw new UnsupportedOperationException("Not yet implemented");
      return true;
   }

   public static void reset() {
      operationsNotSupported = new AccumulatorMap<String>();
   }

   //public static final Map<String, Integer> operationsNotSupported = new HashSet<String>();
   public static AccumulatorMap<String> operationsNotSupported = new AccumulatorMap<String>();








}
