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

package org.specs.DymaLib.Assembly;

import java.io.Serializable;

/**
 * Represents a register which does not change its value during the considered
 * segment.
 *
 * <p>Includes an id to identify the register and the value
 * of the register during the considered segment.
 *
 * @author Joao Bispo
 */
public class ConstantRegister implements Serializable {

   public ConstantRegister(String id, int value) {
      this.id = id;
      this.value = value;
   }

   @Override
   public String toString() {
      return id+"("+value+")";
   }

   public final String id;
   public final int value;

   private static final long serialVersionUID = 1;
}
