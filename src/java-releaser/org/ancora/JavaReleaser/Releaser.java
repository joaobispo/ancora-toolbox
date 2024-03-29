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

package org.ancora.JavaReleaser;

import java.awt.EventQueue;
import org.ancora.JavaReleaser.Gui.ReleaserFrame;

/**
 *
 * @author Joao Bispo
 */
public class Releaser {

   /**
    * Creates a new instance of the Java Releaser.
    */
   public Releaser() {
      mainFrame = new ReleaserFrame();
   }



   /**
    * Runs the Java Releaser GUI
    */
   public void execute() {
      // Setup Frame
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            mainFrame.loadPreferenceValues();
         }
      });


      // Show frame
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            mainFrame.setVisible(true);
         }
      });
   }

   /**
    * INSTANCE VARIABLES
    */
   private ReleaserFrame mainFrame;
}
