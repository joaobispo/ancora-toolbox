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

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.ancora.SharedLibrary.AppBase.App;

/**
 * Frame of the SimpleGui.
 *
 * @author Joao Bispo
 */
public class AppFrame {

   public AppFrame(App application) {
      frameTitle = "";
      tabbedPane = new TabbedPane(application);
 //     preferredHeight = PREFERRED_HEIGHT;
 //     preferredWidth = PREFERRED_WIDTH;
   }

   /*
   public void setPreferredHeight(int preferredHeight) {
      this.preferredHeight = preferredHeight;
   }

   public void setPreferredWidth(int preferredWidth) {
      this.preferredWidth = preferredWidth;
   }
*/
   

   public void setFrameTitle(String frameTitle) {
      this.frameTitle = frameTitle;
   }


   public void launchGui() {
       SwingUtilities.invokeLater(new Runnable() {
         @Override
            public void run() {
                //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
   }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(frameTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        //Add content to the window.
        frame.add(tabbedPane, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * INSTANCE VARIABLES
     */
    private String frameTitle;
    private TabbedPane tabbedPane;
    //private int preferredHeight;
    //private int preferredWidth;

    public static final int PREFERRED_HEIGHT = 236;
    public static final int PREFERRED_WIDTH = 480;
}
