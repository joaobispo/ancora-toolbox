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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.IoUtils;
import org.specs.DymaLib.Dotty.DottyStraigthLineLoop;
import org.specs.DymaLib.PreAnalysis.CodeSegment;
import org.specs.DymaLib.deprecated.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.deprecated.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.deprecated.Stats.SllAnalyser;

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
    * @param baseFilename
    * @param setupName
    * @param iterationsThreshold
    * @param lowLevelParser
    */
//   public LoopDiskWriter(File outputFolder, String baseFilename,
//           String setupName, int iterationsThreshold, LowLevelParser lowLevelParser,
//           boolean loopsAreStraightLine, Enum[] instructionNames, boolean writeDottyForStraighLineLoops,
//           boolean writeTxtFilesForLoops) {
   public LoopDiskWriter(File outputFolder, String baseFilename,
           String setupName, LowLevelParser lowLevelParser, DiskWriterSetup setup,
           boolean loopsAreStraightLine, Enum[] instructionNames) {
      this.outputFolder = outputFolder;
      this.programFilename = baseFilename;
      this.setupName = setupName;
      this.iterationsThreshold = setup.iterationsThreshold;
      this.lowLevelParser = lowLevelParser;
      this.straightLineLoops = loopsAreStraightLine;
      this.instructionNames = instructionNames;
      this.writeDottyForStraighLineLoops = setup.writeDottyForStraighLineLoops;
      this.writeTxtForLoops = setup.writeTxtFilesForLoops;

      loopCount = 0;
      writtenLoops = new HashSet<Integer>();


   }

   public void addLoops(List<CodeSegment> loops) {
      if(loops == null) {
         return;
      }

      for(CodeSegment unit : loops) {
         addLoop(unit);
      }

   }

      public void addLoop(CodeSegment unit) {
               // Only write units of the type loop
         if(!unit.isLoop()) {
            return;
         }

         // Check if loop has enough iterations
         if(unit.getIterations() < iterationsThreshold) {
            return;
         }

         // Check if loop was already written
         if(writtenLoops.contains(unit.getId())) {
            return;
         }

         // Write current loop
         StringBuilder baseFilename = new StringBuilder();
         //baseFilename.append(ParseUtils.removeSuffix(programFilename, "."));
         baseFilename.append(programFilename);
         baseFilename.append(".");
         baseFilename.append(setupName);
         baseFilename.append(".");
         baseFilename.append(loopCount);

         //String txtFilename = baseFilename.toString() + ".txt";
         String blockFilename = baseFilename.toString() + BLOCK_EXTENSION;
         //String dottyFilename = baseFilename.toString() + ".dotty";
         String dottyFilename = baseFilename.toString() + DOT_EXTENSION;
         String serializedBlockFilename = baseFilename.toString() + SERIALIZED_BLOCK_EXTENSION;

         writeBlock(blockFilename, serializedBlockFilename, unit);
         writeDotty(dottyFilename, unit);
         //IoUtils.writeObject(new File(serializedBlockFilename), unit);

         // House cleaning
         writtenLoops.add(unit.getId());
         loopCount++;
   }

   private String buildBody(CodeSegment unit) {
      StringBuilder builder = new StringBuilder();
      
      //builder.append(unit.toString());


      builder.append("iterations:");
      builder.append(unit.getIterations());
      builder.append("\n");


      builder.append("sequence instructions: ");
      builder.append(unit.getNumInstructions());
      builder.append("\n");
      builder.append("instructions x iterations: ");
      builder.append(unit.getNumInstructions() * unit.getIterations());
      builder.append("\n");
      
      builder.append(BlockParser.BLOCK_BEGIN);
      builder.append("\n");
      for(int i=0; i<unit.getInstructions().size(); i++) {
         builder.append(unit.getAddresses().get(i));
         builder.append(BlockParser.ADDRESS_INSTRUCTION_SEPARATOR);
         builder.append(unit.getInstructions().get(i));
         builder.append("\n");
      }
      builder.append(BlockParser.BLOCK_END);
      builder.append("\n");


      // Get information from the Straigh-Line Loop
      if(straightLineLoops) {
         builder.append("------------\n");

          List<LowLevelInstruction> llInsts = 
                  lowLevelParser.parseInstructions(unit.getAddresses(), unit.getInstructions());
          builder.append(SllAnalyser.analyse(llInsts));

      }
      
      return builder.toString();
   }

   private void writeBlock(String txtFilename, String serializedFilename, CodeSegment unit) {
      if(!writeTxtForLoops) {
         return;
      }

      String txtBody = buildBody(unit);
      IoUtils.write(new File(outputFolder, txtFilename), txtBody);

      IoUtils.writeObject(new File(outputFolder, serializedFilename), unit);
   }

   private void writeDotty(String dottyFilename, CodeSegment unit) {
      if (!straightLineLoops) {
         return;
      }

      if(!writeDottyForStraighLineLoops) {
         return;
      }

      List<LowLevelInstruction> llInsts =
              lowLevelParser.parseInstructions(unit.getAddresses(), unit.getInstructions());

      String dottyBody = DottyStraigthLineLoop.generateDotty(llInsts, instructionNames);
      //String dottyBody = generateDotty(llInsts, instructionNames);

      IoUtils.write(new File(outputFolder, dottyFilename), dottyBody);
   }

   /*
   private String generateDotty(List<LowLevelInstruction> llInsts, Enum[] instructionNames) {
      llInsts.get(0).operands.get(0).
   }
   */
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

   public static final String DOT_EXTENSION = ".dotty";
   public static final String BLOCK_EXTENSION = ".block";
   public static final String SERIALIZED_BLOCK_EXTENSION = BLOCK_EXTENSION+".serialized";



}
