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

import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DymaLib.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;

/**
 * Implementations related to MicroBlaze processor.
 *
 * @author Joao Bispo
 */
public class MbImplementation implements ProcessorImplementation {

   public InstructionDecoder getInstructionDecoder() {
      return new FW_3SP_Decoder();
   }

   public LowLevelParser getLowLevelParser() {
      return new MbLowLevelParser();
   }

   public Enum[] getInstructionNames() {
      return InstructionName.values();
   }

}
