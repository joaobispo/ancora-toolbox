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

package org.specs.DymaLib.Weights;

import java.util.Map;
import org.suikasoft.SharedLibrary.LoggingUtils;

/**
 * A table which maps string identifiers to weights.
 *
 * @author Joao Bispo
 */
public class WeightTable {

   public WeightTable(Map<String, Integer> nodeWeights, int defaultWeight) {
      this.nodeWeights = nodeWeights;
      this.defaultWeight = defaultWeight;
   }

   public int getWeigth(String id) {
      if(nodeWeights == null) {
         return defaultWeight;
      }

      Integer weight = nodeWeights.get(id);
      if(weight == null) {
         LoggingUtils.getLogger().
                 warning("Could not find weigth for id '"+id+"'.");
         return defaultWeight;
      }

      return weight;
   }

   private final Map<String, Integer> nodeWeights;
   private final int defaultWeight;

   public static final int DEFAULT_WEIGHT = 1;
}
