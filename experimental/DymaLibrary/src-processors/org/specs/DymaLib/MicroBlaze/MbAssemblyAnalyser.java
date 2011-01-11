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
import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.DataStructures.CodeSegment;
import org.specs.DymaLib.DataStructures.ConstantRegister;
import org.specs.DymaLib.DataStructures.LiveOut;
import org.specs.DymaLib.StraighLineLoops.AssemblyAnalyser;
import org.specs.DymaLib.Utils.LivenessAnalyser;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MicroBlazeParser;
import org.suikasoft.SharedLibrary.MicroBlaze.RegisterName;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 *
 * @author Joao Bispo
 */
public class MbAssemblyAnalyser implements AssemblyAnalyser {

  

   private MbAssemblyAnalyser(Collection<LiveOut> liveOuts, Collection<ConstantRegister> constantRegisters) {
      this.liveOuts = liveOuts;
      this.constantRegisters = constantRegisters;
   }



   //public MbAssemblyAnalyser(List<Integer> addresses, List<String> instructions) {
      //this.instructions = MicroBlazeParser.getMbInstructions(addresses, instructions);
   //}

   public static MbAssemblyAnalyser create(RegisterTable registerTable, List<MbInstruction> mbInstructions) {
      // Transform into MbInstructions
      //List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
      //        sllLoop.getAddresses(), sllLoop.getInstructions());

      LivenessAnalyser livenessAnalyser = createAnalyser(mbInstructions);

      

      // Extract data for building MbAnalyser
       Collection<LiveOut> liveouts = livenessAnalyser.getLiveOuts();
       //Collection<ConstantRegister> constantRegisters = getConstantRegisters(sllLoop, livenessAnalyser);
       Collection<ConstantRegister> constantRegisters = getConstantRegisters(registerTable, livenessAnalyser);

 
      MbAssemblyAnalyser mbAssAnal = new MbAssemblyAnalyser(liveouts, constantRegisters);

      return mbAssAnal;
   }


   public static void extractInfo(MbInstruction mbInst, List<String> operandIds, List<Boolean> isInput, List<Boolean> isConstant) {
      // Extract info from MbInst operands
      for(MbOperand mbOperand : mbInst.getOperands()) {
         extractInfo(mbOperand, operandIds, isInput, isConstant);
      }
      
      // Extract info from carries
      // Carry In
      if(CarryProperties.usesCarryIn(mbInst.getInstructionName())) {
         operandIds.add(RegisterName.getCarryFlagName());
         isInput.add(Boolean.TRUE);
         isConstant.add(Boolean.FALSE);
      }

      // Carry Out
      if(CarryProperties.usesCarryOut(mbInst.getInstructionName())) {
         operandIds.add(RegisterName.getCarryFlagName());
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


   public static LivenessAnalyser createAnalyser(List<MbInstruction> mbInstructions) {
      // Use LivenessAnalyser
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

   public static Collection<ConstantRegister> getConstantRegisters(RegisterTable registerIdTable, LivenessAnalyser livenessAnalyser) {
      // Check if CodeSegment has the values of the registers
      if (registerIdTable == null) {
         return null;
      }

     return livenessAnalyser.getConstantRegisters(registerIdTable);

   }

   public Collection<LiveOut> getLiveOuts() {
      return liveOuts;
   }

   public Collection<ConstantRegister> getConstantRegisters() {
      return  constantRegisters;
   }

   private Collection<LiveOut> liveOuts;
   private Collection<ConstantRegister> constantRegisters;

   //private List<MbInstruction> instructions;
}
