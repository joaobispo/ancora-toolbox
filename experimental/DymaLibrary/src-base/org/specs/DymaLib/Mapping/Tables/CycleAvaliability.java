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

package org.specs.DymaLib.Mapping.Tables;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Architecture.PeDisposition;
import org.specs.DymaLib.Mapping.Architecture.PeType;

/**
 * Represents working data representing PE ocupancy in a determined cycle.
 *
 * @author Joao Bispo
 */
public class CycleAvaliability {

   public CycleAvaliability(Architecture arch) {
      this.arch = arch;

      currentTypePointer = new EnumMap<PeType, Integer>(PeType.class);
      // For each type, create a pointer
      for(PeType type : arch.peDispositions.keySet()) {
         currentTypePointer.put(type, 0);
      }
   }

   /**
    * @param type
    * @return the first available position corresponding to the given type,
    * or null if no position could be found.
    * 
    */
   public Integer getPosition(PeType type) {
      Integer typePointer = currentTypePointer.get(type);
      if(typePointer == null) {
         LoggingUtils.getLogger().
                 warning("Type '"+type+"' not supported by this architecture.");
         return null;
      }

      // Get position
      PeDisposition disposition = arch.peDispositions.get(type);
      // Check if pointer is still within bounds
      if(typePointer >= disposition.peIds.size()) {
         return null;
      }

      return disposition.peIds.get(typePointer);
   }

   private Map<PeType, Integer> currentTypePointer;
   private final Architecture arch;
}
