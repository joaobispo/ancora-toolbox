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
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;

/**
 * Extracts information from a list of Very Big Instructions.
 *
 * @author Joao Bispo
 */
public class VbiAnalyser {

   public VbiAnalyser() {
      vbiList = new ArrayList<VeryBigInstruction32>();
   }

   public List<VeryBigInstruction32> getVbiList() {
      return vbiList;
   }

   

   /**
    * Adds an instruction to the list of instructions of the analyser.
    * 
    * @param vbi
    */
   /*
   public void addVbi(VeryBigInstruction vbi) {
      vbi
   }
    *
    */

   /**
    *
    * @return the total number of instructions added on the analyser.
    */
   public int getTotalInstructions() {
      return 0;
   }

   /**
    *
    * @return the number of VBI which are marked as mappable.
    */
   public int getMappableInstructions() {
      return 0;
   }

   private List<VeryBigInstruction32> vbiList;
}
