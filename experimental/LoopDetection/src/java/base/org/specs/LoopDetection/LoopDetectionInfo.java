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

package org.specs.LoopDetection;

import java.io.File;
import org.specs.DymaLib.ProcessorImplementation;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public class LoopDetectionInfo {

   public LoopDetectionInfo(File elfFile, File outputFolder, String detectorName,
           File detectorSetup, ProcessorImplementation processor, String loopDetectorId) {
      this.elfFile = elfFile;
      this.outputFolder = outputFolder;
      this.detectorRunName = detectorName;
      this.detectorSetup = detectorSetup;
      this.detectorSetupV4 = null;
      this.processor = processor;
      this.loopDetectorId = loopDetectorId;
   }

   public LoopDetectionInfo(File elfFile, File outputFolder, String detectorRunName,
           Setup detectorSetup, ProcessorImplementation processor, String loopDetectorId) {
      this.elfFile = elfFile;
      this.outputFolder = outputFolder;
      this.detectorRunName = detectorRunName;
      this.detectorSetup = null;
      this.detectorSetupV4 = detectorSetup;
      this.processor = processor;
      this.loopDetectorId = loopDetectorId;
   }

   public String getDetectorName() {
      return BaseUtils.decodeMapOfSetupsKey(detectorRunName);
   }

   public final File elfFile;
   public final File outputFolder;
   public final String detectorRunName;
   public final File detectorSetup;
   public final Setup detectorSetupV4;
   public final ProcessorImplementation processor;
   public final String loopDetectorId;
}
