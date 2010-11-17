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

package org.specs.DymaLib.Utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.DymaLib.Dotty.DottyStraigthLineLoop;
import org.specs.DymaLib.LoopDetection.LoopUnit;
import org.specs.DymaLib.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.Stats.SllAnalyser;

/**
 * Writes the LoopUnits to text-format, adds some analysis.
 *
 * @author Joao Bispo
 */
public class LoopDiskWriter {

   /**
    * Creates a new LoopDiskWriter.
    *
    * <br>LowLevelParser can be null, if we are not interested in extracting
    * additional information from Straight-Line Loops.
    * @param outputFolder
    * @param programFilename
    * @param setupName
    * @param iterationsThreshold
    * @param lowLevelParser
    */
   public LoopDiskWriter(File outputFolder, String programFilename, 
           String setupName, int iterationsThreshold, LowLevelParser lowLevelParser,
           boolean straightLoops, Enum[] instructionNames, boolean writeDottyForStraighLineLoops,
           boolean writeTxtFilesForLoops) {
      this.outputFolder = outputFolder;
      this.programFilename = programFilename;
      this.setupName = setupName;
      this.iterationsThreshold = iterationsThreshold;
      this.lowLevelParser = lowLevelParser;
      this.straightLineLoops = straightLoops;
      this.instructionNames = instructionNames;
      this.writeDottyForStraighLineLoops = writeDottyForStraighLineLoops;
      this.writeTxtForLoops = writeTxtFilesForLoops;

      loopCount = 0;
      writtenLoops = new HashSet<Integer>();


   }

   public void addLoops(List<LoopUnit> loops) {
      if(loops == null) {
         return;
      }

      for(LoopUnit unit : loops) {
         // Only write units of the type loop
         if(!unit.isLoop()) {
            continue;
         }

         // Check if loop has enough iterations
         if(unit.getIterations() < iterationsThreshold) {
            continue;
         }

         // Check if loop was already written
         if(writtenLoops.contains(unit.getId())) {
            continue;
         }

         // Write current loop
         StringBuilder baseFilename = new StringBuilder();
         baseFilename.append(ParseUtils.removeSuffix(programFilename, "."));
         baseFilename.append(".");
         baseFilename.append(setupName);
         baseFilename.append(".");
         baseFilename.append(loopCount);
         
         String txtFilename = baseFilename.toString() + ".txt";
         String dottyFilename = baseFilename.toString() + ".dotty";


         writeTxt(txtFilename, unit);
         writeDotty(dottyFilename, unit);
         

         // House cleaning
         writtenLoops.add(unit.getId());
         loopCount++;
      }

   }

   private String buildBody(LoopUnit unit) {
      StringBuilder builder = new StringBuilder();
      
      builder.append(unit.toString());

      // Get information from the Straigh-Line Loop
      if(straightLineLoops) {
         builder.append("------------\n");

          List<LowLevelInstruction> llInsts = 
                  lowLevelParser.parseInstructions(unit.getAddresses(), unit.getInstructions());
          builder.append(SllAnalyser.analyse(llInsts));

      }
      
      return builder.toString();
   }

   private void writeTxt(String txtFilename, LoopUnit unit) {
      if(!writeTxtForLoops) {
         return;
      }

      String txtBody = buildBody(unit);
      IoUtils.write(new File(outputFolder, txtFilename), txtBody);
   }

   private void writeDotty(String dottyFilename, LoopUnit unit) {
      if (!straightLineLoops) {
         return;
      }

      if(!writeDottyForStraighLineLoops) {
         return;
      }

      List<LowLevelInstruction> llInsts =
              lowLevelParser.parseInstructions(unit.getAddresses(), unit.getInstructions());

      String dottyBody = DottyStraigthLineLoop.generateDotty(llInsts, instructionNames);

      IoUtils.write(new File(outputFolder, dottyFilename), dottyBody);
   }

   private File outputFolder;
   private String programFilename;
   private String setupName;
   private int iterationsThreshold;
   private int loopCount;
   private Set<Integer> writtenLoops;
   private boolean writeDottyForStraighLineLoops;
   private boolean writeTxtForLoops;


   private LowLevelParser lowLevelParser;
   private boolean straightLineLoops;
   private Enum[] instructionNames;






}
