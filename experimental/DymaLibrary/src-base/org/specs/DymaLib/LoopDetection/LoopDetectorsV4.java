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

package org.specs.DymaLib.LoopDetection;

import java.util.Collection;
import java.util.List;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockOptionsV4;
import org.specs.DymaLib.LoopDetection.BackwardBranchBlock.BackwardBranchOptionsV4;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.EnumKeyOptions.SingleSetupEnum;


/**
 * List of supported partitioners in this package.
 *
 * @author Joao Bispo
 */
public enum LoopDetectorsV4 implements SingleSetupEnum {
   MegaBlock(BaseUtils.extractEnumValues(MegaBlockOptionsV4.class)),
   BackwardBranchBlock(BaseUtils.extractEnumValues(BackwardBranchOptionsV4.class));

   private LoopDetectorsV4(List<EnumKey> options) {
      this.options = options;
   }


   //public List<EnumKey> getSetupOptions() {
   public Collection<EnumKey> getSetupOptions() {
      return options;
   }

   private final List<EnumKey> options;


}
