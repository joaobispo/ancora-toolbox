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

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.Utils.PatternDetector;
import org.specs.DymaLib.Utils.TraceUnitCollector;

/**
 * Detects MegaBlocks.
 *
 * @author Joao Bispo
 */
public class MegaBlockDetector implements LoopDetector {

   public MegaBlockDetector(UnitBuilder unitBuilder, int maxPatternSize, boolean storeSequenceInstructions,
           boolean onlyCollectLoops) {
    //  foundLoop = false;
    //  loopEnded = false;
    //  startAddress = null;
    //  loopSize = null;
      //patternFilter = new PatternFilter();
      collector = new TraceUnitCollector(storeSequenceInstructions);
      this.onlyCollectLoops = onlyCollectLoops;
      foundLoops = new ArrayList<LoopUnit>();

      this.unitBuilder = unitBuilder;
      this.patternDetector = new PatternDetector(maxPatternSize);

     // loopUnit = new LoopUnit(false);
   }



   public void step(int address, String instruction) {
      unitBuilder.nextInstruction(address, instruction);
      processUnitBuilder();
      
   }


   private void processTraceUnit(TraceUnit traceUnit) {
      patternDetector.step(traceUnit.getIdentifier());
      collector.step(traceUnit, patternDetector.getState(), patternDetector.getPatternSize());
      collect();

//      loopUnit.addTraceUnit(traceUnit);

      //List<TraceUnit> megablock = collector.getAndClearUnits();
      /*
      List<LoopUnit> megablocks = collector.getAndClearUnits();
      if(megablocks != null) {
         //foundLoop = true;
         //System.out.println("Found megablock:");
         for(LoopUnit unit : megablocks) {
         //   System.out.println(unit.getInstructions());
            System.out.println(unit);
         }

      }
       *
       */


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

/*
   private void startCollecting() {
      throw new UnsupportedOperationException("Not yet implemented");
   }
*/


   private void processUnitBuilder() {
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

   public void close() {
      unitBuilder.close();
      processUnitBuilder();
      collector.close();
      collect();
   }

   private void collect() {
      List<MegaBlockUnit> megablocks = collector.getAndClearUnits();
      if (megablocks == null) {
         return;
      }

      for (LoopUnit unit : megablocks) {
         boolean skipUnit = onlyCollectLoops && (!unit.isLoop());
         if (skipUnit) {
               continue;
         }

         foundLoops.add(unit);

      }
   }


   public List<LoopUnit> getAndClearUnits() {
      if(foundLoops.isEmpty()) {
         return null;
      }

      List<LoopUnit> returnList = foundLoops;
      foundLoops = new ArrayList<LoopUnit>();
      return returnList;
   }

   /*
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
    *
    */
/*
   private boolean foundLoop;
   private boolean loopEnded;
   private Integer startAddress;
   private Integer loopSize;
*/

   //public LoopUnit loopUnit;

   private UnitBuilder unitBuilder;
   private PatternDetector patternDetector;
   //private PatternFilter patternFilter;
   private TraceUnitCollector collector;

   private boolean onlyCollectLoops;
   private List<LoopUnit> foundLoops;


   /*
   public void reset() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
    *
    */







}
