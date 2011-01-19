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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.Assembly.AssemblyAnalysis;
import org.specs.DymaLib.Assembly.ConstantRegister;
import org.specs.DymaLib.Assembly.LiveOut;
import org.specs.DymaLib.Liveness.LivenessAnalyser;
import org.specs.DymaLib.Liveness.LivenessAnalysis;
import org.suikasoft.SharedLibrary.MicroBlaze.CarryProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbRegisterId;
import org.suikasoft.SharedLibrary.MicroBlaze.MbUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbInstruction;
import org.suikasoft.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Utility methods related to analysis of MicroBlaze assembly instructions.
 *
 * @author Joao Bispo
 */
public class MbAssemblyUtils {

   /**
    * Parses a MbInstruction into the Lists needed for the LivenessAnalysis object.
    *
    * @param mbInst
    * @param operandIds
    * @param isInput
    * @param isConstant
    */
    public static void livenessParser(MbInstruction mbInst, List<String> operandIds, List<Boolean> isInput, List<Boolean> isConstant) {
      // Extract info from MbInst operands
      for(MbOperand mbOperand : mbInst.getOperands()) {
         livenessParser(mbOperand, operandIds, isInput, isConstant);
      }

      // Extract info from carries
      // Carry In
      if(CarryProperties.usesCarryIn(mbInst.getInstructionName())) {
         operandIds.add(MbRegisterId.getCarryFlagName());
         isInput.add(Boolean.TRUE);
         isConstant.add(Boolean.FALSE);
      }

      // Carry Out
      if(CarryProperties.usesCarryOut(mbInst.getInstructionName())) {
         operandIds.add(MbRegisterId.getCarryFlagName());
         isInput.add(Boolean.FALSE);
         isConstant.add(Boolean.FALSE);
      }

   }


    /**
     * Parses a MbOperand into the Lists needed for the LivenessAnalysis object.
     *
     * @param mbOperand
     * @param operandIds
     * @param input
     * @param constant
     */
   public static void livenessParser(MbOperand mbOperand, List<String> operandIds, List<Boolean> input, List<Boolean> constant) {
      String operandId = mbOperand.getId();
      Boolean isInput = MbOperand.isInput(mbOperand.getFlow());
      Boolean isConstant = MbOperand.isConstant(mbOperand.getType());

      operandIds.add(operandId);
      input.add(isInput);
      constant.add(isConstant);
   }


   /**
    * Builds LivenessAnalysis data from a list of MbInstructions and a table
    * with the values of the registers.
    *
    * @param mbInstructions
    * @param registerTable
    * @return
    */
   //public static AssemblyAnalyser createAnalyser(List<MbInstruction> mbInstructions) {
   //public static LivenessAnalyser buildLivenessAnalysis(List<MbInstruction> mbInstructions) {
   public static LivenessAnalysis buildLivenessAnalysis(List<MbInstruction> mbInstructions,
           RegisterTable registerTable) {
      // Use LivenessAnalyser
      //AssemblyAnalyser livenessAnalyser = new AssemblyAnalyser();
      LivenessAnalyser livenessAnalyser = new LivenessAnalyser();
      // Extract information from each mbInstruction and feed the information
      // to the analyser
      for(MbInstruction mbInst : mbInstructions) {
         List<String> operandIds = new ArrayList<String>();
         List<Boolean> isInput = new ArrayList<Boolean>();
         List<Boolean> isConstant = new ArrayList<Boolean>();
         livenessParser(mbInst, operandIds, isInput, isConstant);
         livenessAnalyser.next(operandIds, isInput, isConstant);
      }

      LivenessAnalysis liveAnalysis = new LivenessAnalysis(
              livenessAnalyser.getConstantRegisters(registerTable),
              livenessAnalyser.getLiveOuts(),
              livenessAnalyser.getLiveIns());

      //return livenessAnalyser;
      return liveAnalysis;
   }

   /**
    * Builds AssemblyAnalysis objects from MicroBlaze assembly instructions.
    *
    * @param registerTable
    * @param mbInstructions
    * @return
    */
   public static AssemblyAnalysis buildAssemblyAnalysis(RegisterTable registerTable,
           List<MbInstruction> mbInstructions) {

        //LivenessAnalyser livenessAnalyser = MbAssemblyUtils.buildLivenessAnalysis(mbInstructions);
        LivenessAnalysis livenessAnalyser = MbAssemblyUtils.buildLivenessAnalysis(mbInstructions, registerTable);

        return buildAssemblyAnalysis(livenessAnalyser, mbInstructions);
/*
        // Extract data for building MbAnalyser
       Collection<LiveOut> liveouts = livenessAnalyser.getLiveOuts();
       //Collection<ConstantRegister> constantRegisters = getConstantRegisters(registerTable, livenessAnalyser);
       Collection<ConstantRegister> constantRegisters = livenessAnalyser.getConstantRegisters(registerTable);
       boolean hasStores = MbUtils.hasStores(mbInstructions);

       AssemblyAnalysis asmData = new AssemblyAnalysis(liveouts, constantRegisters, hasStores,
               livenessAnalyser.getLiveIns());
       return asmData;
 * 
 */
   }

   /**
    * Builds AssemblyAnalysis objects from MicroBlaze assembly instructions.
    * 
    * @param liveAnalysis
    * @param mbInstructions
    * @return
    */
   public static AssemblyAnalysis buildAssemblyAnalysis(LivenessAnalysis liveAnalysis,
           List<MbInstruction> mbInstructions) {


       boolean hasStores = MbUtils.hasStores(mbInstructions);

//       AssemblyAnalysis asmData = new AssemblyAnalysis(liveouts, constantRegisters, hasStores,
//               livenessAnalyser.getLiveIns());
       AssemblyAnalysis asmData = new AssemblyAnalysis(liveAnalysis, hasStores);
       return asmData;
   }

}
