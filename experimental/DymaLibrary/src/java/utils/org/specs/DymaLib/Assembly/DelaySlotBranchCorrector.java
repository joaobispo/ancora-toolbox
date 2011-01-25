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
package org.specs.DymaLib.Assembly;

/**
 * Indicates when is the jump in architectures with delay slots.
 *
 * @author Joao Bispo
 */
public class DelaySlotBranchCorrector {

   public DelaySlotBranchCorrector() {
      currentDelaySlot = 0;
   }

   public boolean isJump(boolean isJump, int delaySlots) {
      // If we are currently in a delay slot that is not the last,
      // just decrement.
      if (currentDelaySlot > 1) {
         currentDelaySlot--;
         return false;
      }

      // This is the last delay slot. This instruction will jump.
      if (currentDelaySlot == 1) {
         currentDelaySlot--;
         return true;
      }


      // Check if it is a jump instruction
      if (isJump) {
         return processJump(delaySlots);
      }

      // It is not a jump instruction
      return false;
   }

   private boolean processJump(int delaySlots) {
      // Check if it has delay slots
      if (delaySlots > 0) {
         currentDelaySlot = delaySlots;
         return false;
      } else {
         return true;
      }
   }
   
   private int currentDelaySlot;
}
