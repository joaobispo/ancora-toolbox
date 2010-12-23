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

package org.specs.CoverageData;

import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;

/**
 *
 * @author Joao Bispo
 */
public enum ChartOptions implements AppOptionEnum {

   SaveChart(AppValueType.bool),
   DisplayChart(AppValueType.bool),
   ChartName(AppValueType.string),
   DrawingWidth(AppValueType.integer),
   DrawingHeight(AppValueType.integer),
   ScaleFactor(AppValueType.integer);
   
   private ChartOptions(AppValueType type) {
      this.type = type;
   }

   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }
   private final AppValueType type;

}
