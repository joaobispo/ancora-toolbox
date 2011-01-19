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

package org.specs.DymaLib;

/**
 * Extracts information from an instruction in String format.
 *
 * <p>Avoid using state in this class, in case other classes make subsequent use
 * of the same InstructionDecoder object.
 *
 * @author Joao Bispo
 */
public interface InstructionDecoder {
   /**
    *
    * @param instruction
    * @return true if instruction is a jump. False otherwise.
    */
   boolean isJump(String instruction);
   /**
    *
    * @param instruction
    * @return the number of delay slots after this instruction. Returns 0 if
    * the instruction has no delay slots, 1 if it as 1 delay slot, etc.
    */
   int delaySlot(String instruction);

   /**
    * Resets any state of the object.
    */
   //void reset();
}
