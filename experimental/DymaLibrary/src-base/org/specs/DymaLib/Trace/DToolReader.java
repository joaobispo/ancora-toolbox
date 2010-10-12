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

package org.specs.DymaLib.Trace;

import org.specs.DToolPlus.Utils.EasySystem;

/**
 * TraceReader implementation using a EasySystem object from DTool.
 *
 * @author Joao Bispo
 */
public class DToolReader implements TraceReader {

   public DToolReader(EasySystem system) {
      this.system = system;
   }


   public String nextInstruction() {
      system.step();
      address = system.getPc();
      numInstructions++;
      return system.getInstruction();
   }

   public Integer getAddress() {
      return address;
   }

   public long getNumInstructions() {
      return numInstructions;
   }

   private EasySystem system;

   private Integer address;
   private long numInstructions;
}
