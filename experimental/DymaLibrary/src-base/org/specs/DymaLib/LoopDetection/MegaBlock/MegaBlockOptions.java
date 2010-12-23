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

package org.specs.DymaLib.LoopDetection.MegaBlock;

import java.io.File;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleChoice;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.EnumUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;

/**
 *
 * @author Joao Bispo
 */
public enum MegaBlockOptions implements AppOptionEnum, AppOptionMultipleChoice {

   MaxPatternSize(AppValueType.integer),
   TraceUnit(AppValueType.multipleChoice),
   StoreNonLoopInstructions(AppValueType.bool),
   OnlyCollectLoops(AppValueType.bool);

   private MegaBlockOptions(AppValueType type) {
      this.type = type;
   }

   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }

   private AppValueType type;

   public Enum[] getChoices() {
      if (this == TraceUnit) {
         return TraceUnits.values();
      }

      return null;
   }

   /**
    * Builds a new MegaBlock.
    *
    * @param appFile
    * @param decoder
    * @return
    */
   public static MegaBlockDetector newMegaBlockDetector(File appFile, InstructionDecoder decoder) {
      AppOptionFile optionFile = AppOptionFile.parseFile(appFile, MegaBlockOptions.class);

      String traceUnitName = AppUtils.getString(optionFile.getMap(), MegaBlockOptions.TraceUnit);
      TraceUnits traceUnit = EnumUtils.valueOf(TraceUnits.class, traceUnitName);
      if(traceUnitName == null || traceUnit == null) {
         return null;
      }
      UnitBuilder unitBuilder = UnitBuilderFactory.newUnitBuilder(traceUnit, decoder);

      Integer maxPatternSize = AppUtils.getInteger(optionFile.getMap(), MegaBlockOptions.MaxPatternSize);
      if(maxPatternSize == null) {
         return null;
      }

      boolean storeSequenceInstructions = AppUtils.getBool(optionFile.getMap(), MegaBlockOptions.StoreNonLoopInstructions);

      boolean onlyCollectLoops = AppUtils.getBool(optionFile.getMap(), MegaBlockOptions.OnlyCollectLoops);
      
      return new MegaBlockDetector(unitBuilder, maxPatternSize, storeSequenceInstructions, onlyCollectLoops);
   }
}
