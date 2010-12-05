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

package org.specs.CoverageOverIterations;

import org.specs.DymaLib.Interfaces.TraceReader;


/**
 * Accumulator variable which
 *
 * @author Joao Bispo
 */
public class InstructionVerifier {

   public InstructionVerifier(TraceReader traceReader) {
      this.traceReader = traceReader;

      this.instructionsUnitBuilder = 0l;
   }



   public boolean verify() {
      return traceReader.getNumInstructions() == instructionsUnitBuilder;
   }

   public void addInstructions(long inst) {
      instructionsUnitBuilder+=inst;
   }

   public long getTraceReaderInst() {
      return traceReader.getNumInstructions();
   }

   public long getExternalInst() {
      return instructionsUnitBuilder;
   }



   /**
    * INSTANCE VARIABLES
    */
   private long instructionsUnitBuilder;
   private TraceReader traceReader;
}
