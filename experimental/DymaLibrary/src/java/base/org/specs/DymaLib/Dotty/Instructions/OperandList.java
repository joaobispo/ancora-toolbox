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

package org.specs.DymaLib.Dotty.Instructions;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a couple of Operand Ids along their dotty ids.
 *
 * @author Joao Bispo
 */
public class OperandList {

   public OperandList() {
      operandId = new ArrayList<String>();
      dottyId = new ArrayList<String>();
   }

   public void add(String operandId, String dottyId) {
      this.dottyId.add(dottyId);
      this.operandId.add(operandId);
   }

   public List<String> getDottyId() {
      return dottyId;
   }

   public List<String> getOperandId() {
      return operandId;
   }

   public int size() {
      return operandId.size();
   }

   private List<String> operandId;
   private List<String> dottyId;
}
