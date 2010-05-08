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

package org.ancora.JavaReleaser.Gui;

/**
 * Container for the data in the frame.
 *
 * @author Joao Bispo
 */
public class FrameData {

   public FrameData(String releaseName, String distFoldername, String runFoldername, String outputFoldername, boolean buildJavadocZip) {
      this.releaseName = releaseName;
      this.distFoldername = distFoldername;
      this.runFoldername = runFoldername;
      this.outputFoldername = outputFoldername;
      this.buildJavadocZip = buildJavadocZip;
   }

   public String getDistFoldername() {
      return distFoldername;
   }

   public String getOutputFoldername() {
      return outputFoldername;
   }

   public String getReleaseName() {
      return releaseName;
   }

   public String getRunFoldername() {
      return runFoldername;
   }

   public boolean isBuildJavadocZip() {
      return buildJavadocZip;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   final private String releaseName;
   final private String distFoldername;
   final private String runFoldername;
   final private String outputFoldername;
   final private boolean buildJavadocZip;
}
