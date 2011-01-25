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
import org.specs.DymaLib.PreAnalysis.CodeSegment;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
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
      collector = new TraceUnitCollector(storeSequenceInstructions);
      this.onlyCollectLoops = onlyCollectLoops;
      foundLoops = new ArrayList<CodeSegment>();

      this.unitBuilder = unitBuilder;
      this.patternDetector = new PatternDetector(maxPatternSize);
   }



   public void step(int address, String instruction) {
      unitBuilder.nextInstruction(address, instruction);
      processUnitBuilder();
      // Get state of pattern to check if we found a loop
  /*
      if(patternDetector.getState() == PatternDetector.PatternState.PATTERN_STARTED) {
         return true;
      } else {
         return false;
      }
   *
   */
   }


   private void processTraceUnit(TraceUnit traceUnit) {
      patternDetector.step(traceUnit.getIdentifier());
      collector.step(traceUnit, patternDetector.getState(), patternDetector.getPatternSize());
      collect();
 }


   private void processUnitBuilder() {
      List<TraceUnit> traceUnits = unitBuilder.getAndClearUnits();
      
      for(TraceUnit traceUnit : traceUnits) {
         processTraceUnit(traceUnit);
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

      for (CodeSegment unit : megablocks) {
         boolean skipUnit = onlyCollectLoops && (!unit.isLoop());
         if (skipUnit) {
               continue;
         }

         foundLoops.add(unit);

      }
   }


   public List<CodeSegment> getAndClearUnits() {
      if(foundLoops.isEmpty()) {
         return null;
      }

      List<CodeSegment> returnList = foundLoops;
      foundLoops = new ArrayList<CodeSegment>();
      return returnList;
   }

   public String getId() {
      return LoopDetectors.MegaBlock.name() + patternDetector.getMaxPatternSize();
   }
   
   private UnitBuilder unitBuilder;
   private PatternDetector patternDetector;
   private TraceUnitCollector collector;

   private boolean onlyCollectLoops;
   private List<CodeSegment> foundLoops;

}
