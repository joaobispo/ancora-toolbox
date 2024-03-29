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

import java.util.Map;
import org.suikasoft.SharedLibrary.Processors.RegisterId;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Represents a trace.
 *
 * <p>Delivers instructions and the corresponding instruction address.
 *
 * @author Joao Bispo
 */
public interface TraceReader {

   /**
    * 
    * @return the next instruction in the trace, or null if it reached the end
    */
   String nextInstruction();

   /**
    *
    * @return the address of the last returned instruction, or null if there is
    * no valid address
    */
   Integer getAddress();

   /**
    *
    * @return the total number of returned instructions up to this moment
    */
   long getNumInstructions();

   /**
    * Optional: returns the values of the registers at the current moment.
    *
    * @return null if not implemented.
    */
   //Map<RegisterId, Integer> getRegisters();
   RegisterTable getRegisters();

   /**
    *
    * @return the total number of cycles up to this moment
    */
   long getCycles();
}
