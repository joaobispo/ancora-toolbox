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

package org.specs.DymaLib.MicroBlaze.Vbi;

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.Vbi.VbiOperand;
import org.specs.DymaLib.Vbi.VbiUtils;
import org.specs.DymaLib.Vbi.VbiOperandIOView;
import org.suikasoft.SharedLibrary.BitUtils;
import org.suikasoft.SharedLibrary.DataStructures.ArithmeticResult32;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.ArgumentsProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.OperationUtils;

/**
 * Utility methods used by MbSolver.
 *
 * @author Joao Bispo
 */
public class MbSolverUtils {

   public static boolean checkBaseIOs(VbiOperandIOView io, MbInstructionName mbInstructionName) {
      // Check inputs
      int foundReads= io.baseInputs.size();
      int expectedReads = ArgumentsProperties.getNumReads(mbInstructionName);
      if(foundReads != expectedReads) {
         LoggingUtils.getLogger().
                 warning("Found "+foundReads+" reads, expected "+
                 expectedReads+" reads on instruction "+mbInstructionName+".");
         return false;
      }

      // Check outputs
      int foundWrites= io.baseOutputs.size();
      int expectedWrites = ArgumentsProperties.getNumWrites(mbInstructionName);
      if(foundWrites != expectedWrites) {
         LoggingUtils.getLogger().
                 warning("Found "+foundWrites+" writes, expected "+
                 expectedWrites+" writes on instruction "+mbInstructionName+".");
         return false;
      }

      return true;
   }

   public  static List<Integer> getInputs(VbiOperandIOView io, MbInstructionName mbInstructionName, int numInputs) {
      boolean success = checkBaseIOs(io, mbInstructionName);
      if (!success) {
         return null;
      }

      List<Integer> inputs = new ArrayList<Integer>(2);

      for(int i=0; i<numInputs; i++) {
         Integer value = io.baseInputs.get(i).value;
         if(value == null) {
            LoggingUtils.getLogger().
                    warning("A Constant Register has null value. Setting arbitrary value.");
            value = 1;

         }
         inputs.add(value);
      }

      return inputs;
   }

   public static Integer getCarryIn(VbiOperandIOView io, MbInstructionName mbInstructionName) {
      boolean usesCarryIn = CarryProperties.usesCarryIn(mbInstructionName);

      // Get carry in value
      if(!usesCarryIn) {
         //return OperationUtils.CARRY_NEUTRAL_ADD;
         return null;
      } else {
         return io.additionalInputs.get(0).value;
      }
   }

   public static void updateVbi(VbiOperandIOView io, MbInstructionName mbInstructionName, ArithmeticResult32 result) {
      // Update VBI
      io.baseOutputs.get(0).value = result.result;
      io.baseOutputs.get(0).isConstant = true;

      boolean usesCarryOut = CarryProperties.usesCarryOut(mbInstructionName);
      if (usesCarryOut) {
         io.additionalOutputs.get(0).value = result.carryOut;
         io.additionalOutputs.get(0).isConstant = true;
      }

      checkConstant(io.baseOutputs, "Base outputs");
      checkConstant(io.additionalOutputs, "Additional outputs");
      /*
      if(!VbiUtils.areConstant(io.baseOutputs)) {
         System.err.println("Base outputs not constant:");
         System.out.println(io.baseOutputs);
      }

      if(!VbiUtils.areConstant(io.additionalOutputs)) {
         System.err.println("Aditional outputs not constant:");
         System.out.println(io.additionalOutputs);
      }
       * 
       */
   }

   static int solveBinaryLogic(List<Integer> inputs, MbInstructionName mbInstructionName) {
      int input1 = inputs.get(0);
      int input2 = inputs.get(1);

      switch(mbInstructionName) {
         case and:
            return OperationUtils.and32(input1, input2);
         case andi:
            return OperationUtils.and32(input1, input2);
         case andn:
            return OperationUtils.andNot32(input1, input2);
         case andni:
            return OperationUtils.andNot32(input1, input2);
         case or:
            return OperationUtils.or32(input1, input2);
         case ori:
            return OperationUtils.or32(input1, input2);
         case xor:
            return OperationUtils.xor32(input1, input2);
         case xori:
            return OperationUtils.xor32(input1, input2);
         case cmp:
            return OperationUtils.mbCompareSigned(input1, input2);
         case cmpu:
            return OperationUtils.mbCompareUnsigned(input1, input2);
         case bsll:
            return OperationUtils.shiftLeftLogical(input1, input2);
         case bslli:
            return OperationUtils.shiftLeftLogical(input1, input2);
         case bsra:
            return OperationUtils.shiftRightArithmetical(input1, input2);
         case bsrai:
            return OperationUtils.shiftRightArithmetical(input1, input2);
         case bsrl:
            return OperationUtils.shiftRightLogical(input1, input2);
         case bsrli:
            return OperationUtils.shiftRightLogical(input1, input2);
         default:
            LoggingUtils.getLogger().
                    warning("Case not defined:"+mbInstructionName);
            return -1;
      }
   }

   public static ArithmeticResult32 solveUnaryLogic(List<Integer> inputs,
           MbInstructionName mbInstructionName, Integer carryInValue) {
      int input1 = inputs.get(0);

      // Get MSB
      Integer msb = null;
      switch (mbInstructionName) {
         case sra:
            msb = BitUtils.getBit(31, input1);
            break;
         case srl:
            msb = 0;
            break;
         case src:
            msb = carryInValue;
            break;
         default:
            LoggingUtils.getLogger().
                    warning("Case '" + mbInstructionName + "' not defined");
            break;
      }
      if (msb == null) {
         System.err.println("Error solving unary logic.");
         return null;
      }

      // Shift value right by 1
      int newValue = input1 >> 1;
      // Set/Clear the most significant bit
      if (msb == 0) {
         newValue = BitUtils.clearBit(31, newValue);
      } else {
         newValue = BitUtils.setBit(31, newValue);
      }

      // Get discarded bit from original input
      int carryOut = BitUtils.getBit(0, input1);

      return new ArithmeticResult32(newValue, carryOut);
   }

   /**
    * Updates the first argument of baseOutput. Checks if all outputs are
    * constant.
    * 
    * @param io
    * @param mbInstructionName
    */
   public static void updateVbi(VbiOperandIOView io, int result) {
      // Update VBI
      io.baseOutputs.get(0).value = result;
      io.baseOutputs.get(0).isConstant = true;

      checkConstant(io.baseOutputs, "Base outputs");
      checkConstant(io.additionalOutputs, "Additional outputs");
   }

   private static void checkConstant(List<VbiOperand> baseOutputs, String string) {
      if(!VbiUtils.areConstant(baseOutputs)) {
         System.err.println(string+" not constant:");
         System.out.println(baseOutputs);
      }
   }



}
