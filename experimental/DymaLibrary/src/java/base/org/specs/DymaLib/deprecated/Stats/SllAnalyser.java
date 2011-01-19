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

package org.specs.DymaLib.deprecated.Stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.specs.DymaLib.deprecated.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.deprecated.LowLevelInstruction.Elements.Operand;

/**
 * Analyses Straight-Line Loops.
 *
 * @author Joao Bispo
 */
public class SllAnalyser {

   public SllAnalyser() {
      //liveIns = new HashSet<Integer>();
      //written = new HashSet<Integer>();
      liveIns = new HashSet<String>();
      written = new HashSet<String>();
   }



   public static SllAnalyser analyse(List<LowLevelInstruction> instructions) {
      SllAnalyser analyser = new SllAnalyser();

      for(LowLevelInstruction inst : instructions) {
         analyser.next(inst);
      }

      return analyser;
   }

   private void next(LowLevelInstruction inst) {


      // Check inputs
//      for(int i=0; i<inst.operands.length; i++) {
//         if(inst.operands[i].flow != Operand.FLOW_INPUT) {
      for(int i=0; i<inst.operands.size(); i++) {
         if(inst.operands.get(i).flow != Operand.FLOW_INPUT) {
            continue;
         }

//         if(inst.operands[i].type != Operand.TYPE_REGISTER) {
         if(inst.operands.get(i).type != Operand.TYPE_REGISTER) {
            continue;
         }

         // Check if input register is already in written set
         //if(!written.contains(inst.operands[i].value)) {
         if(!written.contains(inst.operands.get(i).value)) {
            // Is a live-in
            //liveIns.add(inst.operands[i].value);
            liveIns.add(inst.operands.get(i).value);
         }
      }

      // Check outputs
      //for(int i=0; i<inst.operands.length; i++) {
      for(int i=0; i<inst.operands.size(); i++) {
         //Operand operand = inst.operands[i];
         Operand operand = inst.operands.get(i);
         if(operand.flow != Operand.FLOW_OUTPUT) {
            continue;
         }

         if(operand.type != Operand.TYPE_REGISTER) {
            continue;
         }

         // Add output register to written set
         written.add(operand.value);
      }

   }

   //public Set<Integer> getLiveIns() {
   public Set<String> getLiveIns() {
      return liveIns;
   }

//   public Set<Integer> getLiveOuts() {
   public Set<String> getLiveOuts() {
      return written;
   }

   //public Set<Integer> getImmutableRegisters() {
   public Set<String> getImmutableRegisters() {
      //Set<Integer> immRegs = new HashSet<Integer>();
      Set<String> immRegs = new HashSet<String>();

      // Add any live-in which was not written
      //for(Integer reg : liveIns) {
      for(String reg : liveIns) {
         if(!written.contains(reg)) {
            immRegs.add(reg);
         }
      }

      return immRegs;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("LiveIns:");
      builder.append(getLiveIns());
      builder.append("\n");

      builder.append("LiveOuts:");
      builder.append(getLiveOuts());
      builder.append("\n");

      builder.append("Constant:");
      builder.append(getImmutableRegisters());
      builder.append("\n");

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   //private Set<Integer> liveIns;
   private Set<String> liveIns;
   //private Set<Integer> written;
   private Set<String> written;
}
