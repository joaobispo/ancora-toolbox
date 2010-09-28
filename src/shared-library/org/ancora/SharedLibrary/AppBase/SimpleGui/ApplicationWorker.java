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

package org.ancora.SharedLibrary.AppBase.SimpleGui;

import java.util.Map;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Launches an App object from the ProgramPanel.
 *
 * @author Joao Bispo
 */
//public class ApplicationWorker extends SwingWorker<Integer, String> {
public class ApplicationWorker implements Runnable {

   public ApplicationWorker(ProgramPanel programPanel,  Map<String,AppValue> options) {
   //public ApplicationWorker(MainWindow mainWindow, String filename) {
      this.mainWindow = programPanel;
      this.options = options;
      this.returnValue = null;
      //this.filename = filename;
   }


   @Override
   public void run() {
      // Disable buttons
      ProcessUtils.runOnSwing(new Runnable() {

         @Override
         public void run() {
            mainWindow.setButtonsEnable(false);
         }
      });

      returnValue = mainWindow.getApplication().execute(options);

      // Enable buttons again
       ProcessUtils.runOnSwing(new Runnable() {

         @Override
         public void run() {
            mainWindow.setButtonsEnable(true);
         }
      });
   }
   /*
   @Override
   protected Integer doInBackground() throws Exception {
      // Disable buttons
      ProcessUtils.runOnSwing(new Runnable() {
         public void run() {
            mainWindow.setButtonsEnable(false);
         }
      });
       

      return mainWindow.getApplication().execute(options);
       // Launch AutoCompiler
       //Launcher.launchAutoCompile(filename);


       //return "";
   }

   @Override
   protected void done() {
      ProcessUtils.runOnSwing(new Runnable() {

         public void run() {
            mainWindow.setButtonsEnable(true);
         }
      });
   }
   */

   public Integer getReturnValue() {
      return returnValue;
   }



   private ProgramPanel mainWindow;
   private Map<String,AppValue> options;
   private Integer returnValue;

   //private  String filename;


}
