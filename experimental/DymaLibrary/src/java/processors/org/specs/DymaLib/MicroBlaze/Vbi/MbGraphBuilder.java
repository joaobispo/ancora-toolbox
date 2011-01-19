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

package org.specs.DymaLib.MicroBlaze.Vbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.Vbi.VbiOperand;
import org.specs.DymaLib.Vbi.VeryBigInstruction32;
import org.specs.DymaLib.Vbi.Utils.GraphBuilder;
import org.specs.DymaLib.Weights.WeightTable;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;
import org.suikasoft.SharedLibrary.MicroBlaze.InstructionProperties;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;

/**
 *
 * @author Joao Bispo
 */
public class MbGraphBuilder implements GraphBuilder {

   /*
   public MbGraphBuilder() {
      this(null);
   }
    *
    */
public MbGraphBuilder(WeightTable nodeWeights) {
   this.registerWrites = new HashMap<String, GraphNode>();
      this.nodeWeights = nodeWeights;
      this.lastBranch = null;
      this.lastStore = null;
      this.rootNode = null;
}

   public MbGraphBuilder(Map<String, Integer> nodeWeights) {
      this(new WeightTable(nodeWeights, DEFAULT_INSTRUCTION_WEIGHT));
      //this.registerWrites = new HashMap<String, GraphNode>();
      //this.nodeWeights = nodeWeights;
      //this.lastBranch = null;
      //this.lastStore = null;
      //this.rootNode = null;
   }



   public GraphNode buildGraph(List<VeryBigInstruction32> vbis) {
      long counter = 0;

      // Build Root Node
      rootNode = new GraphNode(ROOT_NODE_ID, counter);
      counter++;

      for(VeryBigInstruction32 vbi : vbis) {
         // Only build nodes for mappable nodes
         if(!vbi.isMappable) {
            continue;
         }

         // Create new node
         //GraphNode newNode = new GraphNode(counter+":"+vbi.op);
         GraphNode newNode = new GraphNode(vbi.op, counter);
         List<GraphNode> parentNodes = new ArrayList<GraphNode>();
         List<Integer> parentWeights = new ArrayList<Integer>();
         List<String> parentProps = new ArrayList<String>();
         getParents(newNode, vbi, parentNodes, parentWeights, parentProps);

         // The there are no parents for this node, connect to the Root Node
         if (parentNodes.isEmpty()) {
            //rootNode.addChild(newNode, ROOT_NODE_WEIGHT);
            rootNode.addUniqueChild(newNode, ROOT_NODE_WEIGHT);
         } else {
            for(int i=0; i<parentNodes.size(); i++) {
               String props = Integer.toString(parentWeights.get(i));
               if(parentProps.get(i) != null) {
                  props = props + "(" + parentProps.get(i) + ")";
               }

               parentNodes.get(i).addUniqueChild(newNode, parentWeights.get(i), props);
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


   private void getParents(GraphNode newNode, VeryBigInstruction32 vbi, 
           List<GraphNode> parentNodes, List<Integer> parentWeights, List<String> parentProps) {
      // Get parents related to inputs
      //getInputParents(vbi, parentNodes, parentWeights);
      getInputParents(vbi, parentNodes, parentWeights, parentProps);

      // Update outputs
      updateOutputs(newNode, vbi);

      // Get additional parents due to special cases
      addSpecialCases(newNode, vbi, parentNodes, parentWeights, parentProps);

      //throw new UnsupportedOperationException("Not yet implemented");
   }

   //private void getInputParents(VeryBigInstruction32 vbi, List<GraphNode> parentNodes, List<Integer> parentWeights) {
   private void getInputParents(VeryBigInstruction32 vbi, List<GraphNode> parentNodes,
           List<Integer> parentWeights, List<String> parentProps) {
      
      List<String> regIds = getRegIds(vbi, true);

      // For each register, check which node has written it for the last time
      // If no node has written a register, no parent is added
      for(String regId : regIds) {
         GraphNode parent = registerWrites.get(regId);
         if(parent == null) {
            continue;
         }

         int weight = getWeigth(parent.getId());

         parentNodes.add(parent);
         //parentWeights.add(DEFAULT_INSTRUCTION_WEIGHT);
         parentWeights.add(weight);
         parentProps.add(null);
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
         //if (!vbiOp.isInput) {
         if (vbiOp.isInput != getInputs) {
            continue;
         }

         if(vbiOp.isConstant) {
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
         int weigth = getWeigth(node.getId());
         //node.addUniqueChild(endNode, DEFAULT_INSTRUCTION_WEIGHT, Integer.toString(DEFAULT_INSTRUCTION_WEIGHT));
         node.addUniqueChild(endNode, weigth, Integer.toString(weigth));
         return;
      }
      
      for(GraphNode child : node.getChildren()) {
         attachEndNode(child, endNode, visitedNodes);
      }

      // Find all leafs and attach the end node to them

      //GraphNode
      //System.err.println("Not implemented yet");
   }

   //private void addSpecialCases(GraphNode newNode, VeryBigInstruction32 vbi, List<GraphNode> parentNodes, List<Integer> parentWeights) {
   private void addSpecialCases(GraphNode newNode, VeryBigInstruction32 vbi, 
           List<GraphNode> parentNodes, List<Integer> parentWeights, List<String> parentProps) {
      // Convert to MbInstructionName
      MbInstructionName instructionName = MbInstructionName.valueOf(vbi.op);

      // Case Store
      if(specialCaseStore(instructionName, parentNodes, parentWeights, newNode,
              parentProps)) {
         return;
      }

      // Case Branch
      if(specialCaseBranch(instructionName, parentNodes, parentWeights, newNode,
              parentProps)) {
         return;
      }

      // Case Load
      if(specialCaseLoad(instructionName, parentNodes, parentWeights, parentProps)) {
         return;
      }
 
 
   }

   private boolean specialCaseStore(MbInstructionName mbInstructionName,
           List<GraphNode> parentNodes, List<Integer> parentWeights, GraphNode newNode,
           List<String> parentProps) {

      if (!InstructionProperties.STORE_INSTRUCTIONS.contains(mbInstructionName)) {
         return false;
      }

      // Check last store
      if (lastStore != null) {
         int weigth = getWeigth(lastStore.getId());

         parentNodes.add(lastStore);
         //parentWeights.add(DEFAULT_INSTRUCTION_WEIGHT);
         parentWeights.add(weigth);
         parentProps.add(PROP_STORE);
      }

      // Check last branch
      if (lastBranch != null) {
         int weigth = getWeigth(lastBranch.getId());

         parentNodes.add(lastBranch);
         //parentWeights.add(DEFAULT_INSTRUCTION_WEIGHT);
         parentWeights.add(weigth);
         parentProps.add(PROP_BRANCH);
      }

      // Update last store
      lastStore = newNode;
      return true;
   }


   private boolean specialCaseBranch(MbInstructionName mbInstructionName,
           List<GraphNode> parentNodes, List<Integer> parentWeights, GraphNode newNode,
           List<String> parentProps) {

      if (!InstructionProperties.JUMP_INSTRUCTIONS.contains(mbInstructionName)) {
         return false;
      }

      // Check last store
      if (lastStore != null) {
         parentNodes.add(lastStore);
         parentWeights.add(ZERO_INSTRUCTION_WEIGHT);
         parentProps.add(PROP_STORE);
      }

      // Check last branch
      if (lastBranch != null) {
         parentNodes.add(lastBranch);
         parentWeights.add(ZERO_INSTRUCTION_WEIGHT);
         parentProps.add(PROP_BRANCH);
      }

      // Update last branch
      lastBranch = newNode;
      return true;
   }

   private boolean specialCaseLoad(MbInstructionName mbInstructionName,
           List<GraphNode> parentNodes, List<Integer> parentWeights, List<String> parentProps) {

      if (!InstructionProperties.LOAD_INSTRUCTIONS.contains(mbInstructionName)) {
         return false;
      }

      // Check last store
      if (lastStore != null) {
         int weigth = getWeigth(lastStore.getId());

         parentNodes.add(lastStore);
         //parentWeights.add(DEFAULT_INSTRUCTION_WEIGHT);
         parentWeights.add(weigth);
         parentProps.add(PROP_STORE);
      }
      
      return true;
   }

   private int getWeigth(String id) {
      return nodeWeights.getWeigth(id);
      /*
      if(nodeWeights == null) {
         return DEFAULT_INSTRUCTION_WEIGHT;
      }

      Integer weight = nodeWeights.get(id);
      if(weight == null) {
         LoggingUtils.getLogger().
                 warning("Could not find weigth for id '"+id+"'.");
         return DEFAULT_INSTRUCTION_WEIGHT;
      }

      return weight;
       * 
       */
   }

   private Map<String, GraphNode> registerWrites;
   //private Map<String, Integer> nodeWeights;
   private WeightTable nodeWeights;
   private GraphNode lastBranch;
   private GraphNode lastStore;
   private GraphNode rootNode;

   public static final String ROOT_NODE_ID = "root";
   public static final String END_NODE_ID = "endNode";
   public static final Integer ROOT_NODE_WEIGHT = 0;
   public static final Integer DEFAULT_INSTRUCTION_WEIGHT = 1;
   public static final Integer ZERO_INSTRUCTION_WEIGHT = 0;
   
   public static final String PROP_STORE = "store";
   public static final String PROP_BRANCH = "branch";






   }












