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

package org.ancora.SharedLibrary.AppBase.SimpleGui;

import org.ancora.SharedLibrary.AppBase.App;

/**
 *
 * @author Joao Bispo
 */
public class SimpleGui {

   public SimpleGui(App application) {
      //this.application = application;
      this.mainWindow = new MainWindow(application);
   }


   public MainWindow getMainWindow() {
      return mainWindow;
   }

    /**
     * Starts the GUI
     */
    public void execute() {
       java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
            public void run() {
                mainWindow.setVisible(true);
            }
        });
    }

    public void setTitle(String windowTitle) {
       mainWindow.setTitle(windowTitle);
    }

   private MainWindow mainWindow;
   //final private App application;
}
