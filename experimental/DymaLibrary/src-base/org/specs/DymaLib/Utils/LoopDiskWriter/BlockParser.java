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

package org.specs.DymaLib.Utils.LoopDiskWriter;

import java.io.File;
import org.ancora.SharedLibrary.ParseUtils;
import org.ancora.SharedLibrary.Utilities.LineReader;

/**
 *
 * @author Joao Bispo
 */
public class BlockParser {

   private BlockParser(LineReader lineReader) {
      this.lineReader = lineReader;

      currentAddress = -1;
      currentInstruction = "";

      // Advance linereader until we find block begin
      String line = null;
      while((line = lineReader.nextLine()) != null) {
         if(line.equals(BLOCK_BEGIN)) {
            break;
         }
      }
   }



   public static BlockParser newBlockParser(File blockFile) {
      LineReader lineReader = LineReader.createLineReader(blockFile);
      if(lineReader == null) {
         return null;
      }

      return new BlockParser(lineReader);
   }

   /**
    *
    * @return the next instruction in the block, or null if the block reached
    * its end.
    */
   public String nextInstruction() {

      if(currentInstruction.equals(BLOCK_END)) {
         return null;
      }

      String line = lineReader.nextLine();
      if(line == null) {
         currentInstruction = BLOCK_END;
         return null;
      }

      if(line.equals(BLOCK_END)) {
         currentInstruction = BLOCK_END;
         return null;
      }

      // Parse line
      String[] values = line.split(ADDRESS_INSTRUCTION_SEPARATOR);
      currentAddress = ParseUtils.parseInt(values[0]);
      currentInstruction = values[1];

      return currentInstruction;
   }

   public int getCurrentAddress() {
      return currentAddress;
   }

   public String getCurrentInstruction() {
      return currentInstruction;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private LineReader lineReader;
   private String currentInstruction;
   private int currentAddress;

   public static final String BLOCK_BEGIN = "BEGIN_LOOP";
   public static final String BLOCK_END = "END_LOOP";
   public static final String ADDRESS_INSTRUCTION_SEPARATOR = ":";
}
