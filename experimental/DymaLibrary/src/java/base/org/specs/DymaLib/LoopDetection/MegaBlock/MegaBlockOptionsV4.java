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
import java.util.Collection;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.TraceUnit.TraceUnits;
import org.specs.DymaLib.TraceUnit.UnitBuilder;
import org.specs.DymaLib.TraceUnit.UnitBuilderFactory;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;
import org.suikasoft.Jani.EnumKeyOptions.MultipleChoiceEnum;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public enum MegaBlockOptionsV4 implements EnumKey, MultipleChoiceEnum {

   MaxPatternSize(OptionType.integer),
   TraceUnit(OptionType.multipleChoice),
   StoreNonLoopInstructions(OptionType.bool),
   OnlyCollectLoops(OptionType.bool);

   private MegaBlockOptionsV4(OptionType type) {
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }

   private OptionType type;

   public Collection<String>  getChoices() {
      if (this == TraceUnit) {
         return EnumUtils.buildListToString(TraceUnits.values());
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
   public static MegaBlockDetector newMegaBlockDetector(Setup optionFile, InstructionDecoder decoder) {
   //public static MegaBlockDetector newMegaBlockDetector(File appFile, InstructionDecoder decoder) {
      //Setup optionFile = (Setup)IoUtils.readObject(appFile);
      //AppOptionFile optionFile = AppOptionFile.parseFile(appFile, MegaBlockOptionsV4.class);

      String traceUnitName = BaseUtils.getString(optionFile.get(MegaBlockOptionsV4.TraceUnit));
      TraceUnits traceUnit = EnumUtils.valueOf(TraceUnits.class, traceUnitName);
      if(traceUnitName == null || traceUnit == null) {
         return null;
      }
      UnitBuilder unitBuilder = UnitBuilderFactory.newUnitBuilder(traceUnit, decoder);

      Integer maxPatternSize = BaseUtils.getInteger(optionFile.get(MegaBlockOptionsV4.MaxPatternSize));
      if(maxPatternSize == null) {
         return null;
      }

      boolean storeSequenceInstructions = BaseUtils.getBoolean(optionFile.get(MegaBlockOptionsV4.StoreNonLoopInstructions));

      boolean onlyCollectLoops = BaseUtils.getBoolean(optionFile.get(MegaBlockOptionsV4.OnlyCollectLoops));
      
      return new MegaBlockDetector(unitBuilder, maxPatternSize, storeSequenceInstructions, onlyCollectLoops);
   }

}
