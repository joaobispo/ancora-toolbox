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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.DataStructures.ConstantRegister;
import org.specs.DymaLib.DataStructures.LiveOut;
import org.specs.DymaLib.DataStructures.VbiOperand;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.StraighLineLoops.VbiParser;
import org.suikasoft.SharedLibrary.BitUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionName;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand;
import org.suikasoft.SharedLibrary.MicroBlaze.RegisterName;

/**
 *
 * @author Joao Bispo
 */
public class MbVbiParser implements VbiParser {

   public MbVbiParser(Collection<LiveOut> liveOuts, Collection<ConstantRegister> constantRegisters) {
      this.liveOuts = new ArrayList<LiveOut>(liveOuts);
      this.constantRegisters = new ArrayList<ConstantRegister>(constantRegisters);
      liveoutsIndexes = buildLiveoutsMap(this.liveOuts);
      constantRegistersIndexes = buildConstRegMap(this.constantRegisters);

      counter = 0;
      immValue = null;
      immValueCounter = null;
      writtenRegisters = new HashSet<String>();
   }



   //public VeryBigInstruction32 parseInstruction(int address, String instruction) {
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
      if(handleSpecialCases(instruction)) {
         //return null;
         vbi32 = null;
      } else {
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
      VeryBigInstruction32 vbi32 = new VeryBigInstruction32(address, op,
              originalOperands, supportOperands, isMappable);

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
         //Integer value = getOperandValue(mbOperand);
      
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
      //boolean isRegister = mbOperand.getType() == MbOperand.Type.register;
      boolean isRegister = type == MbOperand.Type.register;

      if (isRegister) {

         // Check if register is constant
         //Integer index = constantRegistersIndexes.get(mbOperand.getId());
         Integer index = constantRegistersIndexes.get(id);
         if (index != null) {
            return constantRegisters.get(index).value;
         }

         // Register is not constant, does not have a value
         return null;
      }

      // Assume it is an immediate
      // Check if there is an upper 16-bit immediate value
      if (immValue == null) {
         //return mbOperand.getValue();
         return rawValue;
      }

      //int completeValue = BitUtils.fuseImm(immValue, mbOperand.getValue());
      int completeValue = BitUtils.fuseImm(immValue, rawValue);
      // Check immValue counter
      if (immValueCounter + 1 != counter) {
         LoggingUtils.getLogger().
                 warning("Imm line (" + immValueCounter + ") more than one instruction "
                 + "ahead of current instruction (" + counter + ")");
      }

      // Update immValue
      immValue = null;
      immValueCounter = null;

      return completeValue;
   }

   //private boolean isConstant(String id, MbOperand.Type typeMbOperand mbOperand) {
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

   //private boolean isLiveOut(MbOperand mbOperand) {
   private boolean isLiveOut(String id) {
//      Integer index = liveoutsIndexes.get(mbOperand.getId());
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
      String id = RegisterName.getCarryFlagName();

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
      boolean isMbNop = checkMbNop(instruction);
      // Is Nop? Not mappable
      if(isMbNop) {
         return false;
      }

      return true;
   }

   /**
    * 
    * @param instruction
    * @return true if it is a MicroBlaze nop
    */
    private boolean checkMbNop(MbInstruction instruction) {
      if(instruction.getInstructionName() != InstructionName.or) {
         return false;
      }

         
       for (int i = 0; i < instruction.getOperands().size(); i++) {
          MbOperand operand = instruction.getOperands().get(i);

          // If it is not constant, is not nop
          if (!MbOperand.isConstant(operand.getType())) {
             return false;
          }

          // If value is not 0, is not nop
          if (operand.getValue() != 0) {
             return false;
          }
       }
      

      // Every operand is constant and 0, and operation is OR
      // It is a nop
      return true;
   }

   private boolean handleSpecialCases(MbInstruction mbInst) {
      if(mbInst.getInstructionName() == InstructionName.imm) {
         // Check current Imm
         if(immValue != null) {
            LoggingUtils.getLogger().
                    warning("Overwritting IMM value '"+immValue+"'");
         }

         // Store value
         immValue = mbInst.getOperands().get(0).getValue();
         immValueCounter = counter;
         return true;
      }

      return false;
   }

      //private Map<String, Integer> buildConstRegMap(Collection<ConstantRegister> constantRegisters) {
   private Map<String, Integer> buildConstRegMap(List<ConstantRegister> constantRegisters) {
      Map<String, Integer> newMap = new HashMap<String, Integer>();
      int indexCounter = 0;
      //Iterator<ConstantRegister> iter = constantRegisters.iterator();
//      while(iter.hasNext()) {
//         newMap.put(iter.next().id, indexCounter);
      for (ConstantRegister reg : constantRegisters) {
         newMap.put(reg.id, indexCounter);
         indexCounter++;
      }

      return newMap;
   }

   private Map<String, Integer> buildLiveoutsMap(List<LiveOut> liveOuts) {
      Map<String, Integer> newMap = new HashMap<String, Integer>();
      int indexCounter = 0;
      for (LiveOut liveout : liveOuts) {
         newMap.put(liveout.id, indexCounter);
         indexCounter++;
      }

      return newMap;
   }

   private List<LiveOut> liveOuts;
   private List<ConstantRegister> constantRegisters;
   // Maps the names of the registers to positions in the collection
   private Map<String, Integer> constantRegistersIndexes;
   private Map<String, Integer> liveoutsIndexes;
   private int counter;

   private Set<String> writtenRegisters;

   private Integer immValue;
   private Integer immValueCounter;








}
