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

import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Representation.Configuration;
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
      configuration = new Configuration();
   }



   /**
    *
    * @param lli
    * @return true if instruction could be mapped, false otherwise.
    */
   public boolean nextInstruction(LowLevelInstruction lli) {


      //testAddInstructionWithMethod(lli);
      retireIfFull();
      updateTables(lli);
      
      return true;
   }

   private WorkingCycle getWorkingCycle(int aCycleNumber) {
      // If empty, create a new cycle
      if(workingWindow.isEmpty()) {
         newCycle();
      }

      // If is just one index after the newer, create cycle
      if(aCycleNumber == workingWindow.getNewerIndex()+1) {
         newCycle();
      }

      if(aCycleNumber > workingWindow.getNewerIndex() ||
              aCycleNumber < workingWindow.getOldestIndex()) {
         LoggingUtils.getLogger().
                 warning("Index '"+aCycleNumber+"' out of window ("+workingWindow.getOldestIndex()+
                 " to "+workingWindow.getNewerIndex()+")");
         return null;
      }

      WorkingCycle wCycle = workingWindow.getElement(aCycleNumber);


      // If wCycle == null, add cycles until it gets to the desired one.
      /*
      if(wCycle == null) {
         if(!workingWindow.isFull()) {
            newCycle();
         } else {
            LoggingUtils.getLogger().
                 warning("Window got full! CycleNumber:"+aCycleNumber);
            return null;
         }

         wCycle = workingWindow.getElement(aCycleNumber);
      }
*/
      return wCycle;

   }

   private void updateTables(LowLevelInstruction lli) {
      // Check live-ins
      throw new UnsupportedOperationException("Not yet implemented");
   }

   public void testAddInstructionDirectly(LowLevelInstruction lli) {
      // This part needs to be reworked - only put new cycle if needed.
      workingWindow.pushNewerElement(new WorkingCycle(arch, cycleNumber));
      System.out.println(workingWindow.getElement(cycleNumber));
      cycleNumber++;



      // If full, retire cycle
      if (workingWindow.isFull()) {
         WorkingCycle cycle = workingWindow.popOldestElement();
         // Add cycle to configuration
         MapperUtils.addMapping(configuration, cycle);
      }
   }

   public void testAddInstructionWithMethod(LowLevelInstruction lli) {
      // This part needs to be reworked - only put new cycle if needed.
      getWorkingCycle(cycleNumber);
      System.out.println(workingWindow.getElement(cycleNumber-1));
      /*
      workingWindow.pushNewerElement(new WorkingCycle(arch, cycleNumber));
      System.out.println(workingWindow.getElement(cycleNumber));
      cycleNumber++;
*/

      retireIfFull();
      // If full, retire cycle
//      if (workingWindow.isFull()) {
//         WorkingCycle cycle = workingWindow.popOldestElement();
         // Add cycle to configuration
//         MapperUtils.addMapping(configuration, cycle);
//         retireInstructionFromWindow();
//      }
   }


   private void newCycle() {
            workingWindow.pushNewerElement(new WorkingCycle(arch, cycleNumber));
            cycleNumber++;
   }

   public void close() {
      while(!workingWindow.isEmpty()) {
         //WorkingCycle cycle = workingWindow.popOldestElement();
         //MapperUtils.addMapping(configuration, cycle);
         retireInstructionFromWindow();
      }
   }


   private void retireIfFull() {
      // If full, retire cycle
      if (workingWindow.isFull()) {
         retireInstructionFromWindow();
      }
   }

   private void retireInstructionFromWindow() {
         WorkingCycle cycle = workingWindow.popOldestElement();

         if(cycle == null) {
            LoggingUtils.getLogger().
                 warning("Window is already empty.");
            return;
         }

         MapperUtils.addMapping(configuration, cycle);
   }

   /**
    * INSTANCE VARIABLES
    */
   // Working Data
   private LivenessTable livenessTable;
   private RegisterTable registerTable;

   // Definitions
   private Architecture arch;

   // Temporary Storage
   private WorkingWindow workingWindow;
   private int cycleNumber;
   private Configuration configuration;






}
