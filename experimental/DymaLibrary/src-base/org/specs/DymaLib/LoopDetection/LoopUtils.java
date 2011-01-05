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

package org.specs.DymaLib.LoopDetection;

import java.io.File;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.InstructionDecoder;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockOptions;
import org.specs.DymaLib.LoopDetection.MegaBlock.MegaBlockOptionsV4;
import org.specs.DymaLib.LoopDetection.BackwardBranchBlock.BackwardBranchOptions;
import org.specs.DymaLib.LoopDetection.BackwardBranchBlock.BackwardBranchOptionsV4;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public class LoopUtils {

   /**
    * Verify if two lists of Trace Units are equal.
    *
    * <p>Lightweigth verification: only ids are compared.
    *
    * @param instructions
    * @param verification
    * @return
    */
   public static boolean verifyLists(List<TraceUnit> instructions, List<TraceUnit> verification) {
      // Sizes
      if(instructions.size() != verification.size()) {
         LoggingUtils.getLogger().
                 warning("Lists have diferent sizes.");
         return false;
      }

      for(int i=0; i<instructions.size(); i++) {
         boolean idIsEqual = instructions.get(i).getIdentifier() == verification.get(i).getIdentifier();
         if(!idIsEqual) {
            LoggingUtils.getLogger().
                    warning("Diferent IDs.");
            return false;
         }
      }

      return true;
   }

   /**
    * Verify if two lists of Trace Units are equal.
    *
    * <p>Heavyweigth verification: compares all instruction addresses.
    *
    * @param instructions
    * @param verification
    * @return
    */
   public static boolean verifyListsExtensive(List<TraceUnit> instructions, List<TraceUnit> verification) {
      // Sizes
      if(instructions.size() != verification.size()) {
         LoggingUtils.getLogger().
                 warning("Lists have diferent sizes.");
         return false;
      }

      for(int i=0; i<instructions.size(); i++) {
         List<Integer> instAddresses = instructions.get(i).getAddresses();
         List<Integer> verifyAddresses = verification.get(i).getAddresses();

         for(int j=0; j<instAddresses.size(); j++) {
            if(instAddresses.get(j) != verifyAddresses.get(j)) {
               LoggingUtils.getLogger().
                       warning("Different instruction addresses");
               return false;
            }
         }

         
      }

      return true;
   }

   /**
    * Builds a LoopDetector from the name of the LoopDetector and a file pointing
    * to an AppFile with the definitions for that block.
    * @param name
    * @param appFile
    * @return
    */
   public static LoopDetector newLoopDetector(String name, File appFile, InstructionDecoder decoder) {
      if(LoopDetectors.MegaBlock.name().equals(name)) {
         return MegaBlockOptions.newMegaBlockDetector(appFile, decoder);
      }

      if(LoopDetectors.WarpBlock.name().equals(name)) {
         return BackwardBranchOptions.newWarpBlockDetector(appFile, decoder);
      }

      LoggingUtils.getLogger().
              warning("Case not defined:"+name);
      return null;
   }

   public static LoopDetector newLoopDetector(String detectorName, Setup appFile, InstructionDecoder decoder) {
      if(LoopDetectors.MegaBlock.name().equals(detectorName)) {
         return MegaBlockOptionsV4.newMegaBlockDetector(appFile, decoder);
      }

      if(LoopDetectors.WarpBlock.name().equals(detectorName)) {
         return BackwardBranchOptionsV4.newWarpBlockDetector(appFile, decoder);
      }

      LoggingUtils.getLogger().
              warning("Case not defined:"+detectorName);
      return null;
   }

   

}
