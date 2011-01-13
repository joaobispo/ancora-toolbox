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

package org.specs.DymaLib.VbiUtils;

import java.util.List;
import org.specs.DymaLib.DataStructures.VbiOperand;
import org.specs.DymaLib.Utils.VbiUtils;

/**
 *
 * @author Joao Bispo
 */
public class OperandIO {

   public OperandIO(List<VbiOperand> originalOperands, List<VbiOperand> supportOperands) {
      baseInputs = VbiUtils.getInputs(originalOperands);
      additionalInputs = VbiUtils.getInputs(supportOperands);
      baseOutputs = VbiUtils.getOutputs(originalOperands);
      additionalOutputs = VbiUtils.getOutputs(supportOperands);
   }

   
   
   public final List<VbiOperand> baseInputs;
   public final List<VbiOperand> additionalInputs;
   public final List<VbiOperand> baseOutputs;
   public final List<VbiOperand> additionalOutputs;
}
