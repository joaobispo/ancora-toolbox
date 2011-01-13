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

package org.specs.DymaLib.Dotty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.suikasoft.SharedLibrary.Graphs.GraphNode;

/**
 * Builds a dotty representation of a GraphNode
 *
 * @author Joao Bispo
 */
public class DottyGraph {

   public DottyGraph() {
      declarations = new ArrayList<String>();
      connections = new ArrayList<String>();

      nodeIds = new HashSet<String>();
   }

   public static String generateDotty(GraphNode node) {
      DottyGraph dotty = new DottyGraph();

      dotty.addNode(node);

      return DottyUtils.generateDotty(dotty.declarations, dotty.connections);

   }

   private void addNode(GraphNode node) {
      // Check if node was already added
      if(nodeIds.contains(node.toString())) {
         return;
      }

      // Add node to seen nodes
      nodeIds.add(node.toString());

      // Add node declarion
      declarations.add(DottyUtils.declaration(node.toString(), node.toString(), null, null));

      // Add connections
      for(int i=0; i<node.getChildren().size(); i++) {
      //for(GraphNode child : node.getChildren()) {
         GraphNode child = node.getChildren().get(i);
         String connectionLabel = node.getChildrenProperties().get(i);
         //String connection = DottyUtils.connection(node.toString(), child.toString(), null);
         String connection = DottyUtils.connection(node.toString(), child.toString(), connectionLabel);
         connections.add(connection);
      }

      // Visit children
      for(GraphNode child : node.getChildren()) {
         addNode(child);
      }

   }

   private List<String> declarations;
   private List<String> connections;

   private Set<String> nodeIds;

   //private static final String NODE_PREFIX = "node_";
   //private static final String CONNECTION_PREFIX = "connection_";


}
