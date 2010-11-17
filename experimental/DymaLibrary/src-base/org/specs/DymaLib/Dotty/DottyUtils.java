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

package org.specs.DymaLib.Dotty;

import java.util.List;

/**
 * Utility methods related to Dot plots.
 *
 * @author Joao Bispo
 */
public class DottyUtils {

   public static String generateDotty(List<String> declarations, List<String> connections) {
      StringBuilder builder = new StringBuilder();

      builder.append("digraph graphname {\n");
      for(String declaration : declarations) {
         builder.append(declaration);
         builder.append(";\n");
      }
      builder.append("\n");

      for(String connection : connections) {
         builder.append(connection);
         builder.append(";\n");
      }
      builder.append("}");

      return builder.toString();
   }

   /**
    * Shape and Color can be null.
    *
    * @param id
    * @param label
    * @param shape
    * @param color
    * @return
    */
   public static String declaration(String id, String label, String shape,
           String color) {

      StringBuilder builder = new StringBuilder();
      builder.append(id);
      builder.append("[label=\"");
      builder.append(label);
      builder.append("\"");

      if (shape != null) {
         builder.append(", shape=");
         builder.append(shape);
      }

      if (color != null) {
         builder.append(", style=filled fillcolor=\"");
         builder.append(color);
         builder.append("\"");
      }

      builder.append("]");

      return builder.toString();
   }

   /**
    * Label can be null.
    * 
    * @param id
    * @param label
    * @param shape
    * @param color
    * @return
    */
   public static String connection(String inputId, String outputId, String label) {

      StringBuilder builder = new StringBuilder();
      builder.append(inputId);
      builder.append(" -> ");
      builder.append(outputId);

      boolean usedOptions = false;
      if (label != null) {
         usedOptions = true;
         builder.append(" [label=\"");
         builder.append(label);
         builder.append("\"");
      }
      
      if(usedOptions) {
         builder.append("]");
      }
      
      return builder.toString();
   }

   public static final String SHAPE_BOX = "box";
   public static final String COLOR_LIGHTBLUE = "lightblue";
   public static final String COLOR_GRAY75 = "gray75";

}
