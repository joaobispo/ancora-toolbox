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
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.Utils.PatternDetector.PatternState;

/**
 *
 * @author Joao Bispo
 */
public class TraceUnitCollector {

   public TraceUnitCollector() {
      //traceUnits = new ArrayList<TraceUnit>();
      tempTraceUnits = null;
      completeTraceUnits = null;
      //collecting = false;
      currentState = CollectorState.LOOKING_FOR_PATTERN;
      repetitions = 0;
      //newCollection = false;
      //patternSize = 0;
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

   public void step(TraceUnit traceUnit, PatternState state, int patternSize) {
         switch (currentState) {
            case LOOKING_FOR_PATTERN:
               stateLookingForPattern(traceUnit, state, patternSize);
               break;

            case BUILDING_PATTERN:
               stateBuildingPattern(traceUnit, state, patternSize);
               break;

            case CHECKING_PATTERN:
               stateCheckingPattern(traceUnit, state, patternSize);
               break;
         }
      }

   private void stateLookingForPattern(TraceUnit traceUnit, PatternState state, int patternSize) {
      // Check if there is a pattern
      if (state == PatternState.PATTERN_STARTED || state == PatternState.PATTERN_CHANGED_SIZES) {

         // Start block
         tempTraceUnits = new ArrayList<TraceUnit>();

         // Prepare Data
         currentState = CollectorState.BUILDING_PATTERN;

         // Start processing pattern
         stateBuildingPattern(traceUnit, state, patternSize);

         return;
      }

   }

      private void stateBuildingPattern(TraceUnit traceUnit, PatternState state, int patternSize) {
         // Pattern is bulding and continuing
         if(state == PatternState.PATTERN_STARTED || state == PatternState.PATTERN_UNCHANGED) {
            // Check size
            if(tempTraceUnits.size() < patternSize) {
               tempTraceUnits.add(traceUnit);
            }
            
            if(tempTraceUnits.size() == patternSize) {
               currentState = CollectorState.CHECKING_PATTERN;
               completeTraceUnits = tempTraceUnits;
               tempTraceUnits = new ArrayList<TraceUnit>();
               repetitions = 1;
               
               //stateCheckingPattern(traceUnit, state, patternSize);
            }
            
            return;
         }

         // Pattern stopped before completing a set
         tempTraceUnits = null;
         currentState = CollectorState.LOOKING_FOR_PATTERN;
         stateLookingForPattern(traceUnit, state, patternSize);
      }

      private void stateCheckingPattern(TraceUnit traceUnit, PatternState state, int patternSize) {
         if(state != PatternState.PATTERN_UNCHANGED) {
            // Pattern stopped before completing a set
            tempTraceUnits = null;
            currentState = CollectorState.LOOKING_FOR_PATTERN;
            stateLookingForPattern(traceUnit, state, patternSize);
         }
      }


   /*
   public void step(TraceUnit traceUnit) {
      if(collecting) {
         if(tempTraceUnits.size() < patternSize) {
            tempTraceUnits.add(traceUnit);
            newCollection = false;
         } else if(tempTraceUnits.size() == patternSize){
            completeTraceUnits = tempTraceUnits;
            newCollection = true;
         } else {
            newCollection = false;
         }
      } else {
         newCollection = false;
      }
   }
*/
   /*
   public List<TraceUnit> getPreviousTraceUnits() {
      return previousTraceUnits;
   }
    * 
    */

   public List<TraceUnit> getAndClearUnits() {
      List<TraceUnit> returnList = completeTraceUnits;
      completeTraceUnits = null;
      return returnList;
   }

   /*
   public boolean isNewCollection() {
      return newCollection;
   }
    *
    */
   enum CollectorState {
      LOOKING_FOR_PATTERN,
      BUILDING_PATTERN,
      CHECKING_PATTERN;
   }

   private List<TraceUnit> tempTraceUnits;
   private List<TraceUnit> completeTraceUnits;
   //private boolean newCollection;
//   private boolean collecting;
   private CollectorState currentState;
   private int repetitions;
   //private int patternSize;
}
