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

package org.specs.DymaLib.TraceUnit.Builders;

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.UnitBuilder;

/**
 *
 * @author Joao Bispo
 */
public class BasicBlockBuilder implements UnitBuilder {

   public BasicBlockBuilder(InstructionDecoder instructionDecoder) {
      this.instructionDecoder = instructionDecoder;
      this.branchChecker = new BranchChecker();
      this.currentTraceUnits = new ArrayList<TraceUnit>();
      resetBlock();
   }


   public void nextInstruction(int address, String instruction) {
      //System.out.println("Feeding instruction:"+instruction);
      // Check if is first instruction of block
      if(currentInstructions == null) {
         
         initBlock(address);
         
      }

      // Add current instruction

      addInstruction(address, instruction);

      // Check if the control-flow can change after this instruction
      
      if(branchChecker.isBranch(instruction, instructionDecoder)) {
         
         completeBlock();
      }

   }

   public void close() {
      completeBlock();
   }

   public List<TraceUnit> getAndClearUnits() {
      List<TraceUnit> returnList = currentTraceUnits;
      currentTraceUnits = new ArrayList<TraceUnit>();
      return returnList;
   }

   private void initBlock(int address) {
      currentInstructions = new ArrayList<String>();
      currentAddresses = new ArrayList<Integer>();
      currentIdentifier = address;
   }

   private void resetBlock() {
      currentInstructions = null;
      currentAddresses = null;
      currentIdentifier = null;
   }


   private void addInstruction(int address, String instruction) {
      currentInstructions.add(instruction);
      currentAddresses.add(address);
   }

   private void completeBlock() {
      if(currentInstructions == null) {
         return;
      }

      // Build Trace Unit
      TraceUnit traceUnit = new TraceUnit(currentInstructions, currentAddresses, currentIdentifier);
      currentTraceUnits.add(traceUnit);

      resetBlock();
   }


   private List<TraceUnit> currentTraceUnits;

   private List<String> currentInstructions;
   private List<Integer> currentAddresses;
   private Integer currentIdentifier;

   private final InstructionDecoder instructionDecoder;
   private final BranchChecker branchChecker;


   /**
    * Checkes for branches in architectures with delay slots
    */
   class BranchChecker {

      public BranchChecker() {
         currentDelaySlot = 0;
      }

      public boolean isBranch(String instruction, InstructionDecoder instructionDecoder) {

         
        
         if (currentDelaySlot > 1) {
            currentDelaySlot--;
            return false;
         }

         // Check if we are on a delay slot
         if (currentDelaySlot == 1) {
            currentDelaySlot--;
            return true;
         }


         // Check if it is a jump instruction
         if (instructionDecoder.isJump(instruction)) {
           
            // Check if it has delay slot
            int delaySlot = instructionDecoder.delaySlot(instruction);
            if (delaySlot > 0) {
               currentDelaySlot = delaySlot;
               return false;
            } else {
               return true;
            }
         }

         return false;
      }
      private int currentDelaySlot;

   }

}
