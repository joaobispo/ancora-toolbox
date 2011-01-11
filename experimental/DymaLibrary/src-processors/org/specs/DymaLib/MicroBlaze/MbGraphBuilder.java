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

package org.specs.DymaLib.MicroBlaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.DataStructures.VbiOperand;
import org.specs.DymaLib.DataStructures.VeryBigInstruction32;
import org.specs.DymaLib.StraighLineLoops.GraphBuilder;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;

/**
 *
 * @author Joao Bispo
 */
public class MbGraphBuilder implements GraphBuilder {

   public MbGraphBuilder() {
      this.registerWrites = new HashMap<String, GraphNode>();
      this.lastBranch = null;
      this.lastStore = null;
      this.rootNode = null;
   }



   public GraphNode buildGraph(List<VeryBigInstruction32> vbis) {
      long counter = 0;

      // Build Root Node
      rootNode = new GraphNode(ROOT_NODE_ID, counter);
      counter++;

      for(VeryBigInstruction32 vbi : vbis) {
         // Create new node
         //GraphNode newNode = new GraphNode(counter+":"+vbi.op);
         GraphNode newNode = new GraphNode(vbi.op, counter);
         List<GraphNode> parentNodes = new ArrayList<GraphNode>();
         List<Integer> parentWeights = new ArrayList<Integer>();
         getParents(newNode, vbi, parentNodes, parentWeights);

         // The there are no parents for this node, connect to the Root Node
         if (parentNodes.isEmpty()) {
            rootNode.addChild(newNode, ROOT_NODE_WEIGHT);
         } else {
            for(int i=0; i<parentNodes.size(); i++) {
               parentNodes.get(i).addChild(newNode, parentWeights.get(i));
            }
         }
         
         counter++;
      }

      // Collect every node without child and attach an EndNode
      GraphNode endNode = new GraphNode(END_NODE_ID, counter);
      counter++;
      attachEndNode(rootNode, endNode, new HashSet<Long>());
      
      //System.out.println("Build graph with "+(counter-2)+ " instructions.");

      return rootNode;
   }


   private void getParents(GraphNode newNode, VeryBigInstruction32 vbi, List<GraphNode> parentNodes, List<Integer> parentWeights) {
      // Get parents related to inputs
      getInputParents(vbi, parentNodes, parentWeights);

      // Update outputs
      updateOutputs(newNode, vbi);

      // Get additional parents due to special cases

      //throw new UnsupportedOperationException("Not yet implemented");
   }

   private void getInputParents(VeryBigInstruction32 vbi, List<GraphNode> parentNodes, List<Integer> parentWeights) {
      
      List<String> regIds = getRegIds(vbi, true);

      // For each register, check which node has written it for the last time
      // If no node has written a register, no parent is added
      for(String regId : regIds) {
         GraphNode parent = registerWrites.get(regId);
         if(parent == null) {
            continue;
         }

         parentNodes.add(parent);
         parentWeights.add(DEFAULT_INSTRUCTION_WEIGHT);
      }

   }

   private void updateOutputs(GraphNode newNode, VeryBigInstruction32 vbi) {
      List<String> regIds = getRegIds(vbi, false);
      
      // For each output, update the table so that the current node is the 
      // future parent for other nodes which access these registers
      for(String regId : regIds) {
         registerWrites.put(regId, newNode);
      }
   }

   private List<String> getRegIds(VeryBigInstruction32 vbi, boolean getInputs) {

      List<String> regIds = new ArrayList<String>();
      for (VbiOperand vbiOp : vbi.originalOperands) {
         if (vbiOp.isInput != getInputs) {
            continue;
         }
         
         if(vbiOp.isConstant) {
            continue;
         }

         regIds.add(vbiOp.id);
      }

      for (VbiOperand vbiOp : vbi.supportOperands) {
         if (!vbiOp.isInput) {
            continue;
         }

         regIds.add(vbiOp.id);
      }

      return regIds;
   }

   private void attachEndNode(GraphNode node, GraphNode endNode, Set<Long> visitedNodes) {
      if(visitedNodes.contains(node.getNumber())) {
         return;
      }
      
      visitedNodes.add(node.getNumber());
      
      int numChild = node.getChildren().size();
      if(numChild == 0) {
         node.addChild(endNode, DEFAULT_INSTRUCTION_WEIGHT);
         return;
      }
      
      for(GraphNode child : node.getChildren()) {
         attachEndNode(child, endNode, visitedNodes);
      }

      // Find all leafs and attach the end node to them

      //GraphNode
      //System.err.println("Not implemented yet");
   }

   private Map<String, GraphNode> registerWrites;
   private GraphNode lastBranch;
   private GraphNode lastStore;
   private GraphNode rootNode;

   public static final String ROOT_NODE_ID = "root";
   public static final String END_NODE_ID = "endNode";
   public static final Integer ROOT_NODE_WEIGHT = 0;
   public static final Integer DEFAULT_INSTRUCTION_WEIGHT = 1;









}
