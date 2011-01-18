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

package org.specs.DymaLib.Vbi.Analysis;

import java.util.List;
import org.specs.DymaLib.Vbi.VbiUtils;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.suikasoft.SharedLibrary.DataStructures.AccumulatorMap;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.Graphs.GraphUtils;
import org.suikasoft.SharedLibrary.Processors.InstructionName;

/**
 * Contains the results of an analysis to a list of Vbis.
 *
 * @author Joao Bispo
 */
public class VbiAnalysis {

   public VbiAnalysis(int mappableInstructions, int criticalPathLenght, int loadInstructions, int storeInstructions) {
      this.mappableInstructions = mappableInstructions;
      this.criticalPathLenght = criticalPathLenght;
      this.loadInstructions = loadInstructions;
      this.storeInstructions = storeInstructions;
   }

   public static VbiAnalysis newAnalysis(List<VeryBigInstruction32> vbis,
           InstructionName instName, GraphNode rootNode) {

      AccumulatorMap<String> storeHistogram = VbiUtils.getInstructionsHistogram(vbis, instName.getStoreInstructions());
      int numStores = storeHistogram.getSum();

      AccumulatorMap<String> loadHistogram = VbiUtils.getInstructionsHistogram(vbis, instName.getLoadInstructions());
      int numLoads = loadHistogram.getSum();

      int mappableInstructions = VbiUtils.getMappableInstructions(vbis);
      int cpl = GraphUtils.getCriticalPathLenght(rootNode);

      VbiAnalysis vbiAnalysis = new VbiAnalysis(mappableInstructions, cpl, numLoads, numStores);

      return vbiAnalysis;
   }

   public String diff(VbiAnalysis previousAnalysis) {
      StringBuilder builder = new StringBuilder();

      int mappableDiff =  previousAnalysis.mappableInstructions - mappableInstructions;
      if(mappableDiff != 0) {
         double ratio = ((double)mappableDiff /
                 (double)previousAnalysis.mappableInstructions) * 100;
         builder.append("Mappable Instructions Diff:").
                 append(mappableDiff).append(" (").
                 append(ratio).append("%)\n");
      }

      int cplDiff =  previousAnalysis.criticalPathLenght - criticalPathLenght;
      if(cplDiff != 0) {
         double ratio = ((double)cplDiff /
                 (double)previousAnalysis.criticalPathLenght) * 100;
         builder.append("Critical Path Lenght Diff :").
                 append(cplDiff).append(" (").
                 append(ratio).append("%)\n");
      }

      return builder.toString();
   }


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("#Mappable Instructions  :");
      builder.append(mappableInstructions);
      builder.append("\n");

      builder.append("#Store Instructions     :");
      builder.append(storeInstructions);
      builder.append("\n");

      builder.append("#Load Instructions      :");
      builder.append(loadInstructions);
      builder.append("\n");

      builder.append("#Critical Path Lenght   :");
      builder.append(criticalPathLenght);
      builder.append("\n");
      
      return builder.toString();
   }



   public final int mappableInstructions;
   public final int criticalPathLenght;
   public final int loadInstructions;
   public final int storeInstructions;
}
