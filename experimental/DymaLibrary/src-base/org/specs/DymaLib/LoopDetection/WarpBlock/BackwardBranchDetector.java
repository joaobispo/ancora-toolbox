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

package org.specs.DymaLib.LoopDetection.WarpBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.TraceUnit.TraceUnit;

/**
 * Detects Warp-style blocks. (NOT WORKING)l
 *
 * @author Joao Bispo
 */
public class BackwardBranchDetector implements LoopDetector {

//   public WarpBlockDetector(boolean limitBackwardJump, int backwardJumpMaxSize,
   public BackwardBranchDetector(int backwardJumpMaxSize,
           boolean storeSequenceInstructions, InstructionDecoder decoder) {
      /*
      this.limitBackwardJump = limitBackwardJump;
      this.backwardJumpMaxSize = backwardJumpMaxSize;
      this.storeSequenceInstructions = storeSequenceInstructions;

      lastAddress = null;
      lastOffset = null;

      currentTraceUnits = new ArrayList<TraceUnit>();
      currentIds = new HashSet<Integer>();
*/
      //currentLoop = null;
      

      //instBuilder = new InstructionBuilder();
      //sbBuilder = new SuperBlockBuilder(decoder);
      //this.decoder = decoder;

      //currentLoop = null;
/*
      currentAddress = null;
      nextAddress = null;

      currentInstruction = null;
      nextAddress = null;
*/
      foundLoops = new ArrayList<CodeSegment>();
      this.storeSequenceInstructions = storeSequenceInstructions;
      this.backwardJumpMaxSize = backwardJumpMaxSize;

      //sbb = new ShortBackwardBranch(decoder, limitBackwardJump, backwardJumpMaxSize);
      sbb = new ShortBackwardBranch(decoder);
      currentBranchData = null;

      //currentTraceUnits = null;
      //currentIds = null;
      startSequence();
   }


   private void processBranchData(BranchData branchData) {
      boolean invalidBranchData = branchData.offset > backwardJumpMaxSize;

      // It was a sequence, a loop will start now
      if(currentBranchData == null) {
         // If invalid, ignore branch data
         
         if(invalidBranchData) {
            return;
         }
                   
         
         newUnit();
         //LoopUnit sequenceUnit = newUnit();
         //foundLoops.add(sequenceUnit);
         startLoop();
         currentBranchData = branchData;
//         System.out.println("Starting loop");
         return;
      }

      // Check if is new iteration of current loop
      
      //if(currentBranchData.equals(branchData)) {
      if(branchData.isPartOf(currentBranchData)) {
//         System.out.println("New iteration of the loop");
         //retireCurrentInstructions();
         return;
      }
        
       
//      System.out.println("Branch data is different");
//      System.out.println("Current:"+currentBranchData);
//      System.out.println("New:"+branchData);
      // There is the start of a possible new loop
      newUnit();


      //LoopUnit loopUnit = newUnit();
      //foundLoops.add(loopUnit);

      if(invalidBranchData) {
         startSequence();
         currentBranchData = null;
      } else {
         startLoop();
   //            System.out.println("--Break Loop--");
   //   System.out.println("Previous Loop:"+currentBranchData);
   //   System.out.println("New Loop:"+branchData);

         currentBranchData = branchData;
      }

   }

   private void addInstruction(int address, String instruction) {
      if(isLoop()) {
         processLoop(address, instruction);
      }

      // Update data
      updateData(address, instruction);
     
   }

   private void updateData(int address, String instruction) {
      if (!isLoop() && !storeSequenceInstructions) {
         addInstructionToIncompleteSequence(address, instruction);
      } else {
         currentInstructions.add(instruction);
         currentAddresses.add(address);
         currentTotalInstructions++;
      }

      // Check if end addr. Means we completed an iteration
      if (isLoop()) {
         if (currentBranchData.endAddr == address) {
            currentIterations++;
            retireCurrentInstructions();
         }
      }
   }

   private void addInstructionToIncompleteSequence(int address, String instruction) {
      if(currentInstructions.size() < 2) {
         currentInstructions.add(instruction);
         currentAddresses.add(address);
      } else {
          currentInstructions.set(1, instruction);
         currentAddresses.set(1, address);
      }
      currentTotalInstructions++;
   }

   private void processLoop(int address, String instruction) {
      // Check if we are out of the loop
      if(address > currentBranchData.endAddr) {
         newUnit();
         //LoopUnit loopUnit = newUnit();
         //foundLoops.add(loopUnit);
         startSequence();
         currentBranchData = null;
      }
   }

   /**
    * Collects current data and builds a sequence LoopUnit.
    * @return
    */
   private void newSequence() {
   //private LoopUnit newSequence() {
      // If is a sequence, there are no TraceUnits
      if(currentTraceUnits != null) {
         LoggingUtils.getLogger().
                 warning("Bug. We are in a sequence,there should be no current Trace Units");
         Exception ex = new Exception();
         ex.printStackTrace();
      }

      // Build sequence trace unit
      List<TraceUnit> traceUnit = buildSequenceTraceUnit();

      foundLoops.add(new BackwardBranchLoopUnit(traceUnit, traceUnit.get(0).getIdentifier(), currentIterations,
              currentTotalInstructions, false, storeSequenceInstructions));
   }

   private List<TraceUnit>  buildSequenceTraceUnit() {
      TraceUnit unit = buildTraceUnit();

      
      List<TraceUnit> list = new ArrayList<TraceUnit>();
      list.add(unit);
      //list.add(new TraceUnit(currentInstructions, currentAddresses, id));

      return list;
   }

   private TraceUnit buildTraceUnit() {
      int id = currentAddresses.get(0);
      for(int i=1; i<currentAddresses.size(); i++) {
         id = BitUtils.superFastHash(currentAddresses.get(i), id);
      }

      return new TraceUnit(currentInstructions, currentAddresses, id);
   }

   private void newLoop() {
   //private LoopUnit newLoop() {
      // Check if we need to add a sequence after the loop
      boolean buildNewSequence = false;
      List<String> seqInst = null;
      List<Integer> seqAddr = null;
      if (!currentInstructions.isEmpty()) {
         buildNewSequence = true;
         seqInst = currentInstructions;
         seqAddr = currentAddresses;
         //currentIterations++;
         // Retire current instructions, if any
         //retireCurrentInstructions();
      }


      if (currentIterations > 0) {
         // Build id
         int id = currentTraceUnits.get(0).getIdentifier();
         for (int i = 1; i < currentTraceUnits.size(); i++) {
            id = BitUtils.superFastHash(currentTraceUnits.get(i).getIdentifier(), id);
         }

         int totalInstructions = currentTotalInstructions - currentInstructions.size();
         foundLoops.add(new BackwardBranchLoopUnit(currentTraceUnits, id, currentIterations,
                 totalInstructions, true, true));
         //currentTotalInstructions, true, true);
      }
      /*
      else {
         System.out.println("Current iterations are zero. Build new sequence? "+buildNewSequence);
      }
      */
      
      if(buildNewSequence) {
         startSequence();
         currentIterations = 1;
         currentTotalInstructions = seqInst.size();
         currentInstructions = seqInst;
         currentAddresses = seqAddr;
         newSequence();
      }
   }

   private void startLoop() {
      currentTotalInstructions = 0;
      currentIterations = 0;
      currentInstructions = new ArrayList<String>();
      currentAddresses = new ArrayList<Integer>();


      currentIds = new HashSet<Integer>();
      currentTraceUnits = new ArrayList<TraceUnit>();
   }

   private void startSequence() {
      currentTotalInstructions = 0;
      currentIterations = 1;
      currentInstructions = new ArrayList<String>();
      currentAddresses = new ArrayList<Integer>();


      currentIds = null;
      currentTraceUnits = null;
   }

   private void retireCurrentInstructions() {
      // Should only be called if while in a loop
      if(!isLoop()) {
         LoggingUtils.getLogger().
                 warning("Bug! This method should only be called while in a loop.");
         return;
      }

      if(currentInstructions.isEmpty()) {
          LoggingUtils.getLogger().
                 warning("Retiring instructions but current list is empty..."
                 + " Verify if this should happen.");
         return;
      }

      // Build trace unit
      TraceUnit traceUnit = buildTraceUnit();

      //System.out.println("CurrentIds:"+currentIds);
      //System.out.println("TraceUnit:"+traceUnit);
      // Check if traceUnit was not already added to this loop
      if(!currentIds.contains(traceUnit.getIdentifier())) {
         currentTraceUnits.add(traceUnit);
         currentIds.add(traceUnit.getIdentifier());
      }

      
      currentInstructions = new ArrayList<String>();
      currentAddresses = new ArrayList<Integer>();
   }



   private void newUnit() {
//   private LoopUnit newUnit() {
      if(isLoop()) {
         newLoop();
         //return newLoop();
      } else {
         newSequence();
      }
      //return newSequence();
   }



   private boolean isLoop() {
      if(currentBranchData == null) {
         return false;
      }

      return true;
   }

   public void step(int address, String instruction) {
      //System.out.println(address+": "+instruction);
      sbb.step(address, instruction);
      BranchData branchData = sbb.getAndClearData();

      if(branchData != null) {
  //       System.out.println("------DATA-------");
  //       System.out.println(branchData);
         processBranchData(branchData);
      }

      addInstruction(address, instruction);
/*
      if(branchData != null) {
         //System.out.println(branchData);
         
         //System.out.println("Found loop!");
         if(currentBranchData == null) {
            currentBranchData = branchData;
            currentTotalInstructions = 0;
            currentIterations = 0;
         } else {
            // Check if it is the same loop
            if(!currentBranchData.equals(branchData)) {
               
            System.out.println("Iterations:"+currentIterations);
            System.out.println("Total Inst:"+currentTotalInstructions);
            //System.out.println("Avg per Iterationt:"+(numLoopInstructions/numIterations));
            currentIterations = 0;
            currentTotalInstructions = 0;
            System.out.println("Another loop started");
               // New loop
               //System.out.println("Already have branch data...");
               //System.exit(1);
            }
         }
          
      }

      
      // There is a loop
      if(currentBranchData != null) {
        // System.out.println("Current Address:"+address);
        // System.out.println("Start Address:"+currentBranchData.startAddr);
         //System.out.println("End Address:"+currentBranchData.endAddr);
         //System.out.println("End Offset:"+currentBranchData.offsetAddr);
         if(currentBranchData.startAddr == address) {
            //System.out.println("Starting loop:");
            //System.out.println(instruction);
            currentTotalInstructions++;
            currentIterations++;
            return;
         }

         if(currentBranchData.endAddr > address) {
            //System.out.println(instruction);
            currentTotalInstructions++;
            return;
         }

         if(currentBranchData.endAddr == address) {
            //System.out.println(instruction);
            //System.out.println("Loop iteration ended");
            currentTotalInstructions++;
            return;
         }

         if(currentBranchData.endAddr < address) {
            System.out.println("Loop ended");
            System.out.println("Iterations:"+currentIterations);
            System.out.println("Total Inst:"+currentTotalInstructions);
            //System.out.println("Avg per Iterationt:"+(numLoopInstructions/numIterations));
            currentIterations = 0;
            currentTotalInstructions = 0;
            currentBranchData = null;
            return;
         }

      }

*/

/*
      if(nextAddress == null) {
         nextAddress = address;
         nextInstruction = instruction;
         return;
      }

      currentAddress = nextAddress;
      currentInstruction = nextInstruction;

      nextInstruction = instruction;
      nextAddress = address;

      processCurrentInstruction();
*/
      
      /*
      // Initial case
      if(lastAddress == null) {
         currentLoop = new LoopUnit(storeSequenceInstructions);
         addToSequence(address, instruction);


         //totalInstructions++;
         lastAddress = address;
         return;
      }
       *
       */

/*

      // Check if instruction is a branch
      if(isShortBackwardBranch(instruction, lastAddress)) {
         completeBasicBlock();
      }

     // Add instruction to current block of instructions
      currentInstructions.add(instruction);
      totalInstructions++;
      lastAddress = instruction.getAddress();
 *
 */
   }

/*
   private void processCurrentInstruction() {

      // Build SuperBlocks
      //sbBuilder.nextInstruction(address, instruction);
      //processUnits(sbBuilder.getAndClearUnits());
   }
*/

   public void close() {
      if(!currentInstructions.isEmpty()) {
         newUnit();
         //foundLoops.add(newUnit());
      }
      // Do nothing for now
      //throw new UnsupportedOperationException("Not supported yet.");
   }

   public List<CodeSegment> getAndClearUnits() {
      if (foundLoops.isEmpty()) {
         return null;
      }

      List<CodeSegment> returnList = foundLoops;
      foundLoops = new ArrayList<CodeSegment>();
      return returnList;
   }

   public String getId() {
      return LoopDetectors.BackwardBranchBlock.name() + backwardJumpMaxSize;
   }

   /*
   private void addToSequence(int address, String instruction) {
      instBuilder.nextInstruction(address, instruction);
      add(instBuilder.getAndClearUnits().get(0), LoopUnit.Type.Sequence);
   }
*/
   /*
      private void add(TraceUnit traceUnit, LoopUnit.Type type) {
      if(currentLoop.getType() != type) {
         LoggingUtils.getLogger().
                 warning("Bug. LoopUnit of type '"+currentLoop.getType()+
                 "' should be of type '"+type+"'");
         return;
      }
      currentLoop.addTraceUnit(traceUnit);
   }
*/
/*
   private void processUnits(List<TraceUnit> units) {
      if(units == null) {
         return;
      }

      for(TraceUnit unit : units) {
         int offset = unit.getAddresses().size();
         int address = unit.getAddresses().get(offset-1);

         // Check if there is a loop going on
         if(currentLoop != null) {
            processLoop(unit, offset, address);
            continue;
         }


      }
   }
*/
   /*
   private void processLoop(TraceUnit unit, int offset, int address) {
      throw new UnsupportedOperationException("Not yet implemented");
   }
*/
    /**
    * CAUTION: In architectures with delay slots, the instruction where the
    * control flow jumps might not be the branch instruction itself.
    *
    * @param instruction
    * @return true if the this instruction represents a jump in the control flow.
    */
   //protected abstract boolean isShortBackwardBranch(GenericInstruction instruction, int nextInstAddress);
  /*
      private boolean isShortBackwardBranch(GenericInstruction currentInstruction, int lastAddress) {

      if (!lastInstructionWasJump(currentInstruction)) {
         return false;
      }
      //System.err.println("Current inst:"+currentInstruction.toLine());
      //System.err.println("Last address:"+lastAddress);
      int offset = currentInstruction.getAddress() - lastAddress;
      //System.err.println("Offset:"+offset);

      //if(offset <= 0) {
      if (offset >= 0) {
         return false;
      }

      if (useBranchLimit && (Math.abs(offset) > backwardBranchMaxOffset)) {
         return false;
      }

      return true;
   }
    */
   //private boolean limitBackwardJump;
   //private int backwardJumpMaxSize;
   

   //private Integer lastAddress;
   //private Integer lastOffset;

   //private List<TraceUnit> currentTraceUnits;
   //private Set<Integer> currentIds;
   //private LoopUnit currentLoop;
   
   //private WarpLoopUnit currentLoop;

   //private InstructionDecoder decoder;
   //private InstructionBuilder instBuilder;
  //private SuperBlockBuilder sbBuilder;

   /*
   private Integer currentAddress;
   private Integer nextAddress;

   private String currentInstruction;
   private String nextInstruction;
*/

// OBJECT STATE DATA
   private List<CodeSegment> foundLoops;
   private ShortBackwardBranch sbb;
   private BranchData currentBranchData;
   private boolean storeSequenceInstructions;
   private int backwardJumpMaxSize;

   // LOOP UNIT DATA
   private int currentTotalInstructions;
   private int currentIterations;
   private List<String> currentInstructions;
   private List<Integer> currentAddresses;
   

   private Set<Integer> currentIds;
   private List<TraceUnit> currentTraceUnits;



}
