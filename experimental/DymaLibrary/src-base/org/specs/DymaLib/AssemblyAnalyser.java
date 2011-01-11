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

package org.specs.DymaLib;

import java.util.Collection;
import org.specs.DymaLib.DataStructures.ConstantRegister;
import org.specs.DymaLib.DataStructures.LiveOut;

/**
 * Extracts information from a list of assembly instructions representing a
 * straight-line loop.
 *
 * <p>This step exits because we assume that we cannot extract information from
 * VeryBigInstructions which would need more than one pass over the instructions
 * (ex.: live-out determination), due to not be practical to store all VBIs when
 * doing a hardware implementation.
 *
 * @author Joao Bispo
 */
public interface AssemblyAnalyser {

   /**
    *
    * @return which registers are live-outs, with the number of the instruction
    * (according to the order in the input list) where they are written for the
    * last time
    */
   Collection<LiveOut> getLiveOuts();
   /**
    * @return which registers have a constant value through the straight-line loop,
    * and which value they have
    */
   Collection<ConstantRegister> getConstantRegisters();
}
