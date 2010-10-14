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
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.ancora.SharedLibrary.AppBase.AppOptionEnumSetup;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppOptionMultipleSetup;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.AppFilePanel;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class MultipleSetupListPanel extends AppOptionPanel {

   public MultipleSetupListPanel(String labelName, AppOptionMultipleSetup setup) {
      // Initiallize objects


      label = new JLabel(labelName+":");
      removeButton = new JButton("X");
      addButton = new JButton("Add");

      initChoices(setup);
      initElements();

      // Add possible values
      /*
      for(String choice : EnumUtils.buildList((Enum[])setup.getSetups())) {
         possibleValues.addItem(choice);
         possibleValuesShadow.add(choice);
      }
       *
       */

      // Add actions
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

      elementsBox.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            elementComboBoxActionPerformed(e);
         }

      });

      // Build choice panel
      choicePanel = buildChoicePanel();
      /*
      choicePanel.add(label);
      choicePanel.add(elements);
      choicePanel.add(removeButton);
      choicePanel.add(possibleValues);
      choicePanel.add(addButton);

      choicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
  */

      //JPanel choicePanel = new MultipleChoiceListPanel(labelName, EnumUtils.buildList((Enum[])setup.getSetups()));
      currentOptionsPanel = null;

      //setLayout(new BorderLayout(5, 5));
      //add(choicePanel, BorderLayout.PAGE_START);
      LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      setLayout(layout);
      add(choicePanel);


      //setups = setup;
   }

   private void initChoices(AppOptionMultipleSetup setupList) {
      setups = new ArrayList<AppOptionEnumSetup>();
      setups.addAll(Arrays.asList(setupList.getSetups()));
      
      choicesBox = new JComboBox();
      choicesBoxShadow = new ArrayList<String>();

      for(AppOptionEnumSetup setup : setups) {
         String setupName = ((Enum)setup).name();
         choicesBox.addItem(setupName);
         choicesBoxShadow.add(setupName);
      }

//            for(String choice : EnumUtils.buildList((Enum[])setup.getSetups())) {
  //       choicesBox.addItem(choice);
    //     choicesBoxShadow.add(choice);
      //}
   }

   private void initElements() {
      elementsBoxShadow = new ArrayList<Integer>();
      elementsBox = new JComboBox();
      elementsFiles = new ArrayList<AppOptionFile>();
      elementsOptionPanels = new ArrayList<AppFilePanel>();
   }

   private JPanel buildChoicePanel() {
      JPanel panel = new JPanel();

      panel.add(label);
      panel.add(elementsBox);
      panel.add(removeButton);
      panel.add(choicesBox);
      panel.add(addButton);

      panel.setLayout(new FlowLayout(FlowLayout.LEFT));

      return panel;
   }

   private void update(JPanel panel) {
//      setLayout(new BorderLayout(5, 5));
      if(currentOptionsPanel != null) {
         remove(currentOptionsPanel);
      }
      currentOptionsPanel = panel;
      add(currentOptionsPanel);
//      setLayout(new BorderLayout(5, 5));
//      add(choicePanel, BorderLayout.PAGE_START);

//      add(appFile, BorderLayout.CENTER);
      validate();
   }

   /**
    * Moves one value from possibleValues to selectedValues. This method is not
    * thread-safe.
    * 
    * @param valueName
    * @return
    */
   private boolean addValue(String valueName) {
      if(valueName == null && choicesBoxShadow.isEmpty()) {
         return true;
      }

      // Check if value is available
      if(!choicesBoxShadow.contains(valueName)) {
         LoggingUtils.getLogger().
                 warning("Could not find value '"+valueName+"' in Multiple Choice "
                 + "list. Available choices:"+choicesBoxShadow);
         return false;
      }

      // Remove from possible and add to selected
      choicesBox.removeItem(valueName);
      choicesBoxShadow.remove(valueName);
      elementsBox.addItem(valueName);
//      elementsBoxShadow.add(valueName);
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
      if(valueName == null && elementsBoxShadow.isEmpty()) {
         return true;
      }
       // Check if value is selected
      if(!elementsBoxShadow.contains(valueName)) {
         LoggingUtils.getLogger().
                 warning("Could not find value '"+valueName+"' in already "
                 + "selected choices. Currently selected choices:"+elementsBoxShadow);
         return false;
      }

      // Remove from possible and add to selected
      elementsBox.removeItem(valueName);
      elementsBoxShadow.remove(valueName);
      choicesBox.addItem(valueName);
      choicesBoxShadow.add(valueName);
      return true;
   }

   /**
    * Adds the option from the avaliable list to selected list.
    *
    * @param evt
    */
    private void addButtonActionPerformed(ActionEvent evt) {
       // Determine what element is selected
       int choice = choicesBox.getSelectedIndex();
       if (choice == -1) {
          return;
       }

       addElement(choice);

       // Check if there is text in the textfield
       //final String selectedValue = (String)choicesBox.getSelectedItem();
       //addValue(selectedValue);
       /*
       ProcessUtils.runOnSwing(new Runnable() {
         @Override
         public void run() {
            addValue(selectedValue);
         }
      });
        * 
        */
//        AppOptionEnumSetup[] enums = setups.getSetups();
       /*
        AppOptionEnumSetup anEnum;
        if(!dummy) {
            anEnum = setups.get(0);
            dummy = true;
       } else {
           anEnum = setups.get(1);
       }
       AppOptionEnum[] otherEnums = anEnum.getSetupOptions();
       AppOptionEnum otherEnum = otherEnums[0];

       JPanel panel = new AppFilePanel(otherEnum.getClass());
       update(panel);
        * 
        */
    }

    /**
     * Removes the option from the selected list to the available list.
     *
     * @param evt
     */
   private void removeButtonActionPerformed(ActionEvent evt) {
      // Determine index of selected element to remove
      int indexToRemove = elementsBox.getSelectedIndex();
      if(indexToRemove == -1) {
         return;
      }
      
      removeElement(indexToRemove);

      // Check if there is text in the textfield
      //final String selectedValue = (String) elementsBox.getSelectedItem();
      //removeValue(selectedValue);
   }

   /**
    * Updates the options panel.
    * 
    * @param e
    */
   private void elementComboBoxActionPerformed(ActionEvent e) {
      updateSetupOptions();
   }

   /**
    * The currently selected values.
    *
    * @return currently selected values.
    */
   public List<String> getSelectedValues() {
      return null;
//      return Collections.unmodifiableList(elementsBoxShadow);
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
         if(elementsBoxShadow.contains(valueName)) {
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
      return AppValueType.multipleSetupList;
   }

   /**
    * Adds an element to the elements list, from the choices list.
    * 
    * @return
    */
   public void addElement(int choice) {
      // Add index to elements
      elementsBoxShadow.add(choice);
      // Get setup options and create option file for element
      AppOptionEnumSetup setup = setups.get(choice);
      Class setupOptionsClass = EnumUtils.getClass((Enum[])setup.getSetupOptions());
      elementsFiles.add(new AppOptionFile(setupOptionsClass));
      elementsOptionPanels.add(new AppFilePanel(setupOptionsClass));
      
      // Refresh
      updateElements();
      // Select last item
      elementsBox.setSelectedIndex(elementsBoxShadow.size()-1);
      // Update vision of setup options - not needed, when we select, automatically updates
      //updateSetupOptions();
   }


   private void updateElements() {
      // Build list of strings to present
      //List<String> comboBoxElements = new ArrayList<String>(elementsBoxShadow.size());
      elementsBox.removeAllItems();
      for(int i=0; i<elementsBoxShadow.size(); i++) {
         // Get choice name
         int choice = elementsBoxShadow.get(i);
         AppOptionEnumSetup setup = setups.get(choice);
         String boxString = (i+1)+ " - "+((Enum)setup).name();
         //comboBoxElements.add(boxString);
         elementsBox.addItem(boxString);
      }

   }

   private void updateSetupOptions() {
      if (currentOptionsPanel != null) {
         remove(currentOptionsPanel);
         currentOptionsPanel = null;
      }

      // Determine what item is selected in the elements combo
      int index = elementsBox.getSelectedIndex();

      if (index != -1) {
         currentOptionsPanel = elementsOptionPanels.get(index);
         add(currentOptionsPanel);
      }

      //validate();
      revalidate();
   }

   /**
    * Removes an element from the elements list.
    *
    * @return
    */
   public void removeElement(int index) {
      // Check if the index is valid
      if(elementsBox.getItemCount() <= index) {
         LoggingUtils.getLogger().
                 warning("Given index ('"+index+"')is too big. Elements size: "+elementsBox.getItemCount());
         return;
      }

      // Remove shadow index, AppOptionFile and panel


      elementsBoxShadow.remove(index);

      // Get setup options and create option file for element
      elementsFiles.remove(index);
      elementsOptionPanels.remove(index);

      // Refresh
      updateElements();

      // Calculate new index of selected element
      int newIndex = calculateIndexAfterRemoval(index);
      if(newIndex != -1) {
         elementsBox.setSelectedIndex(newIndex);
      }

      // Select last item
//      
      // Update vision of setup options
//      updateSetupOptions();


   }

   private int calculateIndexAfterRemoval(int index) {
      int numElements = elementsBox.getItemCount();

      // If there are no elements, return -1
      if(numElements == 0) {
         return -1;
      }

      // If there are enough elements, the index is the same
      if(numElements > index) {
         return index;
      }

      // If size is the same as index, it means that we removed the last element
      // Return the index of the current last element
      if(numElements == index) {
         return index-1;
      }

      LoggingUtils.getLogger().
              warning("Invalid index '"+index+"' for list with '"+numElements+"' elements.");
      return -1;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JPanel currentOptionsPanel;
   private JPanel choicePanel;
   //private AppOptionMultipleSetup setups;

   private JLabel label;
   private JComboBox elementsBox;
   private JComboBox choicesBox;
   private JButton removeButton;
   private JButton addButton;

   private List<String> choicesBoxShadow;
   private List<AppOptionEnumSetup> setups;
   

   private List<Integer> elementsBoxShadow;
   private List<AppOptionFile> elementsFiles;
   private List<AppFilePanel> elementsOptionPanels;

private boolean dummy = false;







}
