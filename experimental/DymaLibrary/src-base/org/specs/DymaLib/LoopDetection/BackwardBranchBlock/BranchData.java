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

package org.specs.DymaLib.LoopDetection.BackwardBranchBlock;

/**
 *
 * @author Joao Bispo
 */
public class BranchData {

   public BranchData(int startAddr, int endAddr, int offsetAddr) {
      this.startAddr = startAddr;
      this.endAddr = endAddr;
      this.offset = offsetAddr;
   }

   public boolean equals(BranchData data) {
      boolean sameStart = startAddr == data.startAddr;
      boolean sameEnd = endAddr == data.endAddr;
      boolean sameOffset = offset == data.offset;

      return sameEnd && sameOffset && sameStart;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("StartAddr:"+startAddr+"\n");
      builder.append("EndAddr:"+endAddr+"\n");
      builder.append("Offset:"+(endAddr-startAddr)+"\n");

      return builder.toString();
   }

   /**
    * A BranchData is part of another BranchData if it is "inside" the given
    * object.
    *
    * <p>It is equivalent to test if:
    * <br> this.startAddr >= givenBranchData.startAddr;
    * <br> this.endAddr <= givenBranchData.endAddr;
    * <br> this.offset <= givenBranchData.offset;
    * @param currentBranchData
    * @return
    */
   boolean isPartOf(BranchData currentBranchData) {
      boolean insideStart = startAddr >= currentBranchData.startAddr;
      boolean insideEnd = endAddr <= currentBranchData.endAddr;
      boolean insideOffset = offset <= currentBranchData.offset;

      return insideEnd && insideOffset && insideStart;
   }
   

   public final int startAddr;
   public final int endAddr;
   public final int offset;

}
