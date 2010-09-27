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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppUtils;

/**
 *
 * @author Joao Bispo
 */
public class OptionsPanel extends javax.swing.JPanel {

   public OptionsPanel(Class appOptionEnum) {

       Map<String,AppOptionEnum> enums = AppUtils.getEnumMap(appOptionEnum);
       System.out.println("Enums:"+enums);
      //this.filenameTextField = filenameTextField;
      //this.application = application.;
       //List<Component> components = new ArrayList<Component>();
       //components.add(new JLabel("Label:"));
       //components.add(new JTextField("a text field"));
       //components.add(new JButton("button"));
       //initComponents(components);

       initComponentsPanel();
       //initComponentsSelf();
   }

public void updateValues(String optionsFilename) {
   
}

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
       scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH, AppFrame.PREFERRED_HEIGHT));
       scrollPane.setViewportView(options);
      //add(options);
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

   //private JTextField filenameTextField;
   //private App application;
   //private Class appOptionEnum;
}
