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

package org.specs.DymaLib.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.specs.DymaLib.DataStructures.VbiAnalysis;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.GraphBuilder;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.Graphs.GraphUtils;
import org.suikasoft.SharedLibrary.MiscUtils;
import org.suikasoft.SharedLibrary.Processors.InstructionName;

/**
 * Extracts information from a list of Very Big Instructions.
 *
 * @author Joao Bispo
 */
public class VbiAnalyser {

   /**
    *
    * @return the number of VBI which are marked as mappable.
    */
   //public int getMappableInstructions() {
   public static int getMappableInstructions(List<VeryBigInstruction32> vbis) {
      int counter = 0;
      for(VeryBigInstruction32 vbi : vbis) {
         if(!vbi.isMappable) {
            continue;
         }

         counter++;
      }
      return counter;
   }

   /**
    * Builds an histogram with the quantity of instructions present in the list
    * of VBIs. The histogram is built taking into account only the instructions
    * given in the list.
    *
    * <p>If the list of instruction names is null, the histogram will include all
    * instructions.
    *
    * @param vbis
    * @param instructionNames
    * @return
    */
   public static Map<String, Integer> getInstructionsHistogram(List<VeryBigInstruction32> vbis, Collection<String> instructionNames) {
      Map<String, Integer> counterTable = new HashMap<String, Integer>();
      for(VeryBigInstruction32 vbi : vbis) {
         boolean nameListExists = instructionNames != null;
         if (nameListExists) {
            if (!instructionNames.contains(vbi.op)) {
               continue;
            }
         }

         Integer value = counterTable.get(vbi.op);
         if(value == null) {
            value = 0;
         }

         value = value + 1;

         counterTable.put(vbi.op, value);
      }
      
      return counterTable;
   }

   /**
    * Builds a graph from the list of VBIs and calculates the
    * Critical Path Lenght of the resulting GraphNode.
    *
    * @param vbis
    * @param graphBuilder
    * @return
    */
   public static int getCriticalPathLenght(List<VeryBigInstruction32> vbis, GraphBuilder graphBuilder) {

      GraphNode rootNode = graphBuilder.buildGraph(vbis);
      //findCycle(rootNode);
      //visitLeafs(rootNode);
      //GraphUtils.printNode(rootNode, "");

      return GraphUtils.getCriticalPathLenght(rootNode);
   }

   /*
   private static void findCycle(GraphNode node) {
      // Check if number of node e less than any of its children
      for(GraphNode child : node.getChildren()) {
         if(child.getNumber() <= node.getNumber()) {
            System.err.println("Parent node '"+node+"' less or equal than child '"+child+"'");
         }
      }
  
      // Call childs
      for(GraphNode child : node.getChildren()) {
         findCycle(child);
      }

   }
*/
   /*
   private static void visitLeafs(GraphNode node) {
      int numChildren = node.getChildren().size();
      if(numChildren == 0) {
         System.out.println("Node '"+node+"' is leaf");
         return;
      }

      System.out.println("Node '"+node+"' has "+node.getChildren().size()+" children.");
      // Call childs
      for(GraphNode child : node.getChildren()) {
         visitLeafs(child);
      }
   }
*/

   //private List<VeryBigInstruction32> vbiList;

   public static VbiAnalysis getData(List<VeryBigInstruction32> vbis, 
           InstructionName instName, GraphBuilder graphBuilder) {


      Map<String,Integer> storeHistogram = VbiAnalyser.getInstructionsHistogram(vbis, instName.getStoreInstructions());
      int numStores = MiscUtils.sumValues(storeHistogram);

      Map<String,Integer> loadHistogram = VbiAnalyser.getInstructionsHistogram(vbis, instName.getLoadInstructions());
      int numLoads = MiscUtils.sumValues(loadHistogram);

      int mappableInstructions = VbiAnalyser.getMappableInstructions(vbis);
      int cpl = VbiAnalyser.getCriticalPathLenght(vbis, graphBuilder);

      VbiAnalysis vbiAnalysis = new VbiAnalysis(mappableInstructions, cpl, numLoads, numStores);

      return vbiAnalysis;
   }


}
