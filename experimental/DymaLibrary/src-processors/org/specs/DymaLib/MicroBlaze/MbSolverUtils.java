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
import java.util.List;
import org.specs.DymaLib.VbiUtils.OperandIO;
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

   public static boolean checkBaseIOs(OperandIO io, MbInstructionName mbInstructionName) {
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

   public  static List<Integer> getInputs(OperandIO io, MbInstructionName mbInstructionName, int numInputs) {
      boolean success = checkBaseIOs(io, mbInstructionName);
      if (!success) {
         return null;
      }

      List<Integer> inputs = new ArrayList<Integer>(2);

      for(int i=0; i<numInputs; i++) {
         Integer value = io.baseInputs.get(i).value;
         inputs.add(value);
      }

      return inputs;
   }

   public static Integer getCarryIn(OperandIO io, MbInstructionName mbInstructionName) {
      boolean usesCarryIn = CarryProperties.usesCarryIn(mbInstructionName);

      // Get carry in value
      if(!usesCarryIn) {
         return OperationUtils.CARRY_NEUTRAL_ADD;
      } else {
         return io.additionalInputs.get(0).value;
      }
   }

   public static void updateVbi(OperandIO io, MbInstructionName mbInstructionName, ArithmeticResult32 result) {
      // Update VBI
      io.baseOutputs.get(0).value = result.result;
      io.baseOutputs.get(0).isConstant = true;

      boolean usesCarryOut = CarryProperties.usesCarryOut(mbInstructionName);
      if (usesCarryOut) {
         io.additionalOutputs.get(0).value = result.carryOut;
         io.additionalOutputs.get(0).isConstant = true;
      }
   }
}
