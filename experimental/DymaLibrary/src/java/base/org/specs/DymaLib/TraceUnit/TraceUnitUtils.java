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

package org.specs.DymaLib.TraceUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods related to TraceUnits.
 *
 * @author Joao Bispo
 */
public class TraceUnitUtils {

   public static List<String> extractInstructionsWithAddresses(List<TraceUnit> traceUnits) {
      List<String> instructions = new ArrayList<String>();

      for(TraceUnit traceUnit : traceUnits) {
         for(int i=0; i<traceUnit.getInstructions().size(); i++) {
            instructions.add(traceUnit.getAddresses().get(i) + ": "+traceUnit.getInstructions().get(i));
         }
      }

      return instructions;
   }

   public static List<String> extractInstructions(List<TraceUnit> traceUnits) {
      List<String> instructions = new ArrayList<String>();

      for(TraceUnit traceUnit : traceUnits) {
         for(int i=0; i<traceUnit.getInstructions().size(); i++) {
            instructions.add(traceUnit.getInstructions().get(i));
         }
      }

      return instructions;
   }

   public static List<Integer> extractAddresses(List<TraceUnit> traceUnits) {
      List<Integer> instructions = new ArrayList<Integer>();

      for(TraceUnit traceUnit : traceUnits) {
         for(int i=0; i<traceUnit.getInstructions().size(); i++) {
            instructions.add(traceUnit.getAddresses().get(i));
         }
      }

      return instructions;
   }


}
