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

package org.specs.DymaLib.Vbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.Dotty.Instructions.DottyOpWithData;
import org.specs.DymaLib.Dotty.Instructions.DottyOperand;
import org.specs.DymaLib.Vbi.Parser.VbiParser;
import org.suikasoft.SharedLibrary.DataStructures.AccumulatorMap;
import org.suikasoft.SharedLibrary.LoggingUtils;

/**
 * Utility methods related to VBIs and its operands.
 *
 * @author Joao Bispo
 */
public class VbiUtils {

   public static List<VbiOperand> getInputs(List<VbiOperand> operands) {
      return getIO(operands, true);
   }

   public static List<VbiOperand> getOutputs(List<VbiOperand> operands) {
      return getIO(operands, false);
   }

   private static List<VbiOperand> getIO(List<VbiOperand> operands, boolean getInputs) {
       List<VbiOperand> ioOperands = new ArrayList<VbiOperand>();
      for(VbiOperand op : operands) {
         // Ignore same as flag
         if(op.isInput != getInputs) {
            continue;
         }

         ioOperands.add(op);
      }

      return ioOperands;
   }

   public static boolean areConstant(List<VbiOperand> operands) {
      for(VbiOperand op : operands) {
         if(!op.isConstant) {
            return false;
         }
      }

      return true;
   }

   /**
    *
    * @return the number of VBI which are marked as mappable.
    */
   public static int getMappableInstructions(List<VeryBigInstruction32> vbis) {
      int counter = 0;
      for(VeryBigInstruction32 vbi : vbis) {
         if(!vbi.isMappable) {
            continue;
         }

         counter++;
      }
      return counter;
   }

      /**
    * Builds an histogram with the quantity of instructions present in the list
    * of VBIs. The histogram is built taking into account only the instructions
    * given in the list.
    *
    * <p>If the list of instruction names is null, the histogram will include all
    * instructions.
    *
    * @param vbis
    * @param instructionNames
    * @return
    */
   public static AccumulatorMap getInstructionsHistogram(List<VeryBigInstruction32> vbis, Collection<String> instructionNames) {
      AccumulatorMap<String> counterTable = new AccumulatorMap<String>();
      for(VeryBigInstruction32 vbi : vbis) {
         boolean nameListExists = instructionNames != null;
         if (nameListExists) {
            if (!instructionNames.contains(vbi.op)) {
               continue;
            }
         }

         counterTable.add(vbi.op);
      }

      return counterTable;
   }

   /**
    * Transforms assembly instructions into VBIs, with the help of a VbiParser.
    *
    * @param assemblyInstructions
    * @param vbiParser
    * @return
    */
   public static List<VeryBigInstruction32> getVbis(List<?> assemblyInstructions,
           VbiParser vbiParser) {
      List<VeryBigInstruction32> vbis = new ArrayList<VeryBigInstruction32>();
      for (Object asmInstruction : assemblyInstructions) {
         VeryBigInstruction32 vbi = vbiParser.parseInstruction(asmInstruction);
         if (vbi == null) {
            LoggingUtils.getLogger().
                    warning("Returned null VBI. This may impact operations which"
                    + " use line number information from pre-analysis.");
            continue;
         }
         vbis.add(vbi);

      }

      return vbis;
   }

   public static DottyOperand toDottyOperand(VbiOperand operand) {
      String value = null;
      if(operand.value != null) {
         value = operand.value.toString();
      }

      return new DottyOperand(operand.id, value, operand.isInput, !operand.isRegister
              , operand.isConstant, operand.isLiveIn, operand.isLiveOut);
   }

   public static List<DottyOperand> extractDottyOperands(VeryBigInstruction32 vbi) {
      List<DottyOperand> ops = new ArrayList<DottyOperand>();

      for(VbiOperand vbiOp : vbi.originalOperands) {
         ops.add(toDottyOperand(vbiOp));
      }

      for(VbiOperand vbiOp : vbi.supportOperands) {
         ops.add(toDottyOperand(vbiOp));
      }

      return ops;
   }

   public static String generateDotFile(List<VeryBigInstruction32> vbis) {
      DottyOpWithData dottyGenerator = new DottyOpWithData();

      for(VeryBigInstruction32 vbi : vbis) {
         if(!vbi.isMappable) {
            continue;
         }
         
         //dottyGenerator.addInstruction(vbi.op, vbi.isMappable,
         dottyGenerator.addInstruction(vbi.op,
                 VbiUtils.extractDottyOperands(vbi));
      }
      dottyGenerator.close();

      return dottyGenerator.getDotty();
   }
}
