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

package org.specs.SharedLibrary.Processors;

/**
 * Identifies registers in the DTool simulator.
 *
 * @author Joao Bispo
 */
public interface RegisterId {

   /**
    * @return  the register number used in the DTool simulator corresponding to
    * this particular register.
    */
   int getRegisterNumber();

   /**
    *
    * @return the name of the register.
    */
   String getRegisterName();
}
