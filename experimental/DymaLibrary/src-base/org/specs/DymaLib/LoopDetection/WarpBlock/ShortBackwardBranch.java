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

package org.specs.DymaLib.LoopDetection.WarpBlock;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.InstructionDecoder;

/**
 *
 * @author Joao Bispo
 */
public class ShortBackwardBranch {

   //public ShortBackwardBranch(InstructionDecoder decoder, boolean limitBackwardJump, int backwardJumpMaxSize) {
   public ShortBackwardBranch(InstructionDecoder decoder) {
      this.decoder = decoder;
      //this.limitBackwardJump = limitBackwardJump;
      //this.backwardJumpMaxSize = backwardJumpMaxSize;


      completedData = null;
      endAddr = null;
      currentDelaySlot = null;
   }
   
   public void step(int address, String instruction) {
      /*
      if(address == 496) {
            System.out.println("Previous Address:"+endAddr);
            System.out.println("Current Address:"+address);
            System.out.println("Is Backward:"+backwardBranch(address));
         }
*/
      // If != null it means the previous instruction was a Branch
      if (endAddr != null) {

         /*
         if (shortBackwardBranch(address)) {
            buildBranchData(address);
         } else {
            endAddr = null;
         }
          *
          */
         // If last instruction was a backward branch, build branch data
         if (backwardBranch(address)) {
            buildBranchData(address);
         }
         endAddr = null;
      }

      // Check for SBB
      /*
      if(address == 860) {
         System.out.println("Address:"+address);
         System.out.println("Instruction:"+instruction);
         System.out.println("Is branch?:"+isBranch(instruction));
         System.out.println("Delay slot:"+currentDelaySlot);
      }
       * 
       */
      if (isBranch(instruction)) {
         endAddr = address;
      }
   }

   public BranchData getAndClearData() {
      BranchData returnData = completedData;
      completedData = null;
      return returnData;

   }

   private boolean isBranch(String instruction) {
      if(currentDelaySlot != null) {
         if(currentDelaySlot > 0) {
            currentDelaySlot--;
            return false;
         }

         currentDelaySlot = null;
         return true;
      }

      // If not jump, return false
      if(!decoder.isJump(instruction)) {
         return false;
      }

      // Check number of delay slots
      int delaySlot = decoder.delaySlot(instruction);
      if(delaySlot == 0) {
         return true;
      }

      currentDelaySlot = delaySlot;
      currentDelaySlot--;
      return false;
   }

/*
   private boolean shortBackwardBranch(int currentAddress) {
      // Forward jump
      //if(endAddr >= currentAddress) {
      if(currentAddress >= endAddr) {
         return false;
      }

      // If limit, check maximum size
      if(limitBackwardJump) {
         int offset = endAddr - currentAddress;
         if(offset > backwardJumpMaxSize) {
            return false;
         }
      }

      return true;
   }
   */
   private boolean backwardBranch(int currentAddress) {
// Forward jump
      //if(endAddr >= currentAddress) {
      if(currentAddress == endAddr) {
         LoggingUtils.getLogger().
                 warning("Jump to itself.");
         return true;
      }

      if(currentAddress > endAddr) {
         return false;
      }

      // If limit, check maximum size
      /*
      if(limitBackwardJump) {
         int offset = endAddr - currentAddress;
         if(offset > backwardJumpMaxSize) {
            return false;
         }
      }
       *
       */

      return true;
   }

      private void buildBranchData(int address) {
         int offset = endAddr - address;
         int endAddress = endAddr;
         int startAddress = address;

         if(completedData != null) {
            LoggingUtils.getLogger().
                    warning("Completed data is not null; Loosing data, please make "
                    + "sure to get data before it is overwritten.");
         }

         completedData = new BranchData(startAddress, endAddress, offset);
         //endAddr = null;
   }

   private Integer endAddr;

   private InstructionDecoder decoder;
   //private boolean limitBackwardJump;
   //private int backwardJumpMaxSize;

   private Integer currentDelaySlot;

   private BranchData completedData;







}
