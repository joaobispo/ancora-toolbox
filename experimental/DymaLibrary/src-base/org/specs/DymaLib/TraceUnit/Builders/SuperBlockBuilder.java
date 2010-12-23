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
import org.ancora.SharedLibrary.BitUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.UnitBuilder;

/**
 *
 * @author Joao Bispo
 */
public class SuperBlockBuilder implements UnitBuilder {

   public SuperBlockBuilder(InstructionDecoder instructionDecoder) {
      basicBlockBuilder = new BasicBlockBuilder(instructionDecoder);
      completedSuperBlocks = new ArrayList<TraceUnit>();
      resetSuperBlock();
   }



   public void nextInstruction(int address, String instruction) {
      // Feed instruction to BasicBlockBuilder
      basicBlockBuilder.nextInstruction(address, instruction);
      processBasicBlockBuilder();
   }


   private void processBasicBlockBuilder() {
      // Check if there is a new BasicBlock
      List<TraceUnit> basicBlocks = basicBlockBuilder.getAndClearUnits();
      if (basicBlocks == null) {
         return;
      }

      // Process each BasicBlock
      for (TraceUnit basicBlock : basicBlocks) {
         processBasicBlock(basicBlock);
      }
   }

   private void processBasicBlock(TraceUnit basicBlock) {
      // Check if is first basic block
      if(previousBasicBlocks == null) {
         initBlocks();
         addBasicBlock(basicBlock);
         return;
      }

      // Check if this basic block is a backward jump in relation to the previous basic block
      int lastBbIndex = previousBasicBlocks.size()-1;
      TraceUnit lastBasicBlock = previousBasicBlocks.get(lastBbIndex);
      int lastAddressIndex = lastBasicBlock.getAddresses().size()-1;
      int lastAddress = lastBasicBlock.getAddresses().get(lastAddressIndex);

      // If first address of current basic block is greater than previous address,
      // it is a forward jump.
      int firstAddress = basicBlock.getAddresses().get(0);

      if(firstAddress > lastAddress) {
         addBasicBlock(basicBlock);
         return;
      }

      // Backward jump. Build SuperBlock and start a new one.
      completeSuperBlock();
      initBlocks();
      addBasicBlock(basicBlock);
   }

   private void addBasicBlock(TraceUnit basicBlock) {
      previousBasicBlocks.add(basicBlock);
      currentId = BitUtils.superFastHash(currentId, basicBlock.getIdentifier());
   }

   public void close() {
      completeSuperBlock();
   }

   public List<TraceUnit> getAndClearUnits() {
      List<TraceUnit> returnList = completedSuperBlocks;
      completedSuperBlocks = new ArrayList<TraceUnit>();
      return returnList;
   }

   private void resetSuperBlock() {
      previousBasicBlocks = null;
      currentId = null;
   }
   
   private void initBlocks() {
      previousBasicBlocks = new ArrayList<TraceUnit>();
      currentId = 0;
   }

   private void completeSuperBlock() {
      if(previousBasicBlocks == null) {
         return;
      }

      List<String> superBlockInstructions = new ArrayList<String>();
      List<Integer> superBlockAddresses = new ArrayList<Integer>();

      for(TraceUnit basicBlock : previousBasicBlocks) {
         superBlockAddresses.addAll(basicBlock.getAddresses());
         superBlockInstructions.addAll(basicBlock.getInstructions());
      }

      TraceUnit superBlock = new TraceUnit(superBlockInstructions, superBlockAddresses, currentId);
      completedSuperBlocks.add(superBlock);
   }

   private List<TraceUnit> completedSuperBlocks;
   private UnitBuilder basicBlockBuilder;

   private List<TraceUnit> previousBasicBlocks;
   private Integer currentId;








}
