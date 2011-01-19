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

package org.specs.DymaLib.Vbi.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.Assembly.ConstantRegister;
import org.specs.DymaLib.Assembly.LiveOut;

/**
 * Utility methods related to parsing and creation of VBI instructions.
 *
 * @author Joao Bispo
 */
public class VbiParserUtils {

   public static Map<String, Integer> buildConstRegMap(List<ConstantRegister> constantRegisters) {
      Map<String, Integer> newMap = new HashMap<String, Integer>();
      int indexCounter = 0;

      for (ConstantRegister reg : constantRegisters) {
         newMap.put(reg.id, indexCounter);
         indexCounter++;
      }

      return newMap;
   }

   public static Map<String, Integer> buildLiveoutsMap(List<LiveOut> liveOuts) {
      Map<String, Integer> newMap = new HashMap<String, Integer>();
      int indexCounter = 0;
      for (LiveOut liveout : liveOuts) {
         newMap.put(liveout.id, indexCounter);
         indexCounter++;
      }

      return newMap;
   }
}
