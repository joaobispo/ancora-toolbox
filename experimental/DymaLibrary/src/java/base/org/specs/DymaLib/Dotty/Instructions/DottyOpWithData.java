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

package org.specs.DymaLib.Dotty.Instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.Dotty.DottyUtils;


/**
 * Builds a graphic representation (in DOT format) of operations including the
 * dependencies between the data registers.
 *
 * @author Joao Bispo
 */
public class DottyOpWithData {

  // public DottyOpWithData(Enum[] instructionNames) {
   public DottyOpWithData() {
//      this.values = instructionNames;

      operandCounter = 0;
      operantionCounter = 0;

      //registersIds = new HashMap<String,String>();
      liveInsIds = new HashMap<String,List<String>>();

      declarations = new ArrayList<String>();
      connections = new ArrayList<String>();

      //connectOperationsDirectly = false;

      dottyIds = new HashMap<String, String>();
   }

   public List<String> getConnections() {
      return connections;
   }

   public List<String> getDeclarations() {
      return declarations;
   }

   public String getDotty() {
      return DottyUtils.generateDotty(declarations, connections);
   }
   /*
   public static String generateDotty(List<LowLevelInstruction> instructions,
           Enum[] instructionNames) {

      DottyOpWithData dotty = new  DottyOpWithData(instructionNames);
      for(LowLevelInstruction instruction : instructions) {
         dotty.addInstruction(instruction);
      }
      dotty.close();

      return DottyUtils.generateDotty(dotty.declarations, dotty.connections);
   }
    *
    */

   //public void addInstruction(String opName, boolean isMappable, List<DottyOperand> operands) {
   public void addInstruction(String opName, List<DottyOperand> operands) {


      // Add Operation
      String instructionName = opName;
      String operationId = newOperationId();

      //String color = null;
      //if(!isMappable) {
      //   color = DottyUtils.COLOR_GRAY75;
      //}

      //declarations.add(DottyUtils.declaration(operationId, instructionName, null, color));
      declarations.add(DottyUtils.declaration(operationId, instructionName, null, null));

      OperandList operandList = new OperandList();
      for(DottyOperand operand : operands) {
         //String dottyId = addOperand(operand, opName);
         String dottyId = addOperand(operand, operationId);

         if(!operand.isInput) {
            operandList.getDottyId().add(dottyId);
            operandList.getOperandId().add(operand.id);
         }
      }

      // Update table with new output operands
      for(int i=0; i<operandList.size(); i++) {
         String operandId = operandList.getOperandId().get(i);
         String dottyId = operandList.getDottyId().get(i);
         dottyIds.put(operandId, dottyId);
      }
   }

   public void close() {
      // Attach a box to the live-outs
      for(String liveOut : dottyIds.keySet()) {
         String registerId = dottyIds.get(liveOut);

         String liveOutId = newOperandId();
         //OPERAND_PREFIX + operandCounter;
         //operandCounter++;
         //String liveOutName = "LiveOut (r"+liveOut+")";
         String liveOutName = "LiveOut ("+liveOut+")";

         // Create box for live-out
         declarations.add(DottyUtils.declaration(liveOutId, liveOutName,
                 DottyUtils.SHAPE_BOX, DottyUtils.COLOR_LIGHTBLUE));

         // Create connection
         connections.add(DottyUtils.connection(registerId, liveOutId, null));
      }

      // Add live-outs to live-ins
      for(String liveIn : liveInsIds.keySet()) {
         // Check if live in is live out
         String liveOutId = dottyIds.get(liveIn);

         // Immutable register
         if(liveOutId == null) {
            continue;
         }

         String liveOutName = "LiveOut ("+liveIn+")";

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

   private String addInputOperand(DottyOperand operand, String opName) {
      String operandId = null;

      if (operand.isImmediate) {
         operandId = addInputImmediate(operand);
      } else {
         operandId = addInputRegister(operand);
      }

      // Connect operand to operation
      connections.add(DottyUtils.connection(operandId, opName, null));

      return operandId;
   }

   private String addOutputOperand(DottyOperand operand, String operationName) {
      // Outputs are always declared
      String dottyId = newOperandId();

      String operandName = null;
      if (!operand.isImmediate) {
         operandName = operand.id;
         // Add output register to written set
         //registersIds.put(Integer.toString(operand.value), operandId);
         //dottyIds.put(operand.id, dottyId);
      } else {
         //operandName = Integer.toString(operand.value);
         operandName = operand.value;
      }

      declarations.add(DottyUtils.declaration(dottyId, operandName,
              DottyUtils.SHAPE_BOX, null));


      // Connect operand
      connections.add(DottyUtils.connection(operationName, dottyId, null));

      return dottyId;
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

   //private Enum[] values;

   private List<String> declarations;
   private List<String> connections;

   private static final String OPERATION_PREFIX = "operation_";
   private static final String OPERAND_PREFIX = "operand_";

   //private Map<String,String> registersIds;
   // Maps register ids to the dotty ids which correspond to their last write.
   private Map<String,String> dottyIds;
   private Map<String,List<String>> liveInsIds;

   //private boolean connectOperationsDirectly;

   private String addOperand(DottyOperand operand, String opName) {
      
      if (operand.isInput) {
         return addInputOperand(operand, opName);
      } else {
         /*
         if (connectOperationsDirectly) {

            // EXPERIMENT
            if (!operand.isImmediate) {
               // Add output register to written set
               dottyIds.put(operand.value, operand.id);
            }
         } else {
          *
          */
            return addOutputOperand(operand, opName);
         //}
      }
   }

   private String addInputRegister(DottyOperand operand) {

      // Only add operand if live-in. If it is a register and is not a live-in,
      // we have to get the operand from the table
      if (operand.isLiveIn) {
         return addInputRegisterLiveIn(operand);
      } else {
         return addInputRegisterExisting(operand);
      }
   }

   private String addInputImmediate(DottyOperand operand) {
      // Immediates are always declared
      String operandName = operand.value;
      String dottyId = newOperandId();

      declarations.add(DottyUtils.declaration(dottyId, operandName,
              DottyUtils.SHAPE_BOX, null));

      return dottyId;
   }

   private String addInputRegisterLiveIn(DottyOperand operand) {
       String operandName = "In:" + operand.id;

      String dottyId = newOperandId();

      declarations.add(DottyUtils.declaration(dottyId, operandName,
              DottyUtils.SHAPE_BOX, null));

      // Add to liveIns
      List<String> ids = liveInsIds.get(operand.id);
      if (ids == null) {
         ids = new ArrayList<String>();
         liveInsIds.put(operand.id, ids);
      }
      ids.add(dottyId);

      return dottyId;
   }

   private String addInputRegisterExisting(DottyOperand operand) {
      // Get operand from table
      String dottyId = dottyIds.get(operand.id);
      return dottyId;
   }


}

