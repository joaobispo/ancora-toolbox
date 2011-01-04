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

package org.specs.DymaLib.TraceUnit;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a unit, composed of instructions, in a trace.
 *
 * <p>A unit can range from the instruction itself, to BasicBlocks and
 * SuperBlocks.
 *
 * @author Joao Bispo
 */
public class TraceUnit implements Serializable {

   public TraceUnit(List<String> instructions, List<Integer> addresses, int identifier) {
      this.instructions = instructions;
      this.addresses = addresses;
      this.identifier = identifier;
   }

   public List<Integer> getAddresses() {
      return addresses;
   }

   public int getIdentifier() {
      return identifier;
   }

   public List<String> getInstructions() {
      return instructions;
   }

   

   private List<String> instructions;
   private List<Integer> addresses;
   private int identifier;
}
