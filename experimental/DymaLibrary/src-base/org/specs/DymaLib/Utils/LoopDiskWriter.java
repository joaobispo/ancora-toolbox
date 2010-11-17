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

   /*
   public LoopDiskWriter(File outputFolder, String programFilename,
           String setupName, int iterationsThreshold) {
      this(outputFolder, programFilename, setupName, iterationsThreshold, null);
   }
    * 
    */

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
           boolean straightLoops) {
      this.outputFolder = outputFolder;
      this.programFilename = programFilename;
      this.setupName = setupName;
      this.iterationsThreshold = iterationsThreshold;
      this.lowLevelParser = lowLevelParser;
      this.straightLineLoops = straightLoops;

      loopCount = 0;
      writtenLoops = new HashSet<Integer>();


   }

   /*
   public void addLoops(List<LoopUnit> loops) {
      addLoops(loops, false);
   }
    * 
    */

   //public void addLoops(List<LoopUnit> loops, boolean isStraightLineLoop) {
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
         StringBuilder builder = new StringBuilder();
         builder.append(ParseUtils.removeSuffix(programFilename, "."));
         builder.append(".");
         builder.append(setupName);
         builder.append(".");
         builder.append(loopCount);
         builder.append(".txt");

         String body = buildBody(unit);
         IoUtils.write(new File(outputFolder, builder.toString()), body);
         //IoUtils.write(new File(outputFolder, builder.toString()), unit.toString());

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

   private File outputFolder;
   private String programFilename;
   private String setupName;
   private int iterationsThreshold;
   private int loopCount;
   private Set<Integer> writtenLoops;

   private LowLevelParser lowLevelParser;
   private boolean straightLineLoops;


}
