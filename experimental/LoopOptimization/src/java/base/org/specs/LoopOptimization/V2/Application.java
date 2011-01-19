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
import java.util.List;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.MicroBlaze.Vbi.MbSolver;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.PreBuiltTypes.InputType;
import org.suikasoft.Jani.Setup;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;

/**
 * Calculates speedups of Elf files when we detect and move MegaBlocks at runtime.
 *
 * @author Joao Bispo
 */
public class Application implements App {

   private boolean init(File setupFile) {
      // Get block files to process
      Setup setup = (Setup)IoUtils.readObject(setupFile);

      mbLoopAnalyserSetup = BaseUtils.getSetup(setup.get(Options.AnalysisSetup));

      outputFolder = BaseUtils.getFolder(setup.get(Options.OutputFolder));
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder.");
         return false;
      }

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

      // For each loop, calculate cycles needed to execute it, best theoretical case.
      for(CodeSegment loop : simulatorResults.foundLoops) {
         long loopGppCycles = calcCycles(loop.getTotalInstructions(), cpi);
         long loopHwCycles = calculateHwCycles(loop);

         finalBalance -= loopGppCycles;
         finalBalance += loopHwCycles;

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
      MbLoopAnalyser mbLoopAnalyser = new MbLoopAnalyser(mbLoopAnalyserSetup);
      MbLoopAnalysis analysis = mbLoopAnalyser.analyse(loop);

      return analysis.totalCyclesWithOptimizations;
      //return analysis.totalCyclesWithoutOptimizations;
   }

   
   /**
    * INSTANCE VARIABLES
    */
   private Setup mbLoopAnalyserSetup;
   private File outputFolder;
   
   // New vars
   private MbSimulator simulator;
   private List<File> inputElfs;
   private File speedupFile;









}
