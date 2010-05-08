/*
 *  Copyright 2010 Ancora Research Group.
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

package org.ancora.DTool;

/**
 * Represents a trace line.
 *
 * @author Joao Bispo
 */
public class TraceLine {

   public TraceLine(int address, String instruction) {
      this.address = address;
      this.instruction = instruction;
   }

   public int getAddress() {
      return address;
   }

   public String getInstruction() {
      return instruction;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private String instruction;
}
