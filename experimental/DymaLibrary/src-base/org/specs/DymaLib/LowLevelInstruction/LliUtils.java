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

package org.specs.DymaLib.LowLevelInstruction;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.Elements.Operand;
import org.specs.DymaLib.Mapping.Representation.PeInput;

/**
 * Utility methods for Low-Level Instructions.
 *
 * @author Joao Bispo
 */
public class LliUtils {

   /**
    * Builds a new Operand which represents a carry.
    * 
    * @param flow
    * @return
    */
   /*
   public static Operand newCarry(int flow) {
      String value = CARRY_VALUE;
      int type = Operand.TYPE_REGISTER;
      int isImmutable = DISABLED;
      Integer isLiveIn = DISABLED;

      return new Operand(value, type, flow, isImmutable, isLiveIn);
   }
    *
    */

   /**
    * Transforms a register number into a register string identifier.
    *
    * @param value
    * @return
    */
   /*
   public static String intToRegister(int value) {
      return REGISTER_PREFIX + value;
   }
    *
    */

   /**
    * Transforms a register string identifier into a number.
    *
    * @param register
    * @return
    */
   /*
   public static Integer registerToInt(String register) {
      String integer = register.substring(REGISTER_PREFIX.length());

      Integer parsedInt = ParseUtils.parseInteger(integer);
      if(parsedInt == null) {
         LoggingUtils.getLogger().
                 warning("Could not parse '"+register+"' into an integer.");

      }
      
      return parsedInt;
   }
    * 
    */

   public static String appendImmediateToRegister(String constRegister, int immValue) {
      return constRegister+REGISTER_SEPARATOR+immValue;
   }

   // TODO: Update lli format so these methods are not needed
   public static Integer extractImmediateFromConstantRegister(String appendedRegister) {
      int separatorIndex = appendedRegister.indexOf(REGISTER_SEPARATOR);
      String integer = appendedRegister.substring(separatorIndex+1);
      Integer parsedInteger = ParseUtils.parseInteger(integer);
      if(parsedInteger == null) {
         LoggingUtils.getLogger().
                 warning("Could not parse integer from '"+integer+"'.");

      }

      return parsedInteger;
   }

   public static String extractRegisterFromConstantRegister(String appendedRegister) {
      int separatorIndex = appendedRegister.indexOf(REGISTER_SEPARATOR);
      String register = appendedRegister.substring(0, separatorIndex);


      return register;
   }


   public static List<PeInput> processInputs(LowLevelInstruction lli) {
      List<PeInput> peInputs = new ArrayList<PeInput>();
      for(Operand operand : lli.operands) {
         if(operand.flow != Operand.FLOW_INPUT) {
            continue;
         }


      }

      return peInputs;

   }


   public final static int ENABLED = 1;
   public final static int DISABLED = 0;

   //public final static String CARRY_VALUE = "carry";
   //public final static String REGISTER_PREFIX = "r";
   public final static String REGISTER_SEPARATOR = ":";

}
