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

import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Tables.LivenessTable;
import org.specs.DymaLib.Mapping.Tables.RegisterTable;
import org.specs.DymaLib.Mapping.Tables.WorkingCycle;

/**
 * Maps LowLevelInstructions into a coarse-grained.
 *
 * @author Joao Bispo
 */
public class Mapper {

   public Mapper(Architecture arch, int windowSize) {
      this.arch = arch;
      livenessTable = new LivenessTable();
      registerTable = new RegisterTable();
      workingWindow = new WorkingWindow(windowSize);
      cycleNumber = 0;
   }



   /**
    *
    * @param lli
    * @return true if instruction could be mapped, false otherwise.
    */
   public boolean nextInstruction(LowLevelInstruction lli) {
      //System.out.println(workingWindow);
      workingWindow.pushNewerElement(new WorkingCycle(arch, cycleNumber));
      cycleNumber++;

      if(workingWindow.isFull()) {
         workingWindow.popOldestElement();
         //System.out.println(workingWindow.popOldestElement());
      }
      
      return true;
   }

   public void close() {
      while(!workingWindow.isEmpty()) {
         System.out.println(workingWindow);
         workingWindow.popOldestElement();
         //System.out.println(workingWindow.popOldestElement());
      }
   }

   /**
    * INSTANCE VARIABLES
    */
   private LivenessTable livenessTable;
   private RegisterTable registerTable;
   private Architecture arch;
   private WorkingWindow workingWindow;
   private int cycleNumber;
}
