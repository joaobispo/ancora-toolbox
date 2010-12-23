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

package org.specs.DymaLib.MicroBlaze;


import org.ancora.SharedLibrary.EnumUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;
import org.specs.SharedLibrary.MicroBlaze.InstructionProperties;
import org.specs.SharedLibrary.MicroBlaze.Utilities.InstructionParsing;

/**
 * Extracts information from MicroBlaze instructions written in the DToolPlus
 * format (FireWorks 3 Stage-Pipelined).
 *
 * @author Joao Bispo
 */
public class FW_3SP_Decoder implements InstructionDecoder {

   public boolean isJump(String instruction) {

      InstructionName instName = getInstructionName(instruction);
       
      return InstructionProperties.JUMP_INSTRUCTIONS.contains(instName);
   }

   public int delaySlot(String instruction) {
      InstructionName instName = getInstructionName(instruction);
      boolean hasDelaySlot = InstructionProperties.INSTRUCTIONS_WITH_DELAY_SLOT.contains(instName);
      if(hasDelaySlot) {
         return 1;
      } else {
         return 0;
      }
   }

   private InstructionName getInstructionName(String instruction) {
             
      String instructionName = InstructionParsing.getInstructionName(instruction);
          // System.out.println("test1:"+instructionName);
          // System.out.println(TraceUnits.values());
          // System.out.println(InstructionName.values());
           
          InstructionName name = EnumUtils.valueOf(InstructionName.class, instructionName);
           //InstructionName name =   InstructionName.getEnum(instructionName);
//System.out.println("test3:"+name);
      return name;
   }

}
