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

package org.specs.DymaLib.Dotty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.LowLevelInstruction.Elements.Carry;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.Elements.Operand;

/**
 * Graphic representation of a MegaBlock
 * TODO
 * @author Joao Bispo
 */
public class DottyStraigthLineLoop {

   public DottyStraigthLineLoop(Enum[] instructionNames) {
      this.values = instructionNames;

      operandCounter = 0;
      operantionCounter = 0;
      
      liveIns = new HashSet<Integer>();
      written = new HashSet<Integer>();

      registersIds = new HashMap<String,String>();
      liveInsIds = new HashMap<String,List<String>>();

      declarations = new ArrayList<String>();
      connections = new ArrayList<String>();
   }

   public static String generateDotty(List<LowLevelInstruction> instructions,
           Enum[] instructionNames) {
      
      DottyStraigthLineLoop dotty = new DottyStraigthLineLoop(instructionNames);
      for(LowLevelInstruction instruction : instructions) {
         dotty.addInstruction(instruction);
      }
      dotty.close();
      
      return DottyUtils.generateDotty(dotty.declarations, dotty.connections);
   }

   private void addInstruction(LowLevelInstruction instruction) {
      // Add Operation
      String instructionName = values[instruction.op].name();
      String operationId = newOperationId();
              //OPERATION_PREFIX + operantionCounter;
      //operantionCounter++;

      String color = null;
      if(instruction.mappable == LowLevelInstruction.DISABLED) {
         color = DottyUtils.COLOR_GRAY75;
      }

      declarations.add(DottyUtils.declaration(operationId, instructionName, null, color));
      //declarations.add(DottyUtils.declaration(operationId, instructionName,
      //        DottyUtils.SHAPE_BOX, DottyUtils.COLOR_LIGHTBLUE));

      // For each Operand, check if we declare a new one, or use one already
      // declared

      // Check inputs
      for(int i=0; i<instruction.operands.length; i++) {
         Operand operand = instruction.operands[i];


         if(operand.flow != Operand.FLOW_INPUT) {
            continue;
         }

         addInputOperand(operand, operationId);
      }

      // Check carry-in
      Carry carryIn = instruction.carries[LowLevelInstruction.CARRY_IN_INDEX];
      addCarryIn(carryIn, operationId);


      // Check outputs
      for (int i = 0; i < instruction.operands.length; i++) {
         Operand operand = instruction.operands[i];

         if (operand.flow != Operand.FLOW_OUTPUT) {
            continue;
         }

         addOutputOperand(operand, operationId);
         /*
         // Outputs are always declared
         String operandId = OPERAND_PREFIX + operandCounter;
         operandCounter++;

         String operandName = null;
         if (operand.type == Operand.TYPE_REGISTER) {
            operandName = "r" + operand.value;
            // Add output register to written set
            registersIds.put(Integer.toString(operand.value), operandId);
         } else {
            operandName = Integer.toString(operand.value);
         }

         declarations.add(DottyUtils.declaration(operandId, operandName,
                 DottyUtils.SHAPE_BOX, null));


         // Connect operand
         connections.add(DottyUtils.connection(operationId, operandId, null));
*/

      }

      // Add carry-out
      Carry carryOut = instruction.carries[LowLevelInstruction.CARRY_OUT_INDEX];
      addCarryOut(carryOut, operationId);
   }

   private void close() {
      // Attach a box to the live-outs
      for(String liveOut : registersIds.keySet()) {
         String registerId = registersIds.get(liveOut);

         String liveOutId = newOperandId();
         //OPERAND_PREFIX + operandCounter;
         //operandCounter++;
         String liveOutName = "LiveOut (r"+liveOut+")";

         // Create box for live-out
         declarations.add(DottyUtils.declaration(liveOutId, liveOutName,
                 DottyUtils.SHAPE_BOX, DottyUtils.COLOR_LIGHTBLUE));

         // Create connection
         connections.add(DottyUtils.connection(registerId, liveOutId, null));

         // Not needed... replicate live-outs instead, for the graph to be pretty.
         //registersIds.put(liveOut, liveOutId);
      }

      // Add live-outs to live-ins
      for(String liveIn : liveInsIds.keySet()) {
         // Check if live in is live out
         String liveOutId = registersIds.get(liveIn);

         // Immutable register
         if(liveOutId == null) {
            continue;
         }

         

         //String newLiveOutId = OPERAND_PREFIX + operandCounter;
         //operandCounter++;
         String liveOutName = "LiveOut (r"+liveIn+")";



         // Add connections for each liveIn
         List<String> liveInCopies = liveInsIds.get(liveIn);
         for (String liveInId : liveInCopies) {
            String newLiveOutId = newOperandId();
            // Create box for live-out
            declarations.add(DottyUtils.declaration(newLiveOutId, liveOutName,
                    DottyUtils.SHAPE_BOX, DottyUtils.COLOR_LIGHTBLUE));

            connections.add(DottyUtils.connection(newLiveOutId, liveInId, null));
         }
      }
   }

   private void addInputOperand(Operand operand, String operationId) {
              String operandId = null;

         if(operand.type == Operand.TYPE_REGISTER) {
            // Check if we need to declare
            operandId = registersIds.get(Integer.toString(operand.value));

            // Create
            if(operandId == null) {
               String operandName = "In:r"+operand.value;

               operandId = newOperandId();
               //operandId = OPERAND_PREFIX + operandCounter;
               //operandCounter++;

               declarations.add(DottyUtils.declaration(operandId, operandName,
                       DottyUtils.SHAPE_BOX, null));

               // Add to liveIns
               List<String> ids = liveInsIds.get(Integer.toString(operand.value));
               if(ids == null) {
                  ids = new ArrayList<String>();
                  liveInsIds.put(Integer.toString(operand.value), ids);
               }
               ids.add(operandId);
            }
      } else {
         // Immediates are always declared
         String operandName = Integer.toString(operand.value);
         operandId = newOperandId();

         //operandId = OPERAND_PREFIX + operandCounter;
         //operandCounter++;

         declarations.add(DottyUtils.declaration(operandId, operandName,
                 DottyUtils.SHAPE_BOX, null));
      }

      // Connect operand
      connections.add(DottyUtils.connection(operandId, operationId, null));
   }

   private void addCarryIn(Carry carryIn, String operationId) {
      if(carryIn.enabled == LowLevelInstruction.DISABLED) {
         return;
      }

              //Get carry name
      String operandName = null;
      if(carryIn.immutable == LowLevelInstruction.ENABLED) {
         operandName = "(C) " + carryIn.value;
      } else {
         operandName = CARRY_NAME;
      }

      // Check if there is a carry output
      String operandId = registersIds.get(operandName);

      if (operandId == null) {
         // Instantiate Carry-In
         operandId = newOperandId();
         declarations.add(DottyUtils.declaration(operandId, operandName,
                 DottyUtils.SHAPE_BOX, null));
      }

      // Connection
      connections.add(DottyUtils.connection(operandId, operationId, null));
   }

   private void addCarryOut(Carry carryOut, String operationId) {
      if (carryOut.enabled == LowLevelInstruction.DISABLED) {
         return;
      }

      //Get carry name
      String operandName = null;
      if (carryOut.immutable == LowLevelInstruction.ENABLED) {
         operandName = "(C) " + carryOut.value;
      } else {
         operandName = CARRY_NAME;
      }

      // Instantiate carry-out
      String operandId = newOperandId();
      declarations.add(DottyUtils.declaration(operandId, operandName,
              DottyUtils.SHAPE_BOX, null));


      // Connection
      connections.add(DottyUtils.connection(operationId, operandId, null));

      // Put carry out output list
      registersIds.put(operandName, operandId);
   }

   private void addOutputOperand(Operand operand, String operationId) {
      // Outputs are always declared
      String operandId = newOperandId();

      String operandName = null;
      if (operand.type == Operand.TYPE_REGISTER) {
         operandName = "r" + operand.value;
         // Add output register to written set
         registersIds.put(Integer.toString(operand.value), operandId);
      } else {
         operandName = Integer.toString(operand.value);
      }

      declarations.add(DottyUtils.declaration(operandId, operandName,
              DottyUtils.SHAPE_BOX, null));


      // Connect operand
      connections.add(DottyUtils.connection(operationId, operandId, null));
   }

   public String newOperandId() {
      String operandId = OPERAND_PREFIX + operandCounter;
      operandCounter++;

      return operandId;
   }

   public String newOperationId() {
      String operationId = OPERATION_PREFIX + operantionCounter;
      operantionCounter++;

      return operationId;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int operantionCounter;
   private int operandCounter;
   private Set<Integer> liveIns;
   private Set<Integer> written;

   private Enum[] values;
   
   private List<String> declarations;
   private List<String> connections;

   private static final String OPERATION_PREFIX = "operation_";
   private static final String OPERAND_PREFIX = "operand_";
   private static final String CARRY_NAME = "carry";
   
   private Map<String,String> registersIds;
   private Map<String,List<String>> liveInsIds;







}
