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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Ignores loops that have appeard before, and has options which can filter
 * certain kinds of Loop Units.
 *
 * @author Joao Bispo
 */
public class LoopFilter {

   public LoopFilter(boolean onlyStoreLoops, int iterationsThreshold) {
      loops = new ArrayList<CodeSegment>();
      ids = new HashSet<Integer>();

      this.onlyStoreLoops = onlyStoreLoops;
      this.iterationsThreshold = iterationsThreshold;
   }

   public List<CodeSegment> getLoops() {
      return loops;
   }

   public void addLoops(List<CodeSegment> loops) {
      for(CodeSegment loop : loops) {
         addLoop(loop);
      }
   }

   public void addLoop(CodeSegment loop) {
      if (loop == null) {
         LoggingUtils.getLogger().
                 warning("LoopUnit is null;");
         return;
      }

      if (onlyStoreLoops) {
         // Only write units of the type loop
         if (!loop.isLoop()) {
            return;
         }
      }


      // Check if loop has enough iterations
      if (loop.getIterations() < iterationsThreshold) {
         return;
      }

      // Check if loop was already written
      if (ids.contains(loop.getId())) {
         return;
      }



      ids.add(loop.getId());
      loops.add(loop);
   }

   private List<CodeSegment> loops;
   private Set<Integer> ids;

   private boolean onlyStoreLoops;
   private int iterationsThreshold;
}
