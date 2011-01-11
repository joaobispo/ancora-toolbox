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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.StraighLineLoops.GraphBuilder;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.Graphs.GraphUtils;

/**
 * Extracts information from a list of Very Big Instructions.
 *
 * @author Joao Bispo
 */
public class VbiAnalyser {
/*
   public VbiAnalyser() {
      vbiList = new ArrayList<VeryBigInstruction32>();
   }

   public List<VeryBigInstruction32> getVbiList() {
      return vbiList;
   }
*/
   

   /**
    * Adds an instruction to the list of instructions of the analyser.
    * 
    * @param vbi
    */
   /*
   public void addVbi(VeryBigInstruction vbi) {
      vbi
   }
    *
    */

   /**
    *
    * @return the total number of instructions added on the analyser.
    */
   /*
   public int getTotalInstructions() {
      return 0;
   }
    *
    */

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

   /*
   public static int getNumberOfInstructions(List<VeryBigInstruction32> vbis, Collection<String> instructionNames) {
      int counter = 0;
      for(VeryBigInstruction32 vbi : vbis) {
         if(!instructionNames.contains(vbi.op)) {
            continue;
         }

         counter++;
      }
      return counter;
   }
    *
    */

   public static Map<String, Integer> getInstructionsHistogram(List<VeryBigInstruction32> vbis, Collection<String> instructionNames) {
      Map<String, Integer> counterTable = new HashMap<String, Integer>();
      for(VeryBigInstruction32 vbi : vbis) {
         if(!instructionNames.contains(vbi.op)) {
            continue;
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

      public static int getCriticalPathLenght(List<VeryBigInstruction32> vbis, GraphBuilder graphBuilder) {

         GraphNode rootNode = graphBuilder.buildGraph(vbis);
         //findCycle(rootNode);
         //visitLeafs(rootNode);
         //GraphUtils.printNode(rootNode, "");
      
         return GraphUtils.getCriticalPathLenght(rootNode);
      }

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
      
      /*
      // Check if current node is already in the set
      if(nodes.contains(node)) {
         
         System.exit(1);
      }

      // Add current node to the set
      nodes.add(node);
*/

   }

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


   //private List<VeryBigInstruction32> vbiList;
}
