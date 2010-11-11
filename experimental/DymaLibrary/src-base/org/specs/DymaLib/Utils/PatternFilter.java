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

/**
 * Filters the results of PatternDetector
 *
 * TO REMOVE
 * 
 * @author Joao Bispo
 * @deprecated
 */
public class PatternFilter {

   public PatternFilter() {
      patternSize = 0;
      patternChanged = false;
      repetitionsCounter = 0;
   }

public void step(int newPatternSize) {
   resetFlags();
   if(newPatternSize != patternSize) {
      patternChanged = true;
      patternSize = newPatternSize;
      previousRepetitions = repetitionsCounter;
      repetitionsCounter = 1;
   } else {
      repetitionsCounter++;
   }
}


   private void resetFlags() {
      patternChanged = false;
   }

   public int getPatternSize() {
      return patternSize;
   }

   public boolean isNewPattern() {
      return patternChanged;
   }

   public int getPreviousRepetitions() {
      return previousRepetitions;
   }




   private int patternSize;
   private boolean patternChanged;
   private int repetitionsCounter;
   private int previousRepetitions;

}
