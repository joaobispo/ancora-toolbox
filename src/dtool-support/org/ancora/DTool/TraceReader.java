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

package org.ancora.DTool;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.LineReader;
import org.ancora.SharedLibrary.ParseUtils;

/**
 * Reads instructions from a trace created by DTools. Also extracts some 
 * properties after reaching the end of the trace.
 *
 * @author Joao Bispo
 */
public class TraceReader {

   private TraceReader(LineReader lineReader) {
      this.lineReader = lineReader;
      numberInstructions = null;
      cycles = null;
   }


/**
 * Builds a TraceReader from the given file. If the object could not be created,
 * returns null.
 *
 * <p>Creating a TraceReader needs File operations which may result in failure
 * to create the object. Instead of a construction, is recommended a public
 * static method to create the object.
 *
 * @param traceFile a DTool trace file
 * @return a TraceReader is the object could be created, null otherwise
 */
   public static TraceReader createTraceReader(File traceFile) {
      LineReader lineReader = LineReader.createLineReader(traceFile);
      if(lineReader == null) {
         return null;
      }

      return new TraceReader(lineReader);
   }

   /**
    *
    * @return the next line in the trace that represents an instruction, or null
    * if the end of the stream has been reached.
    */
   public TraceLine nextLine() {
      // While there are lines and a trace instruction was not found, loop.
      String line = null;
      while (true) {
         // Read next line
         line = lineReader.nextLine();

         // Check if end of stream has arrived.
         if (line == null) {
            return null;
         }

         // Check if current line is a trace instruction
         if (line.startsWith(TraceDefinitions.TRACE_PREFIX)) {
            // Parse Instruction
            return parseTraceInstruction(line);
         }

         // Check if line is a trace propertie
         parseTraceProperty(line);
      }
   }

   public Long getCycles() {
      if(cycles == null) {
         showNoInitMessage("cycles");
      }
      return cycles;
   }

   public Long getNumberInstructions() {
      if(numberInstructions == null) {
         showNoInitMessage("numberOfInstructions");
      }
      return numberInstructions;
   }

      private void showNoInitMessage(String variable) {
      Logger.getLogger(TraceReader.class.getName()).
                 log(Level.INFO,"Variable '"+variable+"' not initialized. Check if reader " +
                 "has reached the end of the trace.");
   }

   private TraceLine parseTraceInstruction(String traceInstruction) {
      // Split the trace instruction in parts
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(traceInstruction);
      String addressString = traceInstruction.substring(0, whiteSpaceIndex);
      String instruction = traceInstruction.substring(whiteSpaceIndex).trim();

      /// Parse Instruction Address
      // Remove prefix
      addressString = addressString.substring(TraceDefinitions.TRACE_PREFIX.length());
      // Get Instruction Address
      int instructionAddress = Integer.valueOf(addressString, 16);

      return new TraceLine(instructionAddress, instruction);
   }

   private void parseTraceProperty(String line) {
     String[] propertyLine = line.split(TraceDefinitions.PROPERTIES_SEPARATOR);

      // Check if size equals two
      if (propertyLine.length != 2) {
         return;
      }

         // We found a possibly key-value pair! Check if it matches any of the keys.
     String propertyName = propertyLine[0].trim();
     TraceProperty property = EnumUtils.valueOf(TraceProperty.class, propertyName);

     if(property == null) {
        return;
     }

     String value = propertyLine[1].trim();
     updateProperty(property, value);

   }

   private void updateProperty(TraceProperty property, String value) {
      switch(property) {
         case cycles:
            cycles = parseLong(value);
            break;
         case instructions:
            numberInstructions = parseLong(value);
            break;
         default:
            Logger.getLogger(TraceReader.class.getName()).
                    log(Level.WARNING, "Case not defined:"+property);
      }
   }


   private Long parseLong(String value) {
      try {
         return Long.valueOf(value);
      } catch(NumberFormatException ex) {
         Logger.getLogger(TraceReader.class.getName()).
                 log(Level.INFO, "Could not parse value '"+value+"' to long.");
         return null;
      }
   }

   /**
    * INSTANCE VARIABLES
    */
   private LineReader lineReader;
   private Long numberInstructions;
   private Long cycles;


   /**
    * INNER ENUM
    */
   public enum TraceProperty {
      instructions,
      cycles;
   }







}
