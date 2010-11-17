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

package org.specs.DymaLib.LowLevelInstruction;

import java.util.List;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;

/**
 * Decodes an assembly instruction in its correspondent LowLevelInstructions.
 *
 * @author Joao Bispo
 */
public interface LowLevelParser {

   /**
    * Feeds an instruction to this LowLevelDecoder.
    *
    * @param address
    * @param instruction
    */
   void nextInstruction(int address, String instruction);

   /**
    * Indicates that the stream of instructions has ended.
    */
   void close();

   /**
    *
    * @return a list of the LowLevelInstructions found. After returning a non-null
    * list, it is cleared from the UnitBuilder. Returns null if there are no
    * LowLevelInstructions at the moment.
    */
   List<LowLevelInstruction> getAndClearUnits();

   /**
    * Processes the given instructions and returns an object with the information
    * already parsed.
    *
    * @param addresses
    * @param instructions
    * @return
    */
   List<LowLevelInstruction> parseInstructions(List<Integer> addresses,
           List<String> instructions);
}
