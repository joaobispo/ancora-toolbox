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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.Entry;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.Utils;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.AppOptionPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.StringListPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.StringPanel;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class OptionsPanel extends javax.swing.JPanel {

   public OptionsPanel(Class appOptionEnum) {
      optionClass = appOptionEnum;
panels = new HashMap<String, JPanel>();
       Map<String,AppOptionEnum> enums = AppUtils.getEnumMap(appOptionEnum);
       optionFile = new AppOptionFile(appOptionEnum);


       saveButton = new JButton("Save");
       // By default, the save button is disable, until there is a valid
       // file to save to.
       saveButton.setEnabled(false);
       saveAsButton = new JButton("Save as...");
       
       fileInfo = new JLabel();
       updateFileInfoString();

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

      // System.out.println("Enums:"+enums);
      //this.filenameTextField = filenameTextField;
      //this.application = application.;
       //List<Component> components = new ArrayList<Component>();
       //components.add(new JLabel("Label:"));
       //components.add(new JTextField("a text field"));
       //components.add(new JButton("button"));
       //initComponents(components);

       JComponent optionsPanel = initEnumOptions(enums);
       //initComponentsPanel();
       //initComponentsSelf();

       setLayout(new BorderLayout(5, 5));
       add(savePanel, BorderLayout.PAGE_START);
       add(optionsPanel, BorderLayout.CENTER);
   }

   public AppOptionFile getOptionFile() {
      return optionFile;
   }

   

   private void saveButtonActionPerformed(ActionEvent evt) {
      updateInternalMap();
      optionFile.write();
   }

   private void saveAsButtonActionPerformed(ActionEvent evt) {
      JFileChooser fc = new JFileChooser(optionFile.getOptionFile());
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         File file = fc.getSelectedFile();
         updateFile(file);
      }
   }

public void updateValues(String optionsFilename) {
   // Check if filename is a valid optionsfile
   File file = new File(optionsFilename);
   if(!file.isFile()) {
      LoggingUtils.getLogger().
              warning("Could not open file '"+optionsFilename+"'");
      optionFile.setOptionFile(null);
      saveButton.setEnabled(false);
      updateFileInfoString();
      return;
   }
   
   AppOptionFile newOptionFile = AppOptionFile.parseFile(file, optionClass);
   if(newOptionFile == null) {
      LoggingUtils.getLogger().
              warning("Given file '"+optionsFilename+"' is a compatible options file.");
      optionFile.setOptionFile(null);
      saveButton.setEnabled(false);
      updateFileInfoString();
      return;
   }

   // Load file
   optionFile = newOptionFile;
   loadValues(optionFile);
   saveButton.setEnabled(true);
   updateFileInfoString();

}
/*

   public JPanel buildStringPanel(String name) {
         JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
   panel.add(new JLabel(name+":"));
   JTextField textField = new JTextField("textfield");
   panel.add(new );
   panel.add(new JButton("button "+i));
      return null;
   }
 * 
 */

public JPanel buildPanel(int i) {
   JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
   panel.add(new JLabel("Label "+i+":"));
   panel.add(new JTextField("textfield"));
   panel.add(new JButton("button "+i));

   return panel;
}

/*
   private void initComponents(List<Component> components) {
      LayoutManager layout = new FlowLayout();

      setLayout(null);
      for(Component component : components) {
         add(component);
      }
   }
 *
 */
private void initComponentsSelf() {
    int numOptions = 30;

      //LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);

       // Set size
//       setMaximumSize(new Dimension(100, 100));

       // Panel which will contain the options
       //JPanel options = new JPanel();
    //BoxLayout layout = new BoxLayout(options, BoxLayout.Y_AXIS);
      //LayoutManager layout = new GridLayout(numOptions, 1, 10, 10);
      //setLayout(layout);
     // options.setLayout(layout);

      //setMaximumSize(new Dimension(100, 100));
      //options.setMaximumSize(new Dimension(100, 100));

      for(int i=1;i<=numOptions;i++) {
         add(buildPanel(i));

         //options.add(buildPanel(i));
      }

       //JScrollPane scrollPane = new JScrollPane(options);
       //scrollPane.setMaximumSize(new Dimension(100, 100));
       //add(scrollPane);

}

   private void initComponentsPanel() {
      int numOptions = 50;

      //LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);




       // Set size
       //setMaximumSize(new Dimension(100, 100));

       // Panel which will contain the options
       JPanel options = new JPanel();
      

      //options.setMaximumSize(new Dimension(100, 100));
      //options.setPreferredSize(new Dimension(450, 250));

      for(int i=1;i<=numOptions;i++) {
         //add(buildPanel(i));
         options.add(buildPanel(i));
      }
      //LayoutManager layout = new GridLayout(numOptions, 1, 10, 10);
            //LayoutManager layout = new BorderLayout(10, 10);
            LayoutManager layout = new BoxLayout(options, BoxLayout.Y_AXIS);
      options.setLayout(layout);

       JScrollPane scrollPane = new JScrollPane();
       //JScrollPane scrollPane = new JScrollPane(options);
       //scrollPane.setMaximumSize(new Dimension(100, 100));
       add(scrollPane);
       
       scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH+10, AppFrame.PREFERRED_HEIGHT+10));
       scrollPane.setViewportView(options);
      //add(options);
   }

   private JComponent initEnumOptions(Map<String, AppOptionEnum> enums) {
      // Panel which will contain the options
      JPanel options = new JPanel();

      // Add panels
      for(String key : enums.keySet()) {
         JPanel panel = getPanel(enums.get(key));

         if(panel == null) {
            continue;
         }
         
         options.add(panel);
         panels.put(key, panel);
      }

      // Make the panel scrollable
      LayoutManager layout = new BoxLayout(options, BoxLayout.Y_AXIS);
      options.setLayout(layout);

      JScrollPane scrollPane = new JScrollPane();
      
      scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
      scrollPane.setViewportView(options);

      //add(scrollPane);
      return scrollPane;
   }

   private JPanel getPanel(AppOptionEnum enumOption) {
      AppValueType type = enumOption.getType();
      if(type == AppValueType.string) {
         return new StringPanel(parseEnumName(enumOption.getName()));
      }

      if(type == AppValueType.stringList) {
         return new StringListPanel(parseEnumName(enumOption.getName()));
      }

      LoggingUtils.getLogger().
              warning("Option type '"+type+"' in option '"+enumOption.getName()+"' not implemented yet.");
      return null;
   }

/*
   private void initComponentsHard(List<Component> horizontalComponents) {
      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      ParallelGroup phGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
      SequentialGroup horizontalGroup = layout.createSequentialGroup();
      phGroup.addGroup(horizontalGroup);

      horizontalGroup.addContainerGap();
      if(!horizontalComponents.isEmpty()) {
         horizontalGroup.addComponent(horizontalComponents.get(0));
      }
      for (int i = 1; i < horizontalComponents.size(); i++) {
         horizontalGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
         horizontalGroup.addComponent(horizontalComponents.get(i));
      }
      horizontalGroup.addContainerGap(212, Short.MAX_VALUE);


      layout.setHorizontalGroup(horizontalGroup);



       ParallelGroup pvGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
      SequentialGroup verticalGroup = layout.createSequentialGroup();

      verticalGroup.addContainerGap();
      ParallelGroup v2Group = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE);
      for(int i=0; i<horizontalComponents.size(); i++) {
         v2Group.addComponent(horizontalComponents.get(i));
      }
      verticalGroup.addContainerGap(266, Short.MAX_VALUE);

      pvGroup.addGroup(verticalGroup);
       layout.setVerticalGroup(pvGroup);
//       layout.setVerticalGroup(
//         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         //.addGroup(layout.createSequentialGroup()
           // .addContainerGap()
           // .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
           //    .addComponent(jLabel1)
           //    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
           //    .addComponent(jButton1))
           // .addContainerGap(266, Short.MAX_VALUE))
      //);
   }
 *
 */


   /**
    * Locates the index of the last '.' and returns everything after '.'
    * @param name
    * @return
    */
   private String parseEnumName(String name) {
      int index = name.lastIndexOf(".");
      if(index == -1) {
         return name;
      }

      if(index == name.length()-1) {
         return name;
      }

      return name.substring(index+1, name.length());
   }


   private void loadValues(AppOptionFile optionFile) {
      Map<String, AppValue> map = optionFile.getMap();
      Map<String, Entry> entries = optionFile.getEntryList().getEntriesMapping();
      for (String key : optionFile.getMap().keySet()) {
         AppValue value = map.get(key);

         // Get panel
         JPanel panel = panels.get(key);
         updatePanel(panel, value);
         // Get comments
         updateTooltipText(panel, entries.get(key).getComments());
      }
   }


   private void updatePanel(JPanel panel, AppValue value) {
      AppValueType type = value.getType();
      if(type == AppValueType.string) {
         StringPanel stringPanel = (StringPanel) panel;
         stringPanel.setText(value.toString());
         return;
      }

      if(type == AppValueType.stringList) {
         StringListPanel stringListPanel = (StringListPanel) panel;
         List<String> values = value.getList();
         JComboBox box = stringListPanel.getValues();
         box.removeAllItems();
         for(String v : values) {
            box.addItem(v);
         }
         //stringListPanel.setValues(new JComboBox(value.getList().toArray()));
         return;
      }

      LoggingUtils.getLogger().
              warning("Update for type '"+value.getType()+"' not implemented yet.");
   }

   /**
    * Collects information in all the panels and updates internal map.
    *
    */
   private void updateInternalMap() {
      //Map<String, AppValue> oldMap = optionFile.getMap();
      Map<String, AppValue> updatedMap = new HashMap<String, AppValue>();
      for(String key : panels.keySet()) {
         JPanel panel = panels.get(key);
         AppValue value = getAppValue((AppOptionPanel)panel);
         if(value == null) {
            continue;
         }
         updatedMap.put(key, value);
      }
      optionFile.update(updatedMap);
   }

   private AppValue getAppValue(AppOptionPanel panel) {
      if(panel.getType() == AppValueType.string) {
         StringPanel sPanel = (StringPanel)panel;
         return new AppValue(sPanel.getText());
      }

      if(panel.getType() == AppValueType.stringList) {
         StringListPanel sPanel = (StringListPanel)panel;
         JComboBox values = sPanel.getValues();
         List<String> newValues = new ArrayList<String>();
         for(int i=0; i<values.getItemCount(); i++) {
            newValues.add((String)values.getItemAt(i));
         }
         return new AppValue(newValues);
      }

      LoggingUtils.getLogger().
              warning("AppValue extraction for type '"+panel.getType()+"' not implemented yet.");
      return null;
   }


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

   private void updateFile(File file) {
      optionFile.setOptionFile(file);
      saveButton.setEnabled(true);
      updateFileInfoString();
   }


   private void updateTooltipText(JPanel panel, List<String> comments) {
      // Build a string of text with the last non-empty lines
      int counter = 0;
      for(int i = comments.size()-1; i>=0; i--) {
         String line = comments.get(i);
         if(line.isEmpty()) {
            break;
         }

         counter++;
      }

      StringBuilder builder = new StringBuilder();
      for(int i=comments.size()-counter; i<comments.size(); i++) {
         String trimmedLine = comments.get(i).trim();
         trimmedLine = trimmedLine.substring(Utils.COMMENT_PREFIX.length());
         builder.append(trimmedLine);
         builder.append(" ");
      }

      panel.setToolTipText(builder.toString());
   }

   //private JTextField filenameTextField;
   //private App application;
   //private Class appOptionEnum;
   private Map<String, JPanel> panels;
   private Class optionClass;
   private AppOptionFile optionFile;
   private JButton saveButton;
   private JButton saveAsButton;
   private JLabel fileInfo;








}
