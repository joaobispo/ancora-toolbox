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

/**
 *
 * @author Joao Bispo
 */
public class TraceUnitCollector {

   public TraceUnitCollector() {
      //traceUnits = new ArrayList<TraceUnit>();
      currentTraceUnits = null;
      previousTraceUnits = null;
      collecting = false;
      newCollection = false;
      patternSize = 0;
   }

   public void startCollecting(int patternSize) {
      currentTraceUnits = new ArrayList<TraceUnit>();
      collecting = true;
      this.patternSize = patternSize;
   }

   public void stopCollecting() {
      //previousTraceUnits = currentTraceUnits;
      currentTraceUnits = null;
      collecting = false;
      //newCollection = true;
      patternSize = 0;
   }

   public void step(TraceUnit traceUnit) {
      if(collecting) {
         if(currentTraceUnits.size() < patternSize) {
            currentTraceUnits.add(traceUnit);
            newCollection = false;
         } else if(currentTraceUnits.size() == patternSize){
            previousTraceUnits = currentTraceUnits;
            newCollection = true;
         } else {
            newCollection = false;
         }
      } else {
         newCollection = false;
      }
   }

   public List<TraceUnit> getPreviousTraceUnits() {
      return previousTraceUnits;
   }

   public boolean isNewCollection() {
      return newCollection;
   }

   

   private List<TraceUnit> currentTraceUnits;
   private List<TraceUnit> previousTraceUnits;
   private boolean newCollection;
   private boolean collecting;
   private int patternSize;
}
