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

package org.specs.AutoCompile.Gui;

import javax.swing.SwingWorker;
import org.ancora.SharedLibrary.ProcessUtils;
import org.specs.AutoCompile.Launcher;

/**
 *
 * @author Joao Bispo
 */
public class AutoCompileRunner extends SwingWorker<String, String> {

   public AutoCompileRunner(Main mainWindow, String filename) {
      this.mainWindow = mainWindow;
      this.filename = filename;
   }

   @Override
   protected String doInBackground() throws Exception {
      // Disable buttons
      ProcessUtils.runOnSwing(new Runnable() {
         public void run() {
            mainWindow.setButtonsEnable(false);
         }
      });
       

       // Launch AutoCompiler
       Launcher.launchAutoCompile(filename);


       return "";
   }

   @Override
   protected void done() {
      ProcessUtils.runOnSwing(new Runnable() {

         public void run() {
            mainWindow.setButtonsEnable(true);
         }
      });
   }


   private Main mainWindow;
   private  String filename;


}
