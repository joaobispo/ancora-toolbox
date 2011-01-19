/*
 *  Copyright 2011 SuikaSoft.
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

package org.specs.LoopOptimization.V2;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.MicroBlaze.Vbi.MbSolver;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class Application implements App {

   public Application() {
      nodeWeights = null;
   }


   private boolean init(File setupFile) {
      // Get block files to process
      Setup setup = (Setup)IoUtils.readObject(setupFile);

      // Get serialized files
      //serializedBlocks = getSerializedFiles(setup);

      nodeWeights = getWeights(setup);

      outputFolder = BaseUtils.getFolder(setup.get(Options.OutputFolder));
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder.");
         return false;
      }

      /// New vars
      String speedupFilename = BaseUtils.getString(setup.get(Options.SpeedupFile));
      speedupFile = new File(outputFolder, speedupFilename);
      // Clean file
      IoUtils.write(speedupFile, "");

      // Get Simulator Setup
      Setup simulatorSetup = BaseUtils.getSetup(setup.get(Options.SimulationSetup));
      simulator = new MbSimulator(simulatorSetup);


      inputElfs = InputType.getFiles(setup, Options.InputPath,
              Options.PathType);

      MbSolver.reset();
      return true;
   }

   public int execute(File setupFile) throws InterruptedException {
      /**
       * INIT
       */
       if(!init(setupFile)) {
          return -1;
       }


      System.out.println("Processing "+inputElfs.size()+" files.");
      for(File file : inputElfs) {
         String message = "File '"+file.getName()+"':";
         System.out.println(message);
         IoUtils.append(speedupFile, message+"\n");
         processElf(file);
      }

      /*
      System.out.println("Processing "+serializedBlocks.size()+" files.");
      for(File file : serializedBlocks) {
         System.out.println("File '"+file.getName()+"':");
         processLoop(file);
      }
       *
       */


      // What instructions are not yet supported?
      System.out.println("Instructions not yet supported by MbSolver which could be optimized by CFP:");
      System.out.println(MbSolver.operationsNotSupported);

      return 0;
   }

   public Collection<EnumKey> getEnumKeys() {
      return BaseUtils.extractEnumValues(Options.class);
   }

   private void processElf(File file) {

      SimulatorResults simulatorResults = simulator.runElf(file);

      double cpi = calcCpi(simulatorResults);
      long finalBalance = simulatorResults.totalCycles;

//            System.out.println("Total Elf Cycles:"+simulatorResults.totalCycles);
//            System.out.println("Total Elf Instructions:"+simulatorResults.totalInstructions);
//            System.out.println("Elf CPI:"+cpi);

      // For each loop, calculate cycles needed to execute it, best theoretical case.
      for(CodeSegment loop : simulatorResults.foundLoops) {
         long loopGppCycles = calcCycles(loop.getTotalInstructions(), cpi);
         long loopHwCycles = calculateHwCycles(loop);

         finalBalance -= loopGppCycles;
         finalBalance += loopHwCycles;

//         System.out.println("Loop Gpp Cycles (-):"+loopGppCycles);
//         System.out.println("Loop HW Cycles (+):"+loopHwCycles);
      }

      double speedup = (double)simulatorResults.totalCycles / (double)finalBalance;

      String message = "SpeedUp:"+speedup;
      System.out.println(message);
      IoUtils.append(speedupFile, message+"\n");
      return;
   }

   public static double calcCpi(double totalCycles, double totalInstructions) {
      return totalCycles / totalInstructions;
   }

   public static double calcCpi(SimulatorResults simulatorResults) {
      return calcCpi((double)simulatorResults.totalCycles, (double)simulatorResults.totalInstructions);
   }

   private static long calcCycles(double totalInstructions, double cpi) {
      double totalCycles = totalInstructions * cpi;
      long totalCyclesLong = (long) Math.ceil(totalCycles);
      return totalCyclesLong;
   }

   private long calculateHwCycles(CodeSegment loop) {
      MbLoopAnalysis analysis = MbLoopAnalysis.analyse(loop, nodeWeights);

      return analysis.totalCyclesWithOptimizations;
      //return analysis.totalCyclesWithoutOptimizations;
   }

   /*
   private List<File> getSerializedFiles(Setup setup) {

      List<File> files = InputType.getFiles(setup, Options.InputPath,
              Options.PathType);

      // Filter files
      List<File> blocks = new ArrayList<File>();
      for (File file : files) {
         if (!file.getName().endsWith(LoopDiskWriter.SERIALIZED_BLOCK_EXTENSION)) {
            continue;
         }

         blocks.add(file);
      }

      return blocks;
   }
*/
   //private void processLoop(CodeSegment loop) {
  /*
   private void processLoop(File loopFile) {
      CodeSegment loop = (CodeSegment) IoUtils.readObject(loopFile);
      if (loop == null) {
         LoggingUtils.getLogger().
                 warning("Could not open loop for analysis");
         return;
      }


      // Build MicroBlaze instructions cache
      List<MbInstruction> mbInstructions = MicroBlazeParser.getMbInstructions(
              loop.getAddresses(), loop.getInstructions());

      // Gather complete pass analysis data
      //AssemblyAnalyser assAnal = MbAssemblyAnalyser.create(loop.getRegisterValues(), mbInstructions);
      AssemblyAnalysis asmData = MbAssemblyAnalyser.buildData(loop.getRegisterValues(), mbInstructions);

      //System.out.println(assAnal);

      // Expand instructions into very big instructions
      //MbVbiParser vbiParser = new MbVbiParser(assAnal.getLiveOuts(), assAnal.getConstantRegisters());
      MbVbiParser vbiParser = new MbVbiParser(asmData);
      List<VeryBigInstruction32> vbis = getVbis(mbInstructions, vbiParser);

      GraphBuilder graphBuilder = new MbGraphBuilder(nodeWeights);
      GraphNode rootNode = graphBuilder.buildGraph(vbis);
      String dottyFilename = loopFile.getName() + ".dotty";
      IoUtils.write(new File(outputFolder, dottyFilename), DottyGraph.generateDotty(rootNode));

      //VbiAnalysis vbiAnalysisOriginal = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisOriginal = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      //System.out.println(vbiAnalysisOriginal);


      optimizeVbis(vbis);
      graphBuilder = new MbGraphBuilder(nodeWeights);
      rootNode = graphBuilder.buildGraph(vbis);

      //VbiAnalysis vbiAnalysisTransformed = VbiAnalyser.buildData(vbis, MbInstructionName.add, rootNode);
      VbiAnalysis vbiAnalysisTransformed = VbiAnalysis.newAnalysis(vbis, MbInstructionName.add, rootNode);
      System.out.println(vbiAnalysisTransformed.diff(vbiAnalysisOriginal));

      dottyFilename = loopFile.getName() + ".after.dotty";
      IoUtils.write(new File(outputFolder, dottyFilename), DottyGraph.generateDotty(rootNode));
   }
*/



   private Map<String, Integer> getWeights(Setup setup) {

      String propertiesFilename = BaseUtils.getString(setup.get(Options.PropertiesFileWithInstructionCycles));
      if (propertiesFilename.isEmpty()) {
         return null;
      }

      File propertiesFile = new File(propertiesFilename);
      if (!propertiesFile.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not load properties file '" + propertiesFilename + "'.");
         return null;
      }

      Properties properties = IoUtils.loadProperties(propertiesFile);


      if (properties == null) {
         return null;
      }

      Map<String, Integer> weights = new HashMap<String, Integer>();
      for (String key : properties.stringPropertyNames()) {
         String stringValue = properties.getProperty(key);
         Integer value = ParseUtils.parseInteger(stringValue);
         if (value == null) {
            continue;
         }
         weights.put(key, value);
      }

      return weights;
   }
/*
   private void optimizeVbis(List<VeryBigInstruction32> vbis) {
      Solver solver = new MbSolver();
      VbiOptimizer constantPropagation = new ConstantFoldingAndPropagation(solver);
      //VbiOptimizer constantPropagation2 = new ConstantFoldingAndPropagation(solver);
      ConstantLoadsRemoval loadRemove = new ConstantLoadsRemoval();

      for(VeryBigInstruction32 vbi : vbis) {
         constantPropagation.optimize(vbi);
//         constantPropagation2.optimize(vbi);
         loadRemove.optimize(vbi);
      }

      loadRemove.close();
   }
*/
   /**
    * INSTANCE VARIABLES
    */
   //private List<File> serializedBlocks;
   private Map<String, Integer> nodeWeights;
   private File outputFolder;
   
   // New vars
   private MbSimulator simulator;
   private List<File> inputElfs;
   private File speedupFile;









}
