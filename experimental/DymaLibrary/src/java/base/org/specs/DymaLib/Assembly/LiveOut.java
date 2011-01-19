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
 * Represents a live-out.
 *
 * <p>Includes an id to identify the live-out and the number
 * of the instruction where it is written for the last time.
 *
 * @author Joao Bispo
 */
public class LiveOut implements Serializable {

   public LiveOut(String id, int instructionNumber) {
      this.id = id;
      this.instructionNumber = instructionNumber;
   }

   @Override
   public String toString() {
      return id+"("+instructionNumber+")";
   }

   public final String id;
   public final int instructionNumber;

   private static final long serialVersionUID = 1;
}
