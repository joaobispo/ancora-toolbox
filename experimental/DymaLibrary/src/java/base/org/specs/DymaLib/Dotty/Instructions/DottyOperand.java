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

package org.specs.DymaLib.Dotty.Instructions;

/**
 * Data class used for DOT graphs in this package.
 *
 * @author Joao Bispo
 */
public class DottyOperand {

   public DottyOperand(String id, String value, boolean isInput, boolean isImmediate, boolean isConstant, boolean isLiveIn, boolean isLiveOut) {
      this.id = id;
      this.value = value;
      this.isInput = isInput;
      this.isImmediate = isImmediate;
      this.isConstant = isConstant;
      this.isLiveIn = isLiveIn;
      this.isLiveOut = isLiveOut;
   }
   

   public final String id;
   public final String value;
   public boolean isInput;
   public boolean isImmediate;
   public boolean isConstant;
   public boolean isLiveIn;
   public boolean isLiveOut;
}
