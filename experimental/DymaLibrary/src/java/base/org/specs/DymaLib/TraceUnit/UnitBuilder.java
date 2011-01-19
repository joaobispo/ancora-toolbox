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

package org.specs.DymaLib.TraceUnit;

import java.util.List;

/**
 * Builds TraceUnit objects from the incoming trace.
 *
 * @author Joao Bispo
 */
public interface UnitBuilder {

   /**
    * Feeds an instruction to this UnitBuilder.
    * 
    * @param address
    * @param instruction
    */
   void nextInstruction(int address, String instruction);

   /**
    * Indicates that the stream of instructions has ended.
    */
   void close();

   /**
    *
    * @return a list of the TraceUnits found. After returning lists, they are
    * cleared from the UnitBuilder. Returns null if there are no TraceUnits at
    * the moment
    */
   List<TraceUnit> getAndClearUnits();
}
