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

package org.specs.DymaLib.MicroBlaze.Assembly;

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.Utils.LivenessAnalyser;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbRegisterId;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand;

/**
 * Utility methods related to liveness analysis of MicroBlaze assembly instructions.
 *
 * @author Joao Bispo
 */
public class MbLivenessUtils {

    public static void extractInfo(MbInstruction mbInst, List<String> operandIds, List<Boolean> isInput, List<Boolean> isConstant) {
      // Extract info from MbInst operands
      for(MbOperand mbOperand : mbInst.getOperands()) {
         extractInfo(mbOperand, operandIds, isInput, isConstant);
      }

      // Extract info from carries
      // Carry In
      if(CarryProperties.usesCarryIn(mbInst.getInstructionName())) {
         operandIds.add(MbRegisterId.getCarryFlagName());
         isInput.add(Boolean.TRUE);
         isConstant.add(Boolean.FALSE);
      }

      // Carry Out
      if(CarryProperties.usesCarryOut(mbInst.getInstructionName())) {
         operandIds.add(MbRegisterId.getCarryFlagName());
         isInput.add(Boolean.FALSE);
         isConstant.add(Boolean.FALSE);
      }

   }


   public static void extractInfo(MbOperand mbOperand, List<String> operandIds, List<Boolean> input, List<Boolean> constant) {
      String operandId = mbOperand.getId();
      Boolean isInput = MbOperand.isInput(mbOperand.getFlow());
      Boolean isConstant = MbOperand.isConstant(mbOperand.getType());

      operandIds.add(operandId);
      input.add(isInput);
      constant.add(isConstant);
   }


   //public static AssemblyAnalyser createAnalyser(List<MbInstruction> mbInstructions) {
   public static LivenessAnalyser createLivenessAnalyser(List<MbInstruction> mbInstructions) {
      // Use LivenessAnalyser
      //AssemblyAnalyser livenessAnalyser = new AssemblyAnalyser();
      LivenessAnalyser livenessAnalyser = new LivenessAnalyser();
      // Extract information from each mbInstruction and feed the information
      // to the analyser
      for(MbInstruction mbInst : mbInstructions) {
         List<String> operandIds = new ArrayList<String>();
         List<Boolean> isInput = new ArrayList<Boolean>();
         List<Boolean> isConstant = new ArrayList<Boolean>();
         extractInfo(mbInst, operandIds, isInput, isConstant);
         livenessAnalyser.next(operandIds, isInput, isConstant);
      }

      return livenessAnalyser;
   }
}
