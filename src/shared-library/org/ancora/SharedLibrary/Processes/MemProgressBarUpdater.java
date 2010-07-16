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

package org.ancora.SharedLibrary.Processes;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Joao Bispo
 */
class MemProgressBarUpdater extends SwingWorker {

   public MemProgressBarUpdater(JProgressBar jProgressBar) {
      this.jProgressBar = jProgressBar;
      this.jProgressBar.setStringPainted(true);
   }



   @Override
   protected Object doInBackground() throws Exception {
      long heapSize = Runtime.getRuntime().totalMemory();
      long heapFreeSize = Runtime.getRuntime().freeMemory();
      long usedMemory = heapSize - heapFreeSize;

      long mbFactor = (long)Math.pow(1024, 2);
      long kbFactor = (long)Math.pow(1024, 1);

      heapSizeMb = (int) (heapSize / kbFactor);
      currentSizeMb = (int) (usedMemory / kbFactor);

      java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               String barString = currentSizeMb + "kb/"+heapSizeMb+"kb";

                jProgressBar.setMinimum(0);
      jProgressBar.setMaximum(heapSizeMb);
      jProgressBar.setValue(currentSizeMb);
      jProgressBar.setString(barString);
      //System.err.println("Heap Size:"+heapSizeMb);
      //System.err.println("Current Size:"+currentSizeMb);
            }
        });

      return null;
   }

   @Override
   protected void done() {
      /*
      jProgressBar.setMinimum(0);
      jProgressBar.setMaximum(heapSizeMb);
      jProgressBar.setValue(currentSizeMb);
      System.err.println("Heap Size:"+heapSizeMb);
      System.err.println("Current Size:"+currentSizeMb);
       *
       */
   }



   /**
    * INSTANCE VARIABLES
    */
   private JProgressBar jProgressBar;
   int heapSizeMb;
   int currentSizeMb;

}
