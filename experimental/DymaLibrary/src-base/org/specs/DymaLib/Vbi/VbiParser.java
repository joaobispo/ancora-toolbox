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

package org.specs.DymaLib.Vbi;

import org.specs.DymaLib.DataStructures.VeryBigInstruction32;

/**
 * Parses strings of assembly instructions into Very Big Instructions and forth.
 *
 * @author Joao Bispo
 */
public interface VbiParser {

   /**
    * Converts an assembly instruction into a very big instruction.
    * 
    * @param instruction
    * @return
    */
   VeryBigInstruction32 parseInstruction(Object instruction);
   /**
    * Converts a very big instruction back into the format of the original instruction.
    * @param instruction
    * @return
    */
   //String parseVbi(VeryBigInstruction32 instruction);
}
