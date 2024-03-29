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

package org.specs.DymaLib.deprecated.LowLevelInstruction.Elements;

/**
 *
 * @author Joao Bispo
 */
public class InstructionFlags {

   public int mappable;
   public int instructionType;

   
   public final static int ENABLED = 1;
   public final static int DISABLED = 0;
   
   public final static int TYPE_LOAD = 0;
   public final static int TYPE_STORE = 1;
   public final static int TYPE_EXIT = 2;
   public final static int TYPE_GENERAL = 3;
}
