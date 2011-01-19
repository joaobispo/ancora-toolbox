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

package org.specs.DymaLib.Assembly;

import java.io.Serializable;
import java.util.List;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Represents a segment of code, which can represent a loop.
 *
 * @author Joao Bispo
 */
public interface CodeSegment extends Serializable {

   List<String> getInstructions();

   List<Integer> getAddresses();

   int getId();

   int getNumInstructions();

   int getIterations();

   int getTotalInstructions();

   boolean isLoop();

   boolean areAllInstructionsStored();

   RegisterTable getRegisterValues();

   void setRegisterValues(RegisterTable registerValues);

}
