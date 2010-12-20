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

package org.specs.AutoCompile.Job;

/**
 * Flags for mb-gcc compiler.
 *
 * @author Joao Bispo
 */
public enum MbGccFlag {
   Wall,
   g,
   mxlBarrelShifter,
   mnoXlSoftDiv,
   mnoXlSoftMul,
   xlModeExecutable;

   /**
    * String representation of the flag, to be used as an argument for the compiler.
    */
   @Override
   public String toString() {
      switch(this) {
         case mxlBarrelShifter:
                 return "-mxl-barrel-shift";
         case mnoXlSoftDiv:
                 return "-mno-xl-soft-div";
         case mnoXlSoftMul:
                 return "-mno-xl-soft-mul";
         case xlModeExecutable:
                 return "-xl-mode-executable";
         default:
            return "-"+this.name();
      }
   }


}
