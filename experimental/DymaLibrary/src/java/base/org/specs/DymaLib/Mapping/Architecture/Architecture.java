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

package org.specs.DymaLib.Mapping.Architecture;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class Architecture {

   public Architecture(int numPes, Map<PeType, PeDisposition> peDispositions) {
      this.numPes = numPes;
      this.peDispositions = peDispositions;
   }


/**
 * The first PEs are 'memory', the rest are 'general'.
 *
 * @param numPes
 * @param memory
 * @return
 */
   public static Architecture xPesYMemory(int numPes, int memory) {

      if(numPes <= memory) {
         LoggingUtils.getLogger().
                 warning("Number of PEs ("+numPes+") needs to be greater than the number of memory ("+memory+").");
         return null;
      }

      // Memory PEs
      List<Integer> memoryPes = new ArrayList<Integer>();
      for(int i=0; i<memory; i++) {
         memoryPes.add(i);
      }

      // General PEs
      List<Integer> generalPes = new ArrayList<Integer>();
      for(int i=0; i<numPes-memory; i++) {
         generalPes.add(i+memory);
      }

      Map<PeType, PeDisposition> peDispositions = new EnumMap<PeType, PeDisposition>(PeType.class);
      peDispositions.put(PeType.Memory, new PeDisposition(memoryPes));
      peDispositions.put(PeType.General, new PeDisposition(generalPes));


      return new Architecture(numPes, peDispositions);
   }

   public final int numPes;
   public final Map<PeType, PeDisposition> peDispositions;

}
