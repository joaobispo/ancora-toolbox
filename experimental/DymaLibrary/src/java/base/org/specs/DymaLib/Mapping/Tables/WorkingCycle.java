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

import java.util.ArrayList;
import java.util.List;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Representation.Exit;

/**
 *
 * @author Joao Bispo
 */
public class WorkingCycle {

   public WorkingCycle(Architecture arch, int cycleNumber) {
      this.cycleAval = new CycleAvaliability(arch);
      mappedPes = new ArrayList<MappedPe>();
      this.cycleNumber = cycleNumber;
   }

   @Override
   public String toString() {
      return "Cycle: "+cycleNumber;
   }




   public CycleAvaliability cycleAval;
   public List<MappedPe> mappedPes;
   public List<Exit> exits;
   public final int cycleNumber;
}
