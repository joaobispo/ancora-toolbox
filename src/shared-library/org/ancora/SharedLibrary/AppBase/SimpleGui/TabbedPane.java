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

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.ancora.SharedLibrary.AppBase.App;

/**
 *
 * @author Joao Bispo
 */
public class TabbedPane extends JPanel {

   public TabbedPane(App application) {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();


        this.programPanel = new ProgramPanel(application);

       
        tabbedPane.addTab("Program", programPanel);
        tabbedPane.setMnemonicAt(PROGRAM_PANEL, KeyEvent.VK_1);

      this.optionsPanel = new OptionsPanel(application.getAppOptionEnum());
      tabbedPane.addTab("Options", optionsPanel);
      tabbedPane.setMnemonicAt(OPTIONS_PANEL, KeyEvent.VK_2);

      /*
      tabbedPane.addFocusListener(new java.awt.event.FocusAdapter() {


         @Override
         public void focusGained(java.awt.event.FocusEvent evt) {
            jTabbedPane2FocusGained(evt);
         }
      });
          *
          */
/*
      tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTabbedPane2MouseClicked(evt);
         }
      });
*/

      // Register a change listener
      tabbedPane.addChangeListener(new ChangeListener() {
         // This method is called whenever the selected tab changes
         @Override
         public void stateChanged(ChangeEvent evt) {
            JTabbedPane pane = (JTabbedPane) evt.getSource();

            // Get current tab
            int sel = pane.getSelectedIndex();
            if(sel == OPTIONS_PANEL) {
               String optionsFilename = programPanel.getFilenameTextField().getText();
               optionsPanel.updateValues(optionsFilename);
            }
         }
      });


        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    }

   /*
    private void jTabbedPane2FocusGained(java.awt.event.FocusEvent evt) {
       optionsPanel.focusGained();
    }
    
    
       private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {
       optionsPanel.updateValues();
    }
    * 
    */

    private ProgramPanel programPanel;
    private OptionsPanel optionsPanel;
   //private App application;
   //private JTextField inputFileTextField;

    private final static int PROGRAM_PANEL = 0;
    private final static int OPTIONS_PANEL = 1;
}
