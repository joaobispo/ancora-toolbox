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

package org.specs.DymaLib.PreAnalysis;

import org.specs.DymaLib.Liveness.LivenessAnalysis;

/**
 * Contains the results of an analysis to a list of assembly instructions 
 * representing a straight-line loop.
 *
 * <p>This class exits because we assume that we cannot extract information from
 * VeryBigInstructions which would need more than one pass over the instructions
 * (ex.: live-out determination), due to not be practical to store all VBIs when
 * doing a hardware implementation.
 *
 * @author Joao Bispo
 */
public class AssemblyAnalysis {

   //public AssemblyAnalysis(Collection<LiveOut> liveOuts, Collection<ConstantRegister> constantRegisters, boolean hasStores, Collection<String> liveIns) {
   public AssemblyAnalysis(LivenessAnalysis livenessAnalysis, boolean hasStores) {
      //this.liveOuts = liveOuts;
      //this.constantRegisters = constantRegisters;
      this.hasStores = hasStores;
      //this.liveIns = liveIns;
      this.livenessAnalysis = livenessAnalysis;
   }


 @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("Constant Registers:\n");

      //builder.append(constantRegisters);
      //builder.append(getConstantRegisters());
      builder.append(livenessAnalysis.constantRegisters);
      builder.append("\n");

      builder.append("Live Outs:\n");
      //builder.append(liveOuts);
      //builder.append(getLiveOuts());
      builder.append(livenessAnalysis.liveOuts);
      builder.append("\n");

      builder.append("Has Stores?:");
      //builder.append(hasStores);
      //builder.append(hasStores());
      builder.append(hasStores);
      builder.append("\n");

      return builder.toString();
   }

   /**
    * A collection with the operands which were considered live-outs.
    * A live-out is defined as being an operand which is both an output (not input)
    * and mutable (not constant).
    *
    * <p>Includes the number of the instruction (according to the order the
    * operands were fed) where they are written for the last time.
    * 
    * @return
    */
 /*
   public Collection<LiveOut> getLiveOuts() {
      return livenessAnalysis.liveOuts;
   }
*/
   /**
    * The identifiers of the live-ins.
    *
    * @return
    */
 /*
   public Collection<String> getLiveIns() {
      return livenessAnalysis.liveIns;
   }
*/
   /**
    * Which registers have a constant value through the MegaBlock,
    * and which value they have.
    *
    * @return
    */
 /*
   public Collection<ConstantRegister> getConstantRegisters() {
      return livenessAnalysis.constantRegisters;
   }
*/
   /**
    * True if the MegaBlock has store instructions.
    *
    * @return
    */
 /*
   public boolean hasStores() {
      return hasStores;
   }
*/


   /**
    * Which registers are live-outs, with the number of the instruction
    * (according to the order in the input list) where they are written for the
    * last time
    */
   //public final Collection<LiveOut> liveOuts;
   /**
    * Which registers have a constant value through the straight-line loop,
    * and which value they have
    */
   //public final Collection<ConstantRegister> constantRegisters;
   /**
    * True if the loop has store instructions
    */
   public final boolean hasStores;

   //public final Collection<String> liveIns;

   public final LivenessAnalysis livenessAnalysis;
}
