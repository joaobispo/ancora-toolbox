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

package org.specs.DymaLib.Utils;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockUnit;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.Utils.PatternDetector.PatternState;

/**
 *
 * @author Joao Bispo
 */
public class TraceUnitCollector {

   public TraceUnitCollector(boolean storeSequenceInstructions) {
      //traceUnits = new ArrayList<TraceUnit>();
      //tempTraceUnits = null;
      //completeTraceUnits = null;
      this.storeSequenceInstructions = storeSequenceInstructions;
      //collecting = false;
      //currentState = CollectorState.LOOKING_FOR_PATTERN;
      //repetitions = 0;
      //newCollection = false;
      //patternSize = 0;
      // Always start with a Sequence
      completedUnits = new ArrayList<MegaBlockUnit>();
      // Initialize currentLoop
      currentLoop = null;
      initSequence();
   }



   /*
   public void startCollecting(int patternSize) {
      tempTraceUnits = new ArrayList<TraceUnit>();
      collecting = true;
      this.patternSize = patternSize;
   }

   public void stopCollecting() {
      //previousTraceUnits = currentTraceUnits;
      tempTraceUnits = null;
      collecting = false;
      //newCollection = true;
      patternSize = 0;
   }
*/

   /**
    * Builds loops.
    *
    * @param traceUnit
    * @param state
    * @param patternSize
    */
   /*
   public void step(TraceUnit traceUnit, PatternState state, int patternSize) {
      switch(state) {
         case NO_PATTERN:
            break;
         case PATTERN_STARTED:
            initList();
            addTraceUnit(traceUnit);
            checkCompleteness(patternSize);
            break;
         case PATTERN_UNCHANGED:
            if(tempTraceUnits != null) {
               addTraceUnit(traceUnit);
               checkCompleteness(patternSize);
            }
            break;
         case PATTERN_CHANGED_SIZES:
            initList();
            addTraceUnit(traceUnit);
            checkCompleteness(patternSize);
            break;
         case PATTERN_STOPED:
            nullifyList();
            break;
         default:
            LoggingUtils.getLogger().
                    warning("Case not defined:"+state);
            break;
      }
   }
*/

   /**
    * Builds LoopUnits.
    * 
    * @param traceUnit
    * @param state
    * @param patternSize
    */
   public void step(TraceUnit traceUnit, PatternState state, int patternSize) {
      switch(state) {
         case NO_PATTERN:
            addToSequence(traceUnit);
            break;
         case PATTERN_STARTED:
            wrapUpSequence();
            initLoop(patternSize);
            addToLoop(traceUnit);
            break;
         case PATTERN_UNCHANGED:
            addToLoop(traceUnit);
            break;
         case PATTERN_CHANGED_SIZES:
            wrapUpLoop();
            wrapUpSequence();
            initLoop(patternSize);
            addToLoop(traceUnit);
            break;
         case PATTERN_STOPED:
            wrapUpLoop();
            addToSequence(traceUnit);
            break;
         default:
            LoggingUtils.getLogger().
                    warning("Case not defined:"+state);
            break;
      }
   }

   /*
   public List<TraceUnit> getAndClearUnits() {
      List<TraceUnit> returnList = completeTraceUnits;
      completeTraceUnits = null;
      return returnList;
   }
*/

   public List<MegaBlockUnit> getAndClearUnits() {
      if(completedUnits.isEmpty()) {
         return null;
      }

      List<MegaBlockUnit> returnList = completedUnits;
      completedUnits = new ArrayList<MegaBlockUnit>();
      return returnList;
   }

   public void close() {
      if(currentLoop.getType() == MegaBlockUnit.Type.Loop) {
         MegaBlockUnit loop = currentLoop.splitUnit(storeSequenceInstructions);
         if(loop != null) {
            completedUnits.add(loop);
         }
      }

      // Completed unit here can only be of type Sequence
      if(currentLoop.getType() != MegaBlockUnit.Type.Sequence) {
         LoggingUtils.getLogger().
                 warning("Bug. CurrentLoop should be of type '"+MegaBlockUnit.Type.Sequence+"'");
      }
      completedUnits.add(currentLoop);
   }

/*
   private void initList() {
      tempTraceUnits = new ArrayList<TraceUnit>();
   }

   private void addTraceUnit(TraceUnit traceUnit) {
      tempTraceUnits.add(traceUnit);
   }

   private void checkCompleteness(int patternSize) {
      if(tempTraceUnits.size() == patternSize) {
         completeTraceUnits = tempTraceUnits;
         nullifyList();
      }
   }

   private void nullifyList() {
      tempTraceUnits = null;
   }
*/

   private void addToSequence(TraceUnit traceUnit) {
      add(traceUnit, MegaBlockUnit.Type.Sequence);
   }

   private void addToLoop(TraceUnit traceUnit) {
      add(traceUnit, MegaBlockUnit.Type.Loop);
   }

   private void add(TraceUnit traceUnit, MegaBlockUnit.Type type) {
      if(currentLoop.getType() != type) {
         LoggingUtils.getLogger().
                 warning("Bug. LoopUnit of type '"+currentLoop.getType()+
                 "' should be of type '"+type+"'");
         return;
      }
      currentLoop.addTraceUnit(traceUnit);
   }

   private void wrapUpSequence() {
      if(currentLoop.getType() != MegaBlockUnit.Type.Sequence) {
         LoggingUtils.getLogger().
                 warning("Bug. LoopUnit of type '"+currentLoop.getType()+
                 "' should be of type '"+MegaBlockUnit.Type.Sequence+"'");
         return;
      }

      // Check number of instructions
      if(currentLoop.getNumInstructions() != 0) {
         completedUnits.add(currentLoop);
      }
      
      currentLoop = null;
   }

   /**
    * When calling this method, the currentLoop at exit is alway of the type
    * Sequence.
    * 
    */
   private void wrapUpLoop() {
      if (currentLoop.getType() != MegaBlockUnit.Type.Loop) {
         LoggingUtils.getLogger().
                 warning("Bug. LoopUnit of type '" + currentLoop.getType()
                 + "' should be of type '" + MegaBlockUnit.Type.Loop + "'");
         return;
      }

      // Split Loop
      MegaBlockUnit loop = currentLoop.splitUnit(storeSequenceInstructions);
      if(loop != null) {
         completedUnits.add(loop);
      }
   }

   private void initLoop(int patternSize) {
      if(currentLoop != null) {
         LoggingUtils.getLogger().
                 warning("Initializing LoopUnit which is not null.");
      }
      currentLoop = new MegaBlockUnit(patternSize);
   }

   private void initSequence() {
      if(currentLoop != null) {
         LoggingUtils.getLogger().
                 warning("Initializing LoopUnit which is not null.");
      }
      currentLoop = new MegaBlockUnit(storeSequenceInstructions);
   }

   //private List<TraceUnit> tempTraceUnits;
   //private List<TraceUnit> completeTraceUnits;

   // Single loop unit and a list of completed units?
   //private List<LoopUnit> loopUnits;
   private MegaBlockUnit currentLoop;
   private List<MegaBlockUnit> completedUnits;
   private boolean storeSequenceInstructions;




}
