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

package org.specs.DymaLib.MicroBlaze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.LowLevelInstruction.Elements.InstructionFlags;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.Elements.Operand;
import org.specs.DymaLib.LowLevelInstruction.LliUtils;
import org.specs.DymaLib.LowLevelInstruction.LowLevelParser;
import org.specs.SharedLibrary.MicroBlaze.CarryProperties;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;
import org.specs.SharedLibrary.MicroBlaze.InstructionProperties;
import org.specs.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.specs.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand;
import org.specs.SharedLibrary.MicroBlaze.ParsedInstruction.MicroBlazeParser;
import org.specs.SharedLibrary.MicroBlaze.RegisterName;

/**
 *
 * @author Joao Bispo
 */
public class MbLowLevelParser implements LowLevelParser{

   public MbLowLevelParser() {
      mbParser = new MicroBlazeParser();
      instructions = new ArrayList<LowLevelInstruction>();
      immValue = null;
      writtenRegisters = new HashSet<String>();
   }



   public void nextInstruction(int address, String instruction) {
      mbParser.nextInstruction(address, instruction);
      List<MbInstruction> mbInsts = mbParser.getAndClearUnits();
      for(MbInstruction mbInst : mbInsts) {
         addMbInstruction(mbInst);
      }


   }

    private void addMbInstruction(MbInstruction mbInst) {
      // Check for special instruction IMM, which does not build an instruction
      boolean specialCase = handleSpecialCases(mbInst);
      if(specialCase) {
         return;
      }

      int address = mbInst.getAddress();
      //int isMappable = LowLevelInstruction.ENABLED;
      int op = mbInst.getInstructionName().ordinal();

      InstructionFlags flags = getFlags(mbInst);


      // Build operands
      //Operand[] operands = buildOperands(mbInst);
      List<Operand> operands = buildOperands(mbInst);

      // Build carries
      //Carry[] carries = buildCarries(mbInst);
      operands.addAll(buildCarries(mbInst));

      // Add live-in status
      setLiveInStatus(operands);

      LowLevelInstruction newInst =
//              new LowLevelInstruction(isMappable, address, op, operands, carries);
              //new LowLevelInstruction(flags, address, op, operands, carries);
              new LowLevelInstruction(flags, address, op, operands);

      // Check for special cases, such as OR R0, R0, R0
      handleSpecialCases(newInst);

      // Add new inst to list
      instructions.add(newInst);
       
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
         return true;
      }

      return false;
   }

   private void handleSpecialCases(LowLevelInstruction newInst) {
      // Check for OR 0, 0, 0
      if(newInst.op == InstructionName.or.ordinal()) {
         boolean isNop = true;
//         for(int i=0; i<newInst.operands.length; i++) {
         for(int i=0; i<newInst.operands.size(); i++) {
            //if(newInst.operands[i].type != Operand.TYPE_LITERAL) {
            if(newInst.operands.get(i).type != Operand.TYPE_LITERAL) {
               isNop = false;
            }

            //if(newInst.operands[i].value != 0) {
            //if(newInst.operands.get(i).value != 0) {
            if(!newInst.operands.get(i).value.equals("0")) {
               isNop = false;
            }

            // Indicate to not map this instruction
            if(isNop) {
               //newInst.mappable = LowLevelInstruction.DISABLED;
               newInst.flags.mappable = LliUtils.DISABLED;
            }
         }

         return;
      }

   }

   private void handleSpecialCases(Operand operand) {
      // Check for R0
//      if(operand.value == 0) {
      if(operand.value.equals("0")) {
         if(operand.type == Operand.TYPE_REGISTER) {
            // Transform operand into literal 0
            operand.isConstant = LliUtils.ENABLED;
            operand.type = Operand.TYPE_LITERAL;
         }
         return;
      }
   }

//   private Operand[] buildOperands(MbInstruction mbInst) {
   private List<Operand> buildOperands(MbInstruction mbInst) {
      List<MbOperand> mbOperands = mbInst.getOperands();

      //Operand[] operands = new Operand[mbOperands.size()];

      List<Operand> operands = new ArrayList<Operand>();



      //for(int i=0; i<operands.length; i++) {
      //for(int i=0; i<operands.size(); i++) {
      for(int i=0; i<mbOperands.size(); i++) {
         MbOperand mbOperand = mbOperands.get(i);

         // Check type
         // Immediate
         Integer type = null;
         Integer immutable = null;
         //Integer value = null;
         String value = null;
         Integer flow = null;
         Integer isLivein = null;

         if(mbOperand.getType() == MbOperand.Type.immediate) {
            type = Operand.TYPE_LITERAL;
            immutable = LliUtils.ENABLED;
            // Check if there is 16-bit immediate value
            int completeValue;
            if(immValue == null) {
               //value = mbOperand.getValue();
               completeValue = mbOperand.getValue();
            } else {
               completeValue = BitUtils.fuseImm(immValue, mbOperand.getValue());
               //value =
               immValue = null;
            }
            value = Integer.toString(completeValue);
         }

         // Register
         else {
            type = Operand.TYPE_REGISTER;
            immutable = LliUtils.DISABLED;
            //value = mbOperand.getValue();
            //value = LliUtils.intToRegister(mbOperand.getValue());
            int registerIndex = mbOperand.getValue();
            value = (RegisterName.values())[registerIndex].getRegisterName();
         }

         if(mbOperand.getFlow() == MbOperand.Flow.in) {
            flow = Operand.FLOW_INPUT;
         } else {
            flow = Operand.FLOW_OUTPUT;
         }

         isLivein = LliUtils.DISABLED;

         //operands[i] = new Operand(value, type, flow, immutable, null);
         Operand newOperand = new Operand(value, type, flow, immutable, isLivein);
         operands.add(newOperand);

         // Check for special cases such as R0
         //handleSpecialCases(operands[i]);
         handleSpecialCases(newOperand);


      }

      // Update live-in status
      //setLiveInStatus(inputOperands, outputOperands);


      return operands;
   }

//   private Carry[] buildCarries(MbInstruction mbInst) {
   private List<Operand> buildCarries(MbInstruction mbInst) {
      List<Operand> carries = new ArrayList<Operand>();

      // Carry In
      if(CarryProperties.usesCarryIn(mbInst.getInstructionName())) {
         carries.add(newCarry(Operand.FLOW_INPUT));
      }

      // Carry Out
      if(CarryProperties.usesCarryOut(mbInst.getInstructionName())) {
         carries.add(newCarry(Operand.FLOW_OUTPUT));
      }
      
      return carries;
   }

   /**
    * Transforms a list of strings containing MicroBlaze instructions into
    * a more convenient format.
    *
    * @param addresses
    * @param instructions
    * @return
    */

   public List<LowLevelInstruction> parseInstructions(List<Integer> addresses,
           List<String> instructions) {

   //public static List<LowLevelInstruction> getMbInstructions(List<Integer> addresses,
   //        List<String> instructions) {

      MbLowLevelParser lowLevelParser = new MbLowLevelParser();

      int numInstructions = instructions.size();
      List<LowLevelInstruction> mbInstructions = new ArrayList<LowLevelInstruction>();
      for (int i = 0; i < numInstructions; i++) {
         int address = addresses.get(i);
         String instruction = instructions.get(i);
         lowLevelParser.nextInstruction(address, instruction);
         List<LowLevelInstruction> mbInsts = lowLevelParser.getAndClearUnits();
         if (mbInsts != null) {
            mbInstructions.addAll(mbInsts);
         }
      }
      lowLevelParser.close();
      List<LowLevelInstruction> mbInsts = lowLevelParser.getAndClearUnits();
      if (mbInsts != null) {
         mbInstructions.addAll(mbInsts);
      }

      return mbInstructions;
   }

   private InstructionFlags getFlags(MbInstruction mbInst) {
      InstructionFlags flags = new InstructionFlags();
      flags.mappable = LliUtils.ENABLED;

      boolean loadInstruction = InstructionProperties.LOAD_INSTRUCTIONS.contains(mbInst.getInstructionName());
      boolean storeInstruction = InstructionProperties.STORE_INSTRUCTIONS.contains(mbInst.getInstructionName());
      boolean jumpInstruction = InstructionProperties.JUMP_INSTRUCTIONS.contains(mbInst.getInstructionName());

      if (storeInstruction) {
         flags.instructionType = InstructionFlags.TYPE_STORE;
      } else if (loadInstruction) {
         flags.instructionType = InstructionFlags.TYPE_LOAD;
      } else if (jumpInstruction) {
         flags.instructionType = InstructionFlags.TYPE_EXIT;
      } else {
         flags.instructionType = InstructionFlags.TYPE_GENERAL;
      }

      return flags;
   }

   private void setLiveInStatus(List<Operand> operands) {
      // Check inputs first
      setLiveInStatusInputs(operands);

      // Update written registers
      updateWrittenRegisters(operands);

   }

   private void setLiveInStatusInputs(List<Operand> operands) {
      for (Operand operand : operands) {
         if (operand.flow != Operand.FLOW_INPUT) {
            continue;
         }

         // Skip literals
         if (operand.type != Operand.TYPE_REGISTER) {
            continue;
         }

         // Check if operand was already written
         if (writtenRegisters.contains(operand.value)) {
            continue;
         }

         operand.isLiveIn = LliUtils.ENABLED;

      }
   }

   private void updateWrittenRegisters(List<Operand> operands) {
      for (Operand operand : operands) {
         if (operand.flow != Operand.FLOW_OUTPUT) {
            continue;
         }

         // Skip literals
         if (operand.type != Operand.TYPE_REGISTER) {
            continue;
         }

         // Add register to set
         writtenRegisters.add(operand.value);
      }

   }

   public void close() {
      // Do nothing... for now.
   }

   public List<LowLevelInstruction> getAndClearUnits() {
      List<LowLevelInstruction> returnList = instructions;
      instructions = new ArrayList<LowLevelInstruction>();
      return returnList;
   }

   public static Operand newCarry(int flow) {
      String value = RegisterName.getCarryFlagName();
      int type = Operand.TYPE_REGISTER;
      int isImmutable = LliUtils.DISABLED;
      Integer isLiveIn = LliUtils.DISABLED;

      return new Operand(value, type, flow, isImmutable, isLiveIn);
   }

   private MicroBlazeParser mbParser;
   private List<LowLevelInstruction> instructions;
   private Integer immValue;
   private Set<String> writtenRegisters;

}
