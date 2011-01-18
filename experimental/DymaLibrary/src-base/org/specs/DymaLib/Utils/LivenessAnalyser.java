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

package org.specs.DymaLib.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.Assembly.ConstantRegister;
import org.specs.DymaLib.Assembly.LiveOut;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 *
 * @author Joao Bispo
 */
public class LivenessAnalyser {

   public LivenessAnalyser() {
      writtenRegisters = new HashMap<String, Integer>();
      liveIns = new ArrayList<String>();
      counter = 0;
   }

  public void next(List<String> operandIds, List<Boolean> isInput,
          List<Boolean> isConstant) {

     // Check inputs
     for(int i=0; i<operandIds.size(); i++) {
        // Next if output
        if(!isInput.get(i)) {
           continue;
        }
        
        // Next if constant
        if(isConstant.get(i)) {
           continue;
        }
        
        // Check if input register is already in written
        boolean alreadyWritten = writtenRegisters.containsKey(operandIds.get(i));
        // Is a live-in?
        if(!alreadyWritten) {
           liveIns.add(operandIds.get(i));
        }  
     }
     
     // Check outputs
     for(int i=0; i<operandIds.size(); i++) {
        // Next if input
        if(isInput.get(i)) {
           continue;
        }
        
        // Next if constant
        if(isConstant.get(i)) {
           continue;
        }
        
        // Add output register to written set
        writtenRegisters.put(operandIds.get(i), counter);
     }

     // Update Counter
     counter++;
   }

  public Collection<LiveOut> getLiveOuts() {
     List<LiveOut> liveOuts = new ArrayList<LiveOut>();

     // Get keys and order them
     List<String> keys = new ArrayList<String>();
     keys.addAll(writtenRegisters.keySet());
     Collections.sort(keys);

     for(String key : keys) {
        Integer line = writtenRegisters.get(key);
        liveOuts.add(new LiveOut(key, line));
     }

     return liveOuts;
  }

  //public Collection<ConstantRegister> getConstantRegisters(Map<String, Integer> registerValues) {
  public Collection<ConstantRegister> getConstantRegisters(RegisterTable registerValues) {
     List<ConstantRegister> constantRegisters = new ArrayList<ConstantRegister>();

     // Add any live-in which was not written
     for(String liveIn : liveIns) {
        boolean wasWritten = writtenRegisters.containsKey(liveIn);
        // Was written?
        if(!wasWritten) {
           Integer value = registerValues.get(liveIn);
           if(value == null) {
              LoggingUtils.getLogger().
                      warning("Could not get value for constant live-in register '"+liveIn+"'");
              continue;
           }
           constantRegisters.add(new ConstantRegister(liveIn, value));
        }
     }

     return constantRegisters;
  }

   /**
    * Maps the ID of a registers to the number of the instruction where it was
    * written last, according to the order they were fed to the analyser.
    */
   private Map<String, Integer> writtenRegisters;
   private List<String> liveIns;
   private int counter;
}
