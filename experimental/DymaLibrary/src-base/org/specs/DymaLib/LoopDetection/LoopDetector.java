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

package org.specs.DymaLib.LoopDetection;

import java.util.List;

/**
 * Interface for LoopDetectors
 *
 * @author Joao Bispo
 */
public interface LoopDetector {

   /**
    * A step of the loop detector.
    * 
    * @param address
    * @param instruction
    */
   void step(int address, String instruction);

   /**
    *
    * @return true if a loop was found in the current step
    */
   //boolean foundLoop();

   /**
    *
    * @return true if a loop terminated in the current step
    */
   //boolean loopEnded();

   /**
    * @return the start address of the loop, or null if no loop was found
    */
   //Integer getStartAddress();

   /**
    *
    * @return the size, in instructions, of the loop, or null if no loop was
    * found
    */
   //Integer getLoopSize();

   /**
    * Finishes any work that might be unfinished at this point.
    */
   public void close();


   /**
    *
    * @return a list of LoopUnits with the loops found up to this point, or
    * null if there are no completed loops.
    */
   public List<CodeSegment> getAndClearUnits();
}
