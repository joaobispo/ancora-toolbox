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
import org.specs.DymaLib.LoopDetection.CodeSegment;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.TraceUnitUtils;

/**
 *
 * @author Joao Bispo
 */
public class WarpLoopUnit implements CodeSegment {

   public WarpLoopUnit(List<TraceUnit> loopUnits, Integer id, int iterations, int totalInstructions, boolean isLoop, boolean allInstructionsStored) {
      this.loopUnits = loopUnits;
      this.id = id;
      this.iterations = iterations;
      this.totalInstructions = totalInstructions;
      this.isLoop = isLoop;
      this.allInstructionsStored = allInstructionsStored;
   }

   

   /**
    * Every WarpLoopUnit starts as a sequence of instructions.
    *
    * @param iterations
    * @param allInstructionsStored
    */
   /*
   private WarpLoopUnit(boolean allInstructionsStored) {
      loopUnits = new ArrayList<TraceUnit>();
      id = null;
      
   this.isLoop = false;
      this.iterations = 1;
   this.allInstructionsStored = allInstructionsStored;

   //instCounter = 0;
   totalInstructions = 0;
   currentIds = null;

      currentInstructions = null;
      currentAddresses = null;
      //currentInstructions = new ArrayList<String>();
      //currentAddresses = new ArrayList<Integer>();
}
*/
   /*
   public static WarpLoopUnit newLoop() {
      return null;
   }

   public static WarpLoopUnit newSequence() {
      return null;
   }
    *
    */


   public List<String> getInstructions() {
      return TraceUnitUtils.extractInstructions(loopUnits);
   }

   public List<Integer> getAddresses() {
      return TraceUnitUtils.extractAddresses(loopUnits);
   }

   public int getId() {
      return id;
   }

   public float getNumInstructionsFloat() {
      return (float) getTotalInstructions() / (float) getIterations();
   }

   public int getNumInstructions() {
      return getTotalInstructions() / getIterations();
      /*
      float avg = (float) getTotalInstructions() / (float) getIterations();
      int intAvg = getTotalInstructions() / getIterations();

      if((float) intAvg != avg) {
         System.out.println("Truncation while averaging number of instructions:");
         System.out.println("Avg. number of instructions (float):"+avg);
         System.out.println("Avg. number of instructions (int)  :"+intAvg);
      }

      return intAvg;
       * 
       */
      //return instCounter;
   }

   public int getIterations() {
      return iterations;
   }

   public int getTotalInstructions() {
      return totalInstructions;
      //return getNumInstructions() * getIterations();
   }

   public boolean isLoop() {
      return  isLoop;
   }

   public boolean areAllInstructionsStored() {
      return allInstructionsStored;
   }

   public List<TraceUnit> getLoopUnits() {
      return loopUnits;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      //List<String> inst = TraceUnitUtils.extractInstructionsWithAddresses(instructions);

      builder.append("iterations:");
      builder.append(getIterations());
      builder.append("\n");


      builder.append("sequence instructions: ");
      builder.append(getFormattedNumInstructions());
      builder.append("\n");
      builder.append("instructions x iterations: ");
      builder.append(getTotalInstructions());
      builder.append("\n");

      boolean firstTime = true;
      for (TraceUnit unit : loopUnits) {
         if (!firstTime) {
            builder.append("-----------------------\n");
         }

         List<String> inst = unit.getInstructions();
         List<Integer> addr = unit.getAddresses();
         for (int i = 0; i < inst.size(); i++) {
            builder.append(addr.get(i));
            builder.append(": ");
            builder.append(inst.get(i));
            builder.append("\n");
         }

         firstTime = false;
      }

      return builder.toString();
   }

   private String getFormattedNumInstructions() {
      float floatAvg = getNumInstructionsFloat();
      int intAvg = getNumInstructions();

      if((float) intAvg == floatAvg) {
         return Integer.toString(intAvg);
      } else {
         return Float.toString(floatAvg);
      }
   }

   /*
   public void addInstruction(int address, String instruction, int nextAddress) {
      // Check if it is first instruction
      if(currentInstructions == null) {
         addFirstInstruction(address, instruction, nextAddress);
      }
   }

   private void addFirstInstruction(int address, String instruction, int nextAddress) {
      currentInstructions = new ArrayList<String>();
      currentAddresses = new ArrayList<Integer>();

      currentAddresses.add(address);
      currentInstructions.add(instruction);
   }
    * 
    */

   /*
   public void addUnit(TraceUnit unit) {
      // Check if LoopUnit is empty
      if(loopUnits.isEmpty()) {
         addFirstUnit(unit);
         return;
      }

      // Check if trace unit is already on the list
      instCounter += unit.getInstructions().size();
      iterations++;
   }
    *
    */
   /*

   public Integer getLastAddress() {
      return lastAddress;
   }

   public Integer getOffset() {
      return offset;
   }
    *
    */

   /*
   private void addFirstUnit(TraceUnit unit) {
      loopUnits.add(unit);
      id = unit.getIdentifier();
      iterations = 1;
      isLoop = false;

      instCounter += unit.getInstructions().size();
      currentIds.add(id);

      offset = unit.getInstructions().size();
      lastAddress = unit.getAddresses().get(offset-1);
   }
    *
    */

   // Data of the loop unit
   private List<TraceUnit> loopUnits;
   private Integer id;
   private int iterations;
   private int totalInstructions;
   private boolean isLoop;
   private boolean allInstructionsStored;


   //private Set<Integer> currentIds;

   //private int instCounter;
   

   //private Integer lastAddress;
   //private Integer offset;

   //private List<String> currentInstructions;
   //private List<Integer> currentAddresses;



}
