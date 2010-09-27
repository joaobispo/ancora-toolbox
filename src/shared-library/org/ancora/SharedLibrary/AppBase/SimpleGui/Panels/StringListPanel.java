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

package org.ancora.SharedLibrary.AppBase.SimpleGui.Panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.ancora.SharedLibrary.AppBase.AppValueType;

/**
 *
 * @author Joao Bispo
 */
public class StringListPanel extends JPanel implements AppOptionPanel {

   public StringListPanel(String labelName) {
      label = new JLabel(labelName+":");
      values = new JComboBox();
      removeButton = new JButton("X");
      possibleValue = new JTextField(10);
      addButton = new JButton("Add");

      addButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            addButtonActionPerformed(evt);
         }
      });

      removeButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            removeButtonActionPerformed(evt);
         }

      });

      add(label);
      add(values);
      add(removeButton);
      add(possibleValue);
      add(addButton);

      setLayout(new FlowLayout(FlowLayout.LEFT));
   }


   /**
    * Adds the text in the textfield to the combo box
    * @param evt
    */
    private void addButtonActionPerformed(ActionEvent evt) {
       //System.out.println("Current item number:"+values.getSelectedIndex());
       // Check if there is text in the textfield
       String newValueTrimmed = possibleValue.getText().trim();
       if(newValueTrimmed.isEmpty()) {
          return;
       }

       values.addItem(newValueTrimmed);
    }

    /**
     * Removes the currently selected element of the list.
     * 
     * @param evt
     */
   private void removeButtonActionPerformed(ActionEvent evt) {
      int valueIndex = values.getSelectedIndex();
      if(valueIndex == -1) {
         return;
      }

      values.removeItemAt(valueIndex);
   }

   public JComboBox getValues() {
      return values;
   }

   public void setValues(JComboBox values) {
      this.values = values;
   }

   

   /*
   public void setText(String text) {
      value.setText(text);
   }
    *
    */


   @Override
   public AppValueType getType() {
      return AppValueType.stringList;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JComboBox values;
   private JTextField possibleValue;
   private JButton removeButton;
   private JButton addButton;

}
