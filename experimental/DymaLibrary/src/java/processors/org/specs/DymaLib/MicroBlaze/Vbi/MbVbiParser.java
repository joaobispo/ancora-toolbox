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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.PreAnalysis.AssemblyAnalysis;
import org.specs.DymaLib.PreAnalysis.ConstantRegister;
import org.specs.DymaLib.PreAnalysis.LiveOut;
import org.specs.DymaLib.Vbi.VbiOperand;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.specs.DymaLib.Vbi.Parser.VbiParser;
import org.specs.DymaLib.Vbi.Parser.VbiParserUtils;
import org.suikasoft.SharedLibrary.BitUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.Parsing.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.Parsing.MbOperand;
import org.suikasoft.SharedLibrary.MicroBlaze.MbRegisterId;

/**
 *
 * @author Joao Bispo
 */
public class MbVbiParser implements VbiParser {

   public MbVbiParser(AssemblyAnalysis asmData) {
      //this.liveOuts = new ArrayList<LiveOut>(asmData.liveOuts);
      this.liveOuts = new ArrayList<LiveOut>(asmData.livenessAnalysis.liveOuts);
       //this.constantRegisters = new ArrayList<ConstantRegister>(asmData.constantRegisters);
       this.constantRegisters = new ArrayList<ConstantRegister>(asmData.livenessAnalysis.constantRegisters);
      this.hasStores = asmData.hasStores;

      liveoutsIndexes = VbiParserUtils.buildLiveoutsMap(this.liveOuts);
      constantRegistersIndexes = VbiParserUtils.buildConstRegMap(this.constantRegisters);

      counter = 0;
      resetImm();
      writtenRegisters = new HashSet<String>();
   }


   public VeryBigInstruction32 parseInstruction(Object instruction) {
      return parseInstruction((MbInstruction) instruction);
   }

   /**
    *
    * @param instruction
    * @return a VBI if the given instruction could generate another
    * instruction, null otherwise. Some instructions, such as IMM, do not generate VBIs.
    */
   public VeryBigInstruction32 parseInstruction(MbInstruction instruction) {
      VeryBigInstruction32 vbi32;

      vbi32 = handleSpecialCases(instruction);
      if(vbi32 == null) {
         vbi32 = handleNormalCase(instruction);
      }
     
      // Increment instruction counter
      counter++;

      return vbi32;
   }

   private VeryBigInstruction32 handleNormalCase(MbInstruction instruction) {
      Integer address = instruction.getAddress();
      String op = instruction.getInstructionName().name();

      List<VbiOperand> originalOperands = getOriginalOperands(instruction);
      List<VbiOperand> supportOperands = getSupportOperands(instruction);

      // FLAGS
      boolean isMappable = isMappable(instruction);
      boolean hasSideEffects = instruction.hasSideEffects();
      VeryBigInstruction32 vbi32 = new VeryBigInstruction32(address, op,
              originalOperands, supportOperands, isMappable, hasSideEffects,
              hasStores);

      return vbi32;
   }

   /**
    * Converts a VeryBigInstruction back to a MicroBlaze instruction.
    * @param instruction
    * @return
    */
   public String parseVbi(VeryBigInstruction32 instruction) {
      throw new UnsupportedOperationException("Not supported yet.");
   }


   private List<VbiOperand> getOriginalOperands(MbInstruction instruction) {
      List<VbiOperand> originalOperands = new ArrayList<VbiOperand>();

      // Process inputs first
      for(MbOperand operand : instruction.getOperands()) {
         boolean processInputs = true;
         VbiOperand newOperand = buildOperand(operand, processInputs);

         if(newOperand == null) {
            continue;
         }

         originalOperands.add(newOperand);
      }
      
      // Process outputs now
      for(MbOperand operand : instruction.getOperands()) {
         boolean processInputs = false;
         VbiOperand newOperand = buildOperand(operand, processInputs);

         if(newOperand == null) {
            continue;
         }

         originalOperands.add(newOperand);
      }

      return originalOperands;
   }

   private VbiOperand buildOperand(MbOperand mbOperand, boolean processInputs) {
      boolean isInput = MbOperand.isInput(mbOperand.getFlow());
      if(isInput != processInputs) {
         return null;
      }

      // Check special case R0
      if(mbOperand.isR0()) {
         mbOperand = MbOperand.transformR0InImm0(mbOperand);
      }


      // Check if register and output
      if(!processInputs) {
         if(mbOperand.getType() == MbOperand.Type.register) {
            writtenRegisters.add(mbOperand.getId());
         }
      }


      String id = mbOperand.getId();
         // Only has value if it is an immediate, or if
         // is a constant register
         Integer value = getOperandValue(id, mbOperand.getType(), mbOperand.getValue());
      
      boolean isRegister = mbOperand.getType() == MbOperand.Type.register;
      boolean isConstant = isConstant(id, mbOperand.getType());
      boolean isLiveIn = isLiveIn(id, isRegister);
      boolean isLiveOut = isLiveOut(id);
      boolean isAuxiliarOperand = false;

      VbiOperand operand = new VbiOperand(id, value, isInput, isRegister, isConstant,
              isLiveIn, isLiveOut, isAuxiliarOperand);

      return operand;
   }

   /**
    * 
    * @param mbOperand
    * @return null if mutable register, an integer otherwise
    */
   //private Integer getOperandValue(MbOperand mbOperand) {
   private Integer getOperandValue(String id, MbOperand.Type type, Integer rawValue) {
      // Check if register
      boolean isRegister = type == MbOperand.Type.register;

      if (isRegister) {
         // Check if register is constant
         Integer index = constantRegistersIndexes.get(id);
         if (index != null) {
            return constantRegisters.get(index).value;
         }

         // Register is not constant, does not have a value
         return null;
      }

      // Assume it is an immediate
      // Check if there is an upper 16-bit immediate value
      Integer imm = getImm();
      if (imm == null) {
         return rawValue;
      }

      int completeValue = BitUtils.fuseImm(imm, rawValue);


      return completeValue;
   }

   private boolean isConstant(String id, MbOperand.Type type) {
      // Check if immediate
      if(type == MbOperand.Type.immediate) {
         return true;
      }

      // Check register
      Integer index = constantRegistersIndexes.get(id);
      if(index != null) {
         return true;
      }

      return false;
   }

   private boolean isLiveIn(String id, boolean isRegister) {
      // Only registers can be live ins
      if(!isRegister) {
         return false;
      }

      if(writtenRegisters.contains(id)) {
         return false;
      }

      return true;
   }

   private boolean isLiveOut(String id) {
      Integer index = liveoutsIndexes.get(id);
      if(index == null) {
         return false;
      }

      LiveOut liveout = liveOuts.get(index);
      if(liveout.instructionNumber != counter) {
         return false;
      }

      return true;
   }

   private List<VbiOperand> getSupportOperands(MbInstruction instruction) {
      List<VbiOperand> supportOperands = new ArrayList<VbiOperand>();

       // Carry In
      if(CarryProperties.usesCarryIn(instruction.getInstructionName())) {
         boolean isCarryIn = true;
         VbiOperand carryIn = buildCarry(isCarryIn);
         supportOperands.add(carryIn);
      }

      // Carry Out
      if(CarryProperties.usesCarryOut(instruction.getInstructionName())) {
         boolean isCarryIn = false;
         VbiOperand carryOut = buildCarry(isCarryIn);
         supportOperands.add(carryOut);
      }


      return supportOperands;
   }


   private VbiOperand buildCarry(boolean carryIn) {
      String id = MbRegisterId.getCarryFlagName();

      Integer value = getOperandValue(id, MbOperand.Type.register, null);
      boolean isInput = carryIn;
      boolean isRegister = true;
      boolean isConstant = isConstant(id, MbOperand.Type.register);
      boolean isLiveIn = isLiveIn(id, isRegister);
      boolean isLiveOut = isLiveOut(id);
      boolean isAuxiliarOperand = true;



      // If carry out, add id to written registers
      if(!carryIn) {
         writtenRegisters.add(id);
      }

      VbiOperand operand = new VbiOperand(id, value, isInput, isRegister, isConstant,
              isLiveIn, isLiveOut, isAuxiliarOperand);

      return operand;
   }

   /**
    * By default returns true, unless the instruction is some special case such
    * as a nop (OR R0, R0, R0)
    * @param instruction
    * @return
    */
   private boolean isMappable(MbInstruction instruction) {
      // Check for OR 0, 0, 0 (at this point, R0 should have been converted to 
      // immediate 0.
      //boolean isMbNop = checkMbNop(instruction);
      boolean isMbNop = instruction.isMbNop();
      // Is Nop? Not mappable
      if(isMbNop) {
         return false;
      }

      return true;
   }

   private VeryBigInstruction32 handleSpecialCases(MbInstruction mbInst) {
      if(mbInst.getInstructionName() == MbInstructionName.imm) {
         return buildImmInstruction(mbInst);
      }

      return null;
   }

   private VeryBigInstruction32 buildImmInstruction(MbInstruction mbInst) {

      int address = mbInst.getAddress();
      String op = mbInst.getInstructionName().getName();
      List<VbiOperand> originalOperands = getOriginalOperands(mbInst);

      boolean isMappable = false;
      boolean hasSideEffects = mbInst.hasSideEffects();

      VeryBigInstruction32 vbi32 = new VeryBigInstruction32(address, op, originalOperands,
              new ArrayList<VbiOperand>(), isMappable, hasSideEffects, hasStores);

      // Store value
      updateImm(mbInst.getOperands().get(0).getValue());

      
      return vbi32;
   }

   private Integer getImm() {
      if(immValue == null) {
         return null;
      }

      // Check immValue counter
      if (immValueCounter + 1 != counter) {
         LoggingUtils.getLogger().
                 warning("Imm line (" + immValueCounter + ") more than one instruction "
                 + "ahead of current instruction (" + counter + ")");
      }

      Integer imm = immValue;
      resetImm();

      return imm;
   }

   /**
    * If value is null, counter is put to null also.
    * 
    * @param value
    */
   private void updateImm(Integer value) {
      if(value == null) {
         System.err.println("Do not use null values here.");
         return;
      }

      // Check current Imm
      if (immValue != null) {
         LoggingUtils.getLogger().
                 warning("Overwritting IMM value '" + immValue + "'");
      }
      
      immValue = value;
      immValueCounter = counter;
   }

   private void resetImm() {
      immValue = null;
      immValueCounter = null;
   }

   private List<LiveOut> liveOuts;
   private List<ConstantRegister> constantRegisters;
   private boolean hasStores;

   // Maps the names of the registers to positions in the collection
   private Map<String, Integer> constantRegistersIndexes;
   private Map<String, Integer> liveoutsIndexes;
   private int counter;

   private Set<String> writtenRegisters;

   private Integer immValue;
   private Integer immValueCounter;

}
