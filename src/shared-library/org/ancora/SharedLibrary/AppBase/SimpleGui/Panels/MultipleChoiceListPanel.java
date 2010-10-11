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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class MultipleChoiceListPanel extends AppOptionPanel {

   public MultipleChoiceListPanel(String labelName, List<String> choices) {
      label = new JLabel(labelName+":");
      selectedValues = new JComboBox();
      removeButton = new JButton("X");
      possibleValues = new JComboBox();
      addButton = new JButton("Add");

      possibleValuesShadow = new ArrayList<String>();
      selectedValuesShadow = new ArrayList<String>();

      // Add possible values
      for(String choice : choices) {
         possibleValues.addItem(choice);
         possibleValuesShadow.add(choice);
      }

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
      add(selectedValues);
      add(removeButton);
      add(possibleValues);
      add(addButton);

      setLayout(new FlowLayout(FlowLayout.LEFT));
   }

   /**
    * Moves one value from possibleValues to selectedValues. This method is not
    * thread-safe.
    * 
    * @param valueName
    * @return
    */
   private boolean addValue(String valueName) {
      if(valueName == null && possibleValuesShadow.isEmpty()) {
         return true;
      }

      // Check if value is available
      if(!possibleValuesShadow.contains(valueName)) {
         LoggingUtils.getLogger().
                 warning("Could not find value '"+valueName+"' in Multiple Choice "
                 + "list. Available choices:"+possibleValuesShadow);
         return false;
      }

      // Remove from possible and add to selected
      possibleValues.removeItem(valueName);
      possibleValuesShadow.remove(valueName);
      selectedValues.addItem(valueName);
      selectedValuesShadow.add(valueName);
      return true;
   }

   /**
    * Moves one value from selectedValues to possibleValues. This method is not
    * thread-safe.
    *
    * @param valueName
    * @return
    */
   private boolean removeValue(String valueName) {
      if(valueName == null && selectedValuesShadow.isEmpty()) {
         return true;
      }
       // Check if value is selected
      if(!selectedValuesShadow.contains(valueName)) {
         LoggingUtils.getLogger().
                 warning("Could not find value '"+valueName+"' in already "
                 + "selected choices. Currently selected choices:"+selectedValuesShadow);
         return false;
      }

      // Remove from possible and add to selected
      selectedValues.removeItem(valueName);
      selectedValuesShadow.remove(valueName);
      possibleValues.addItem(valueName);
      possibleValuesShadow.add(valueName);
      return true;
   }

   /**
    * Adds the option from the avaliable list to selected list.
    *
    * @param evt
    */
    private void addButtonActionPerformed(ActionEvent evt) {
       // Check if there is text in the textfield
       final String selectedValue = (String)possibleValues.getSelectedItem();
       addValue(selectedValue);
       /*
       ProcessUtils.runOnSwing(new Runnable() {
         @Override
         public void run() {
            addValue(selectedValue);
         }
      });
        * 
        */

    }

    /**
     * Removes the option from the selected list to the available list.
     *
     * @param evt
     */
   private void removeButtonActionPerformed(ActionEvent evt) {
      // Check if there is text in the textfield
      final String selectedValue = (String) selectedValues.getSelectedItem();
      removeValue(selectedValue);
   }


   /**
    * The currently selected values.
    *
    * @return currently selected values.
    */
   public List<String> getSelectedValues() {
      return Collections.unmodifiableList(selectedValuesShadow);
   }

   /**
    * For each element in the value list, add it to the selected items.
    *
    * @param value
    */
   public void updatePanel(AppValue value) {
      List<String> values = value.getList();

      //boolean error = false;
      for(String valueName : values) {
         // Check if it is not already in the selected list.
         if(selectedValuesShadow.contains(valueName)) {
            continue;
         }
        // boolean success = addValue(valueName);
         addValue(valueName);
         /*
         if(!success) {
            error = true;
            LoggingUtils.getLogger().
                    warning("Could not find choice '"+valueName+"'");
         }
          *
          */
      }
   }

   /*
   public JComboBox getValues() {
      return selectedValues;
   }
    * 
    */

   /*
   public void setValues(JComboBox values) {
      this.selectedValues = values;
   }
    *
    */

   

   /*
   public void setText(String text) {
      value.setText(text);
   }
    *
    */


   @Override
   public AppValueType getType() {
      return AppValueType.multipleChoiceStringList;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JComboBox selectedValues;
   private JComboBox possibleValues;
   private JButton removeButton;
   private JButton addButton;

   private List<String> possibleValuesShadow;
   private List<String> selectedValuesShadow;


}
