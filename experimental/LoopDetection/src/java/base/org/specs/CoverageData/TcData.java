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

package org.specs.CoverageData;

import java.util.HashMap;
import java.util.Map;
import org.specs.DymaLib.Assembly.CodeSegment;

/**
 * Stores the number of instruction per repetition in InstructionBlocks
 *
 * @author Joao Bispo
 */
public class TcData {

   public TcData() {
      instPerRepetitions = new HashMap<Integer, Long>();
      totalInstructions = 0;
   }


   public void addBlock(CodeSegment segment) {
      int iterations = segment.getIterations();
      // Get current instructions
      Long instructions = instPerRepetitions.get(iterations);
      if(instructions == null) {
         instructions = 0l;
      }
      instructions += segment.getTotalInstructions();
      totalInstructions += segment.getTotalInstructions();

      instPerRepetitions.put(iterations, instructions);
   }

   @Override
   public String toString() {
      //System.err.println("Inst:"+totalInstructions+". For "+filename);
      return instPerRepetitions.toString();
   }

   public long getTotalInstructions() {
      return totalInstructions;
   }

   public Map<Integer, Long> getInstPerRepetitions() {
      return instPerRepetitions;
   }

   /**
    * INSTANCE VARIABLES
    */
   private Map<Integer, Long> instPerRepetitions;
   private long totalInstructions;

}
