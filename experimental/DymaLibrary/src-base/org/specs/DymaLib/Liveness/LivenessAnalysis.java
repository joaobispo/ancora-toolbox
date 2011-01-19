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

package org.specs.DymaLib.Liveness;

import java.util.Collection;
import org.specs.DymaLib.Assembly.ConstantRegister;
import org.specs.DymaLib.Assembly.LiveOut;

/**
 * Contains the results of an analysis to operands from a list of instruction
 * representing a MegaBlock.
 *
 * @author Joao Bispo
 */
public class LivenessAnalysis {

   public LivenessAnalysis(Collection<ConstantRegister> constantRegisters, Collection<LiveOut> liveOuts, Collection<String> liveIns) {
      this.constantRegisters = constantRegisters;
      this.liveOuts = liveOuts;
      this.liveIns = liveIns;
   }

   

   /**
    * A collection with the constant registers found by the analyser.
    */
   public final Collection<ConstantRegister> constantRegisters;
   /**
    * A collection with the operands which were considered live-outs.
    * A live-out is defined as being an operand which is both an output (not input)
    * and mutable (not constant).
    */
   public final Collection<LiveOut> liveOuts;
   /**
    * The identifiers of the live-ins.
    */
   public final Collection<String> liveIns;
}
