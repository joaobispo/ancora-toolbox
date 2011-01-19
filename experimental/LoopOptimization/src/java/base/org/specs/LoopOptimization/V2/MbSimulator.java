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
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.LoopDetection.LoopDetector;
import org.specs.DymaLib.LoopDetection.LoopDetectors;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.MicroBlaze.MbImplementation;
import org.specs.DymaLib.TraceReader;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public class MbSimulator {

   public MbSimulator(Setup setup) {
      Setup systemJaniSetup = BaseUtils.getSetup(setup.get(MbSimulatorOptions.SystemSetup));
      systemSetup = SystemSetup.newSystemSetup(systemJaniSetup);

      loopDetectorSetup = BaseUtils.getSetup(setup.get(MbSimulatorOptions.MegaBlockSetup));

      instructionDecoder = (new MbImplementation()).getInstructionDecoder();
      //instructionDecoder = new FW_3SP_Decoder();

      iterationThreshold = BaseUtils.getInteger(setup.get(MbSimulatorOptions.IterationThreshold));
   }

   public SimulatorResults runElf(File elfFile) {
      // Build LoopDetector
      LoopDetector loopDetector = LoopUtils.newLoopDetector(loopDetectorName,
              loopDetectorSetup, instructionDecoder);

      // Build TraceReader
      TraceReader traceReader = DToolReader.newDToolReader(elfFile, systemSetup);

      SimpleSimulator sim = new SimpleSimulator();
      SimulatorResults results = sim.run(traceReader, loopDetector, iterationThreshold);

      return results;
   }


   /**
    * INSTANCE VARIABLES
    */
   private SystemSetup systemSetup;
   private Setup loopDetectorSetup;
   private InstructionDecoder instructionDecoder;
   private Integer iterationThreshold;

   public static final String loopDetectorName = LoopDetectors.MegaBlock.name();
}
