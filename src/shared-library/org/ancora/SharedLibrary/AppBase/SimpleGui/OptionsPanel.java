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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.Entry;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.OptionFileUtils;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Interfaces.SetupPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.AppOptionPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.MultipleSetupListPanel;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Panel which loads and can edit the options file.
 *
 * @author Joao Bispo
 */
public class OptionsPanel extends javax.swing.JPanel {

   public OptionsPanel(Class appOptionEnum) {
      JComponent optionsPanel = initEnumOptions(appOptionEnum);
      initSetupPanels();
      // Setup panels must be initallized
      assignNewOptionFile(new AppOptionFile(appOptionEnum));
      fileInfo = new JLabel();
      updateFileInfoString();

      optionClass = appOptionEnum;
      //panels = new HashMap<String, AppOptionPanel>();
      


       saveButton = new JButton("Save");
       // By default, the save button is disable, until there is a valid
       // file to save to.
       saveButton.setEnabled(false);
       saveAsButton = new JButton("Save as...");
       
       

       saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            saveButtonActionPerformed(evt);
         }

      });

       saveAsButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            saveAsButtonActionPerformed(evt);
         }

      });


       JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       savePanel.add(saveButton);
       savePanel.add(saveAsButton);
       savePanel.add(fileInfo);
       

       // If optionFile no file, save button is null;
       // Only "unnulls" after "Save As..." and after successful update.


       //Map<String,AppOptionEnum> enums = AppUtils.getEnumMap(appOptionEnum);
       //JComponent optionsPanel = initEnumOptions(enums);


       //JComponent appFilePanel = new AppFilePanel(appOptionEnum);


       setLayout(new BorderLayout(5, 5));
       add(savePanel, BorderLayout.PAGE_START);

       add(optionsPanel, BorderLayout.CENTER);
       
       /*
       setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       add(savePanel);
       add(Box.createRigidArea(new Dimension(0,5)));
       add(optionsPanel);
       add(Box.createVerticalGlue());
        *
        */

       //add(appFilePanel, BorderLayout.CENTER);
       //panels = ((AppFilePanel)appFilePanel).getPanels();
   }

   /**
    * Find setup panels and adds them to the list
    */
   
   private void initSetupPanels() {
      setupPanels = new ArrayList<SetupPanel>();
      Map<String, AppOptionPanel> panelMap = appFilePanel.getPanels();
      for(String key : panelMap.keySet()) {
         SetupPanel setupPanel = getSetupPanel(panelMap.get(key));
         if(setupPanel == null) {
            continue;
         }
         setupPanels.add(setupPanel);
         // Update option file
         setupPanel.updateMasterFile(optionFile);
      }
   }
    


   private SetupPanel getSetupPanel(AppOptionPanel panel) {
      Class[] interfaces = panel.getClass().getInterfaces();
      Set<Class> classSet = new HashSet<Class>(Arrays.asList(interfaces));
      if(!classSet.contains(SetupPanel.class)) {
         return null;
      }

      return (SetupPanel)panel;
   }

   public AppOptionFile getOptionFile() {
      return optionFile;
   }   

   private void saveButtonActionPerformed(ActionEvent evt) {
      updateInternalMap();
      optionFile.write();
   }

   private void saveAsButtonActionPerformed(ActionEvent evt) {
      JFileChooser fc;

      if(optionFile.getOptionFile() == null) {
        fc = new JFileChooser(new File("./"));
      } else {
         fc = new JFileChooser(optionFile.getOptionFile());
      }
      //JFileChooser fc = new JFileChooser(optionFile.getOptionFile());
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         File file = fc.getSelectedFile();
         updateFile(file);
      }
   }

   public void updateValues(String optionsFilename) {
      // Check if filename is a valid optionsfile
      File file = new File(optionsFilename);
      if (!file.isFile()) {
         LoggingUtils.getLogger().
                 warning("Could not open file '" + optionsFilename + "'");
         optionFile.setOptionFile(null);
         saveButton.setEnabled(false);
         updateFileInfoString();
         return;
      }

      AppOptionFile newOptionFile = AppOptionFile.parseFile(file, optionClass);
      if (newOptionFile == null) {
         LoggingUtils.getLogger().
                 warning("Given file '" + optionsFilename + "' is not a compatible options file.");
         optionFile.setOptionFile(null);
         saveButton.setEnabled(false);
         updateFileInfoString();
         return;
      }

      // Load file
      assignNewOptionFile(newOptionFile);
      //optionFile = newOptionFile;

      //loadValues(optionFile);
      appFilePanel.loadValues(optionFile);
      saveButton.setEnabled(true);
      updateFileInfoString();

   }



   //private JComponent initEnumOptions(Map<String, AppOptionEnum> enums) {
   private JComponent initEnumOptions(Class appOptionEnum) {
      /*
      // Extract the enum values
      AppOptionEnum[] values = AppUtils.getEnumValues(appOptionEnum);

      // Panel which will contain the options
      JPanel options = new JPanel();

      // Add panels, in alphabetical order
      //List<String> keyList = new ArrayList<String>(appOptionEnum.keySet());
      //Collections.sort(keyList);
      //for(String key : keyList) {
      for(int i=0; i<values.length; i++) {
         //AppOptionPanel panel = PanelUtils.newPanel(appOptionEnum.get(key));
         AppOptionPanel panel = PanelUtils.newPanel(values[i]);

         if(panel == null) {
            continue;
         }
         
         options.add(panel);
         panels.put(AppUtils.buildEnumName(values[i]), panel);
      }

      // Make the panel scrollable
      LayoutManager layout = new BoxLayout(options, BoxLayout.Y_AXIS);
      options.setLayout(layout);
*/
//      JPanel options = new AppFilePanel(appOptionEnum);
      appFilePanel = new AppFilePanel(appOptionEnum);
      //this.panels = appFilePanel.getPanels();



      JScrollPane scrollPane = new JScrollPane();
      
      scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
      //scrollPane.setViewportView(options);
      scrollPane.setViewportView(appFilePanel);


      //add(Box.createRigidArea(new Dimension(1,1)));
      //add(scrollPane);
     return scrollPane;
 
   }

   
/*
   private void loadValues(AppOptionFile optionFile) {
      Map<String, AppValue> map = optionFile.getMap();
      Map<String, Entry> entries = optionFile.getEntryList().getEntriesMapping();
      for (String key : optionFile.getMap().keySet()) {
         AppValue value = map.get(key);

         // Get panel
         //JPanel panel = panels.get(key);
         AppOptionPanel panel = panels.get(key);
         PanelUtils.updatePanel(panel, value);
         // Get comments
         updateTooltipText(panel, entries.get(key).getComments());
      }
   }
*/

   /**
    * Collects information in all the panels and updates internal map.
    *
    */
   /*
   private void updateInternalMap() {
      Map<String, AppValue> updatedMap = new HashMap<String, AppValue>();
      for(String key : panels.keySet()) {
         JPanel panel = panels.get(key);
         AppValue value = PanelUtils.getAppValue((AppOptionPanel)panel);
         if(value == null) {
            LoggingUtils.getLogger().
                    warning("value is null.");
            // No valid value for the table
            continue;
         }
         updatedMap.put(key, value);
      }
      
      // Update internal optionfile
      optionFile.update(updatedMap);
   }
    *
    */

   private void updateFileInfoString() {
      File file = optionFile.getOptionFile();
      String filename;
      if(file == null) {
         filename = "none";
      } else {
         filename = "'"+file.getName()+"'";
      }

      String text = "Selected file: "+filename;
      fileInfo.setText(text);
   }

   /*
    * Sets the current option file to the given file.
    */
   private void updateFile(File file) {
      updateInternalMap();
      optionFile.setOptionFile(file);
      saveButton.setEnabled(true);
      updateFileInfoString();
   }

   private void updateInternalMap() {
      // Get info from panels
      Map<String, AppValue> updatedMap = appFilePanel.getMapWithValues();
      // Update internal optionfile
      optionFile.update(updatedMap);
   }


   /**
    * Can only be called after setup panels are initallized.
    * 
    * @param newOptionFile
    */
   private void assignNewOptionFile(AppOptionFile newOptionFile) {
      optionFile = newOptionFile;
      for(SetupPanel setup : setupPanels) {
         setup.updateMasterFile(newOptionFile);
      }
   }

/*
   private void updateTooltipText(final JPanel panel, List<String> comments) {

      final String commentString = OptionFileUtils.parseComments(comments);
      ProcessUtils.runOnSwing(new Runnable() {

         @Override
         public void run() {
            panel.setToolTipText(commentString);
         }
      });
   }
*/

   //private Map<String, AppOptionPanel> panels;
   private AppFilePanel appFilePanel;
   //private AppFilePanel optionsPanel;
   
   private Class optionClass;
   private AppOptionFile optionFile;
   private JButton saveButton;
   private JButton saveAsButton;
   private JLabel fileInfo;

   private List<SetupPanel> setupPanels;




}
