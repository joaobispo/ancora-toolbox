/*
 *  Copyright 2011 SPeCS Research Group.
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
package org.specs.DymaLib.Liveness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.PreAnalysis.ConstantRegister;
import org.specs.DymaLib.PreAnalysis.LiveOut;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Analyses the liveness of operands from the sequence of instructions of a
 * MegaBlock.
 * 
 * <p>For each instruction the analyser requires three lists:
 * <br>operandIds, with Strings which identify each instruction operand;
 * <br>isInput, with booleans which identify if an operand is an input. If false
 * , the operand is assumed to be an output;
 * <br>isConstant, with booleans which identify if an operand does not change 
 * during the considered sequence (ex.: immediate values);
 *
 *
 * @author Joao Bispo
 */
public class LivenessAnalyser {

   public LivenessAnalyser() {
      writtenRegisters = new HashMap<String, Integer>();
      liveIns = new ArrayList<String>();
      counter = 0;
   }

   /**
    * Feeds to the analyser the operands of an instruction, along some of the
    * properties of those operands.
    *
    * @param operandIds
    * @param isInput
    * @param isConstant
    */
   public void next(List<String> operandIds, List<Boolean> isInput,
           List<Boolean> isConstant) {

      // Check inputs
      for (int i = 0; i < operandIds.size(); i++) {
         // Next if output
         if (!isInput.get(i)) {
            continue;
         }

         // Next if constant
         if (isConstant.get(i)) {
            continue;
         }

         // Check if input register is already in written
         boolean alreadyWritten = writtenRegisters.containsKey(operandIds.get(i));
         // Is a live-in?
         if (!alreadyWritten) {
            liveIns.add(operandIds.get(i));
         }
      }

      // Check outputs
      for (int i = 0; i < operandIds.size(); i++) {
         // Next if input
         if (isInput.get(i)) {
            continue;
         }

         // Next if constant
         if (isConstant.get(i)) {
            continue;
         }

         // Add output register to written set
         writtenRegisters.put(operandIds.get(i), counter);
      }

      // Update Counter
      counter++;
   }

   /**
    * Returns which operands are considered live-outs. A live-out is defined as
    * being an operand which is both an output and mutable.
    *
    * @return
    */
   public Collection<LiveOut> getLiveOuts() {
      List<LiveOut> liveOuts = new ArrayList<LiveOut>();

      // Get keys and order them
      List<String> keys = new ArrayList<String>();
      keys.addAll(writtenRegisters.keySet());
      Collections.sort(keys);

      for (String key : keys) {
         Integer line = writtenRegisters.get(key);
         liveOuts.add(new LiveOut(key, line));
      }

      return liveOuts;
   }

   /**
    * Returns the Constant Register found by the analyser. The Constant Register
    * objects need to have the value of the register, if a RegisterTable with the
    * register values is not provided, this method returns null.
    *
    * @param registerValues
    * @return a collection with the constant registers found by the analyser
    */
   //public Collection<ConstantRegister> getConstantRegisters(Map<String, Integer> registerValues) {
   public Collection<ConstantRegister> getConstantRegisters(RegisterTable registerValues) {
      //boolean hasRegisterTable = registerValues != null;
      if (registerValues == null) {
      //if (!hasRegisterTable) {
         LoggingUtils.getLogger().
                 warning("RegisterTable is null. Constant Registers will have "
                 + "its value set to null.");
         //return null;
      }
      List<ConstantRegister> constantRegisters = new ArrayList<ConstantRegister>();

      // Add any live-in which was not written
      for (String liveIn : liveIns) {
         boolean wasWritten = writtenRegisters.containsKey(liveIn);
         // Was written?
         if (!wasWritten) {
            Integer value = getValue(registerValues, liveIn);
            constantRegisters.add(new ConstantRegister(liveIn, value));
         }
      }

      return constantRegisters;
   }

   private Integer getValue(RegisterTable registerValues, String liveIn) {
      Integer value = null;
      if (registerValues != null) {
         //Integer value = registerValues.get(liveIn);
         value = registerValues.get(liveIn);
         if (value == null) {
            LoggingUtils.getLogger().
                    warning("Could not get value for constant live-in register '" + liveIn + "'");
            //continue;
         }
      }
      return value;
   }

   /**
    *
    * @return the identifiers of the found live-ins
    */
   public Collection<String> getLiveIns() {
      return liveIns;
   }
   /**
    * Maps the ID of a registers to the number of the instruction where it was
    * written last, according to the order they were fed to the analyser.
    */
   private Map<String, Integer> writtenRegisters;
   private List<String> liveIns;
   private int counter;

}
