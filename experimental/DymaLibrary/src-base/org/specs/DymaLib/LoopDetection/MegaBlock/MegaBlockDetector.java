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

package org.specs.DymaLib.LoopDetection.MegaBlock;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.Utils.PatternDetector;
import org.specs.DymaLib.Utils.PatternFilter;
import org.specs.DymaLib.Utils.TraceUnitCollector;

/**
 * Detects MegaBlocks.
 *
 * @author Joao Bispo
 */
public class MegaBlockDetector implements LoopDetector {

   public MegaBlockDetector(UnitBuilder unitBuilder, int maxPatternSize) {
      foundLoop = false;
      loopEnded = false;
      startAddress = null;
      loopSize = null;
      patternFilter = new PatternFilter();
      collector = new TraceUnitCollector();

      this.unitBuilder = unitBuilder;
      this.patternDetector = new PatternDetector(maxPatternSize);
   }



   public void step(int address, String instruction) {
      unitBuilder.nextInstruction(address, instruction);
      List<TraceUnit> traceUnits = unitBuilder.getAndClearUnits();

      for(TraceUnit traceUnit : traceUnits) {
         processTraceUnit(traceUnit);

         // Implementar estados - é mais fácil assim
         /*
         if(patternDetector.getPatternSize() > 0 && patternFilter.isNewPattern()) {
            collector.startCollecting(patternDetector.getPatternSize());
         } else if()
         */
         
      }
   }


   private void processTraceUnit(TraceUnit traceUnit) {
      patternDetector.step(traceUnit.getIdentifier());
      collector.step(traceUnit, patternDetector.getState(), patternDetector.getPatternSize());

      List<TraceUnit> megablock = collector.getAndClearUnits();
      if(megablock != null) {
         System.out.println("Found megablock:");
         for(TraceUnit unit : megablock) {
            System.out.println(unit.getInstructions());
         }

      }

      //patternFilter.step(patternDetector.getPatternSize());

      //collector.step(traceUnit);
/*
      if(patternFilter.isNewPattern()) {
        if(patternDetector.getPatternSize() > 0) {
//            System.out.print(" (repetitions:"+patternFilter.getPreviousRepetitions()+")\n"+"New pattern:"+patternDetector.getPatternSize());
         }
      }
*/
 }


   private void startCollecting() {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   public boolean foundLoop() {
      return  foundLoop;
   }

   public boolean loopEnded() {
      return loopEnded;
   }

   public Integer getStartAddress() {
      return startAddress;
   }

   public Integer getLoopSize() {
      return loopSize;
   }

   private boolean foundLoop;
   private boolean loopEnded;
   private Integer startAddress;
   private Integer loopSize;

   private UnitBuilder unitBuilder;
   private PatternDetector patternDetector;
   private PatternFilter patternFilter;
   private TraceUnitCollector collector;



}
