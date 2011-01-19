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

package org.specs.DymaLib.MicroBlaze.Assembly;

import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.Assembly.ConstantRegister;
import org.specs.DymaLib.Assembly.LiveOut;
import org.specs.DymaLib.Assembly.AssemblyAnalysis;
import org.specs.DymaLib.Utils.LivenessAnalyser;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Builds AssemblyAnalysis objects from MicroBlaze assembly instructions.
 * 
 * @author Joao Bispo
 */
public class MbAssemblyAnalyser {

 /*
   private static boolean hasStores(List<MbInstruction> mbInstructions) {
      boolean hasStores = false;

      for (MbInstruction inst : mbInstructions) {
         if (InstructionProperties.STORE_INSTRUCTIONS.contains(inst.getInstructionName())) {
            hasStores = true;
         }
      }

      return hasStores;
   }
*/
  
/*
   private static Collection<ConstantRegister> getConstantRegisters(RegisterTable registerIdTable, LivenessAnalyser livenessAnalyser) {
      // Check if CodeSegment has the values of the registers
      if (registerIdTable == null) {
         return null;
      }

     return livenessAnalyser.getConstantRegisters(registerIdTable);

   }
*/
   
   public static AssemblyAnalysis buildData(RegisterTable registerTable,
           List<MbInstruction> mbInstructions) {

        LivenessAnalyser livenessAnalyser = MbLivenessUtils.createLivenessAnalyser(mbInstructions);

      // Extract data for building MbAnalyser
       Collection<LiveOut> liveouts = livenessAnalyser.getLiveOuts();
       //Collection<ConstantRegister> constantRegisters = getConstantRegisters(registerTable, livenessAnalyser);
       Collection<ConstantRegister> constantRegisters = livenessAnalyser.getConstantRegisters(registerTable);
       boolean hasStores = MbUtils.hasStores(mbInstructions);

       AssemblyAnalysis asmData = new AssemblyAnalysis(liveouts, constantRegisters, hasStores,
               livenessAnalyser.getLiveIns());
       return asmData;
   }

}
