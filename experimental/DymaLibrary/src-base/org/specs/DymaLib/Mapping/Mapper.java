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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.Elements.Operand;
import org.specs.DymaLib.LowLevelInstruction.LliUtils;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Representation.Configuration;
import org.specs.DymaLib.Mapping.Representation.PeInput;
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
      constantLiveouts = new HashMap<String, Integer>();
   }



   /**
    *
    * @param lli
    * @return true if instruction could be mapped, false otherwise.
    */
   public boolean nextInstruction(LowLevelInstruction lli) {
      // If not mappable, don't bother with mapping
      if(lli.flags.mappable == LliUtils.DISABLED) {
         processUnmappable(lli);
         return true;
      }

      // Process inputs
      List<PeInput> peInputs = LliUtils.processInputs(lli);


      // Check dependencies


      // Get first available cycle counting from this cycle


      // Make space for possible new cycle
      retireIfFull();
      // Update
      //updateTables(lli, cycle, pePosition);
      
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

   private void updateTables(LowLevelInstruction lli, int cycle, int pePosition) {
      // Check live-ins

      // Process inputs
      for(Operand operand : lli.operands) {
         if(operand.flow != Operand.FLOW_INPUT) {
            continue;
         }

         if(operand.type != Operand.TYPE_REGISTER) {
            continue;
         }

         if(operand.isLiveIn != LliUtils.ENABLED) {
            continue;
         }

         // Add live-in
         livenessTable.liveIns.add(operand.value);

      }

      // Process outputs
      for (Operand operand : lli.operands) {
         if (operand.flow != Operand.FLOW_OUTPUT) {
            continue;
         }

         if (operand.type != Operand.TYPE_REGISTER) {
            continue;
         }

         // If immutable, means we have to store the value of this live-out.
         if(operand.isConstant == LliUtils.ENABLED) {
            String register = LliUtils.extractRegisterFromConstantRegister(operand.value);
            Integer value = LliUtils.extractImmediateFromConstantRegister(operand.value);
            constantLiveouts.put(register, value);
            continue;
         }

         // Add liveout to tables
         if(!livenessTable.liveOuts.contains(operand.value)) {
            livenessTable.liveOuts.add(operand.value);
//            livenessTable.liveOutsList.add(operand.value);
         }

         registerTable.updateTable(operand.value, cycle, pePosition);

      }
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
      if(workingWindow.isEmpty()) {
         return;
      }
      //while(!workingWindow.isEmpty()) {
      // Get to the last line
      while(workingWindow.getNewerIndex() - workingWindow.getOldestIndex() != 0) {
         //WorkingCycle cycle = workingWindow.popOldestElement();
         //MapperUtils.addMapping(configuration, cycle);
         retireInstructionFromWindow(true);
      }

      // There is only one line left
      retireInstructionFromWindow(false);
   }


   private void retireIfFull() {
      // If full, retire cycle
      if (workingWindow.isFull()) {
         retireInstructionFromWindow(true);
      }
   }

   private void retireInstructionFromWindow(boolean checkLastDefinition) {
         WorkingCycle cycle = workingWindow.popOldestElement();

      if (checkLastDefinition) {
         // Check if this cycle has the last definition for any of the current
         // liveout values
         int retiringCycle = cycle.cycleNumber;
         List<String> registersToReplicate = new ArrayList<String>();
         for (String register : registerTable.registerTable.keySet()) {
            int lastDefinitionCycle = registerTable.getCycle(register);
            
            if (lastDefinitionCycle > retiringCycle) {
               continue;
            }

            if (lastDefinitionCycle < retiringCycle) {
               LoggingUtils.getLogger().
                       warning("Check: retiring cycle '" + retiringCycle + "' greater "
                       + "than last definition cycle '" + lastDefinitionCycle + "'.");

            }
            
            registersToReplicate.add(register);
         }

         // For each register, add a MOVE in the next cycle

         /*
         for (MappedPe pe : cycle.mappedPes) {
            for (Operand operand : pe.outputRegisters) {
               Integer lastDefinitionCycle = registerTable.getCycle(operand.value);
               if (lastDefinitionCycle > retiringCycle) {
                  continue;
               }

               if (lastDefinitionCycle < retiringCycle) {
                  LoggingUtils.getLogger().
                          warning("Check: retiring cycle '" + retiringCycle + "' greater "
                          + "than last definition cycle '" + lastDefinitionCycle + "'.");

               }

               // They are equal. Add to the list of registers to replicate

            }

         }
         */
      }
         if(cycle == null) {
            LoggingUtils.getLogger().
                 warning("Window is already empty.");
            return;
         }

         MapperUtils.addMapping(configuration, cycle);
   }

      private void processUnmappable(LowLevelInstruction lli) {
      // Even if not mappable, can contribute to live-outs
      int cycle = -1; // Won't be used
      int pePosition = -1; // Won't be used
      updateTables(lli, cycle, pePosition);
   }

   /**
    * INSTANCE VARIABLES
    */
   // Working Data
   private LivenessTable livenessTable;
   private RegisterTable registerTable;
   private Map<String, Integer> constantLiveouts;

   // Definitions
   private Architecture arch;

   // Temporary Storage
   private WorkingWindow workingWindow;
   private int cycleNumber;
   private Configuration configuration;








}
