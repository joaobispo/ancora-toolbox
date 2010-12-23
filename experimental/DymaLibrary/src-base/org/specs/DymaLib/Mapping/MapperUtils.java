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

package org.specs.DymaLib.Mapping;

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.Mapping.Representation.Configuration;
import org.specs.DymaLib.Mapping.Representation.Cycle;
import org.specs.DymaLib.Mapping.Representation.Pe;
import org.specs.DymaLib.Mapping.Tables.MappedPe;
import org.specs.DymaLib.Mapping.Tables.WorkingCycle;

/**
 * Utility methods related to mapping.
 * 
 * @author Joao Bispo
 */
public class MapperUtils {

   public static void addMapping(Configuration configuration, WorkingCycle cycle) {
      List<Pe> pes = new ArrayList<Pe>();
      for(MappedPe mappedPe : cycle.mappedPes) {
         pes.add(mappedPe.pe);
      }

      // Create cycle
      Cycle newCycle = new Cycle(cycle.cycleNumber, pes, cycle.exits);
      configuration.cycles.add(newCycle);
   }

}
