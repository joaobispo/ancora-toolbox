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
import java.util.List;
import org.specs.DymaLib.DataStructures.VbiOperand;

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
}
