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

package org.specs.DymaLib.LoopDetection.WarpBlock;

import java.io.File;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.specs.DymaLib.Interfaces.InstructionDecoder;
import org.specs.DymaLib.LoopDetection.LoopDetector;

/**
 *
 * @author Joao Bispo
 */
public enum WarpBlockOptions implements AppOptionEnum {

   LimitBackwardJump(AppValueType.bool),
   BackwardJumpMaxSize(AppValueType.integer),
   StoreNonLoopInstructions(AppValueType.bool);


   private WarpBlockOptions(AppValueType type) {
      this.type = type;
   }

   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }

   private AppValueType type;


   /**
    * Builds a new WarpBlockDetector
    * @param appFile
    * @param decoder
    * @return
    */
   public static LoopDetector newWarpBlockDetector(File appFile, InstructionDecoder decoder) {
      AppOptionFile optionFile = AppOptionFile.parseFile(appFile, WarpBlockOptions.class);

      Integer backwardJumpMaxSize = AppUtils.getInteger(optionFile.getMap(), WarpBlockOptions.BackwardJumpMaxSize);
      if(backwardJumpMaxSize == null) {
         return null;
      }

      boolean limitBackwardJump = AppUtils.getBool(optionFile.getMap(), WarpBlockOptions.LimitBackwardJump);

      boolean storeSequenceInstructions = AppUtils.getBool(optionFile.getMap(), WarpBlockOptions.StoreNonLoopInstructions);

      return new WarpBlockDetector(limitBackwardJump, backwardJumpMaxSize, storeSequenceInstructions, decoder);
   }
}
