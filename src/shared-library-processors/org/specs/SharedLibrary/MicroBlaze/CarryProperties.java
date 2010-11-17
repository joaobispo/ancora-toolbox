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

package org.specs.SharedLibrary.MicroBlaze;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Information about the carry in MicroBlaze operations.
 *
 * @author Joao Bispo
 */
public class CarryProperties {

   public CarryProperties(boolean hasCarryIn, boolean hasCarryOut) {
      this.hasCarryIn = hasCarryIn;
      this.hasCarryOut = hasCarryOut;
   }

   /**
    * INSTANCE VARIABLES
    */
   private boolean hasCarryIn;
   private boolean hasCarryOut;
   private static final Map<InstructionName, CarryProperties> instructionProperties;

   static {
      Map<InstructionName, CarryProperties> aMap = new EnumMap<InstructionName, CarryProperties>(InstructionName.class);

      aMap.put(InstructionName.add, new CarryProperties(false, true));
      aMap.put(InstructionName.addc, new CarryProperties(true, true));
      aMap.put(InstructionName.addi, new CarryProperties(false, true));
      aMap.put(InstructionName.addic, new CarryProperties(true, true));
      aMap.put(InstructionName.addik, new CarryProperties(false, false));
      aMap.put(InstructionName.addikc, new CarryProperties(true, false));
      aMap.put(InstructionName.addk, new CarryProperties(false, false));
      aMap.put(InstructionName.addkc, new CarryProperties(true, false));

      aMap.put(InstructionName.rsub, new CarryProperties(false, true));
      aMap.put(InstructionName.rsubc, new CarryProperties(true, true));
      aMap.put(InstructionName.rsubi, new CarryProperties(false, true));
      aMap.put(InstructionName.rsubic, new CarryProperties(true, true));
      aMap.put(InstructionName.rsubik, new CarryProperties(false, false));
      aMap.put(InstructionName.rsubikc, new CarryProperties(true, false));
      aMap.put(InstructionName.rsubk, new CarryProperties(false, false));
      aMap.put(InstructionName.rsubkc, new CarryProperties(true, false));


      instructionProperties = Collections.unmodifiableMap(aMap);
   }


   public static boolean usesCarryIn(InstructionName instructionName) {
      CarryProperties props = instructionProperties.get(instructionName);

      if (props == null) {
         return false;
      }

      return props.hasCarryIn;
   }

   public static boolean usesCarryOut(InstructionName instructionName) {
      CarryProperties props = instructionProperties.get(instructionName);

      if (props == null) {
         return false;
      }

      return props.hasCarryOut;
   }

}


