/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.SharedLibrary.Identification;

/**
 * Generates integers, incrementally, which can be used to uniquely identify
 * objects.
 * 
 * @author Joao Bispo
 */
public class IntIdentifier {

   /**
    * Creates a IntIdentifier that will generate integers starting from 0.
    */
   public IntIdentifier() {
      this(0);
   }


   /**
    * Creates a IntIdentifier that will generate integers starting from the
    * given value, inclusive.
    *
    * @param startValue
    */
   public IntIdentifier(int startValue) {
      this.currentValue = startValue;
   }


   /**
    *
    * <p>If newInt returns MAX_VALUE, the next value will be MIN_VALUE and
    * it will decrement until it reaches 0.
    *
    * @return an integer which is equal to the last given integer, incremented
    * by one.
    */
   public int newInt() {
      final int returnInt = currentValue;
      currentValue++;

      return returnInt;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int currentValue;

}
