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

package org.specs.SharedLibrary.MicroBlaze.Utilities;

import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Extracts information from instructions generated for this processor.
 *
 * @author Joao Bispo
 */
public class InstructionParsing {
   /**
    *
    * @param instruction
    * @return the name of the instruction
    */
   public static String getInstructionName(String instruction) {
      int whiteSpaceIndex = instruction.indexOf(WHITE_SPACE);
      if(whiteSpaceIndex == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find name separator '"+WHITE_SPACE+"' in MicroBlaze Instruction: '"+instruction+"'.");
         return null;
      }
      String instNameString = instruction.substring(0, whiteSpaceIndex);
      
      return instNameString;
   }

   public static final String WHITE_SPACE = " ";
}
