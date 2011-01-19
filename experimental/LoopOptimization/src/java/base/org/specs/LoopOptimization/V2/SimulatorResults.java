/*
 *  Copyright 2011 SuikaSoft.
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

package org.specs.LoopOptimization.V2;

import java.util.List;
import org.specs.DymaLib.Assembly.CodeSegment;

/**
 *
 * @author Joao Bispo
 */
public class SimulatorResults {

   public SimulatorResults(long totalInstructions, long totalCycles, List<CodeSegment> foundLoops) {
      this.totalInstructions = totalInstructions;
      this.totalCycles = totalCycles;
      this.foundLoops = foundLoops;
   }

   @Override
   public String toString() {
      return "Total instructions:"+totalInstructions+"\n"
              + "Found Loops:"+foundLoops;
   }

   

   public final long totalInstructions;
   public final long totalCycles;
   public final List<CodeSegment> foundLoops;
}
