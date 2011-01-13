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
import org.ancora.SharedLibrary.IoUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public enum WarpBlockOptionsV4 implements EnumKey {

   //LimitBackwardJump(OptionType.bool),
   BackwardJumpMaxSize(OptionType.integer),
   StoreNonLoopInstructions(OptionType.bool);


   private WarpBlockOptionsV4(OptionType type) {
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }

   private OptionType type;


   /**
    * Builds a new WarpBlockDetector
    * @param appFile
    * @param decoder
    * @return
    */
   public static LoopDetector newBackwardBranchBlockDetector(Setup optionFile, InstructionDecoder decoder) {
   //public static LoopDetector newWarpBlockDetector(File appFile, InstructionDecoder decoder) {
//      Setup optionFile = (Setup)IoUtils.readObject(appFile);
      Integer backwardJumpMaxSize = BaseUtils.getInteger(optionFile.get(WarpBlockOptionsV4.BackwardJumpMaxSize));
      if(backwardJumpMaxSize == null) {
         return null;
      }

      //boolean limitBackwardJump = BaseUtils.getBool(optionFile.getMap(), WarpBlockOptions.LimitBackwardJump);

      boolean storeSequenceInstructions = BaseUtils.getBoolean(optionFile.get(WarpBlockOptionsV4.StoreNonLoopInstructions));

      //return new WarpBlockDetector(limitBackwardJump, backwardJumpMaxSize, storeSequenceInstructions, decoder);
      return new BackwardBranchDetector(backwardJumpMaxSize, storeSequenceInstructions, decoder);
   }
}
