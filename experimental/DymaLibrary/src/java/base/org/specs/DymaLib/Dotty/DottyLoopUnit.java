/*
 *  Copyright 2010 Ancora Research Group.
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.specs.DymaLib.Assembly.CodeSegment;
//import org.specs.DymaLib.LoopDetection.MegaBlockUnit;


/**
 * Prints a graphical representation of the execution trace, in LoopUnits.
 *
 * @author Joao Bispo
 */
public class DottyLoopUnit {

   public DottyLoopUnit() {
      blockDeclaration = new StringBuilder();
      previousId = null;

      declaredBlocks = new HashSet<Integer>();
      declaredConnections = new HashMap<String, Integer>();
   }


   public String generateDot() {
      StringBuilder builder = new StringBuilder();

      builder.append("digraph graphname {\n");
      builder.append(blockDeclaration.toString());
      builder.append("\n");

      builder.append(buildConnections());
      builder.append("}\n");

      return builder.toString();
   }

   //public void addUnit(MegaBlockUnit unit) {
   public void addUnit(CodeSegment unit) {
      // Add declaration
      int id = unit.getId();
      if(!declaredBlocks.contains(id)) {
         declaredBlocks.add(id);
         addDeclaration(unit);
      }

      // Add connection
      if (previousId != null) {
         String connection = previousId + " -> " + id;

         // Check if connection already in map
         if (!declaredConnections.containsKey(connection)) {
            declaredConnections.put(connection, 1);
         } else {
            // Increment value
            Integer newValue = declaredConnections.get(connection)+1;
            declaredConnections.put(connection, newValue);
         }
      }

      previousId = id;
   }

//   private void addDeclaration(MegaBlockUnit block) {
   private void addDeclaration(CodeSegment block) {
         blockDeclaration.append(block.getId());
         blockDeclaration.append("[label=\"");

         blockDeclaration.append(buildLabel(block));
         blockDeclaration.append("\"");
         blockDeclaration.append(", shape=box");

         //if(block.getType() == MegaBlockUnit.Type.Loop) {
         if(block.isLoop()) {
            blockDeclaration.append(", style=filled fillcolor=\"lightblue\"");
         }

         blockDeclaration.append("];\n");
   }


   private String buildConnections() {
      StringBuilder builder = new StringBuilder();

      for(String key : declaredConnections.keySet()) {
         builder.append(key);
         // Get value
         Integer value = declaredConnections.get(key);

            builder.append(" [label=");
            builder.append(value);
            builder.append("]");

         builder.append(";\n");
      }

      return builder.toString();
   }

//   private String buildLabel(MegaBlockUnit block) {
   private String buildLabel(CodeSegment block) {
      StringBuilder builder = new StringBuilder();


      builder.append("instructions:");
      builder.append(getFormattedNumInstructions(block));
      builder.append("\\n");

//      if (block.getType() == MegaBlockUnit.Type.Loop) {
      if (block.isLoop()) {
         builder.append("iterations:");
         builder.append(block.getIterations());
         builder.append("\\n");
      }


      builder.append(buildAddressString(block));

      return builder.toString();
   }

   private String getFormattedNumInstructions(CodeSegment block) {
      float floatAvg = (float) block.getTotalInstructions() / (float) block.getIterations();
      int intAvg = block.getNumInstructions();

      if ((float) intAvg == floatAvg) {
         return Integer.toString(intAvg);
      } else {
         return Float.toString(floatAvg);
      }
   }

   //private String buildAddressString(MegaBlockUnit block) {
   private String buildAddressString(CodeSegment block) {
      //if(block.getType() == MegaBlockUnit.Type.Sequence && !block.isSequenceInstructionsStored()) {
      if(!block.isLoop() && !block.areAllInstructionsStored()) {
         return incompleteSequenceAddressString(block);
      }

      StringBuilder builder = new StringBuilder();
      
      List<String> insts = block.getInstructions();
      List<Integer> addresses = block.getAddresses();
      int first = addresses.get(0);
      int previous = first;
      for(int i=1; i<insts.size(); i++) {
         int current = addresses.get(i);
         if(current - previous != 4) {
            builder.append(first);
            builder.append(" - ");
            builder.append(previous);
            builder.append("\\n");
            first = current;
         }
         previous = current;
      }

      builder.append(first);
      builder.append(" - ");
      builder.append(previous);
      builder.append("\\n");

      
      return builder.toString();
   }

   //private String incompleteSequenceAddressString(MegaBlockUnit block) {
   private String incompleteSequenceAddressString(CodeSegment block) {
      StringBuilder builder = new StringBuilder();
      List<Integer> addresses = block.getAddresses();

      builder.append(addresses.get(0));
      builder.append("\\n");
      if(addresses.size() == 1) {
         return builder.toString();
      }

      builder.append("...\\n");

      builder.append(addresses.get(addresses.size()-1));
      builder.append("\\n");

      return  builder.toString();
   }

   private StringBuilder blockDeclaration;
   private Integer previousId;
   private Set<Integer> declaredBlocks;
   private Map<String, Integer> declaredConnections;



}
