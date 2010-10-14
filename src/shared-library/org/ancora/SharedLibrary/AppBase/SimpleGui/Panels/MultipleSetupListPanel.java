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
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOptionEnumSetup;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppOptionMultipleSetup;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.AppFilePanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Interfaces.SetupPanel;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class MultipleSetupListPanel extends AppOptionPanel 
        implements SetupPanel {

   public MultipleSetupListPanel(AppOptionEnum enumOption, String labelName, AppOptionMultipleSetup setup) {
      // Initiallize objects
      id = enumOption;
      masterFile = null;
      label = new JLabel(labelName+":");
      removeButton = new JButton("X");
      addButton = new JButton("Add");

      initChoices(setup);
      initElements();

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

      currentOptionsPanel = null;

      //setLayout(new BorderLayout(5, 5));
      //add(choicePanel, BorderLayout.PAGE_START);
      LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      setLayout(layout);
      add(choicePanel);

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
   /*
   public List<String> getSelectedValues() {
      return null;
//      return Collections.unmodifiableList(elementsBoxShadow);
   }
    *
    */

   /**
    * For each element in the value list, add it to the selected items.
    *
    * @param value
    */
   /*
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
   /*
      }
   }
*/

   @Override
   public AppValueType getType() {
      return AppValueType.multipleSetupList;
   }

   /**
    * Adds an element to the elements list, from the choices list.
    * 
    * @return the index of the added element
    */
   public int addElement(int choice) {
      // Add index to elements
      elementsBoxShadow.add(choice);
      // Get setup options and create option file for element
      AppOptionEnumSetup setup = setups.get(choice);
      Class setupOptionsClass = EnumUtils.getClass((Enum[])setup.getSetupOptions());
      elementsFiles.add(new AppOptionFile(setupOptionsClass));
      elementsOptionPanels.add(new AppFilePanel(setupOptionsClass));

      // Refresh
      updateElementsComboBox();

      int elementIndex = elementsBoxShadow.size()-1;
      // Select last item
      elementsBox.setSelectedIndex(elementIndex);
      // Update vision of setup options - not needed, when we select, automatically updates
      //updateSetupOptions();

      return elementIndex;
   }

   /**
    * Loads several elements from an AppValue.
    * 
    * @param choice
    */
   public void updateValue(AppValue value) {
      // Clear previous values
      clearElements();


      //Unpack value (filename and class)
      List<String> stringValues = value.getList();
      //System.out.println("List size:"+stringValues.size());
      for (int i = 0; i < stringValues.size(); i++) {
         List<Object> unpackedValues = AppUtils.unpackSetup(stringValues.get(i));
         String enumName = (String) unpackedValues.get(0);
         File aFile = (File) unpackedValues.get(1);
         loadElement(enumName, aFile);
      }

   }

   /**
    * Loads a single element from a file
    *
    * @param aClass
    * @param aFile
    */
   private void loadElement(String enumName, File aFile) {
//      System.out.println("Loading:"+enumName+"; "+aFile);
      //int setupIndex = setups.indexOf(enumName);
      int setupIndex = choicesBoxShadow.indexOf(enumName);

      if(setupIndex == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find enum '"+enumName+"'. Available enums:"+setups);
         return;
      }
      
      // Create element
      int elementsIndex = addElement(setupIndex);

      // Update values
      AppOptionEnumSetup setup = setups.get(setupIndex);
      Class setupOptionsClass = EnumUtils.getClass((Enum[])setup.getSetupOptions());
      AppOptionFile optionFile = AppOptionFile.parseFile(aFile, setupOptionsClass);

      // Set option file
      elementsFiles.set(elementsIndex, optionFile);
      // Load values in the file
      elementsOptionPanels.get(elementsIndex).loadValues(optionFile);
      //elementsOptionPanels.get(elementsIndex).getPanels()


/*
      


      // Add index to elements
      elementsBoxShadow.add(choice);
      // Get setup options and create option file for element
      AppOptionEnumSetup setup = setups.get(choice);
      Class setupOptionsClass = EnumUtils.getClass((Enum[]) setup.getSetupOptions());
      elementsFiles.add(new AppOptionFile(setupOptionsClass));
      elementsOptionPanels.add(new AppFilePanel(setupOptionsClass));

      // Refresh
      updateElements();
      // Select last item
      elementsBox.setSelectedIndex(elementsBoxShadow.size() - 1);
      // Update vision of setup options - not needed, when we select, automatically updates
      //updateSetupOptions();
 * 
 */
   }



   private void updateElementsComboBox() {
      // Build list of strings to present
      elementsBox.removeAllItems();
      for(int i=0; i<elementsBoxShadow.size(); i++) {
         // Get choice name
         int choice = elementsBoxShadow.get(i);
         AppOptionEnumSetup setup = setups.get(choice);
         String boxString = (i+1)+ " - "+((Enum)setup).name();
         elementsBox.addItem(boxString);
      }
   }

   private void updateSetupOptions() {
//      System.out.println("CurrentOptionsPanel:"+currentOptionsPanel);
//      System.out.println("ElementsBoxIndex:"+elementsBox.getSelectedIndex());
//      System.out.println("OptionPanelsSize:"+elementsOptionPanels.size());
      if (currentOptionsPanel != null) {
         remove(currentOptionsPanel);
         currentOptionsPanel = null;
      }

      // Determine what item is selected in the elements combo
      int index = elementsBox.getSelectedIndex();

      if (index != -1) {
         currentOptionsPanel = elementsOptionPanels.get(index);
         add(currentOptionsPanel);
         currentOptionsPanel.revalidate();
         //System.out.println("Validated");
      }

      //revalidate();
      // TODO: Is it repaint necessary here, or revalidate on panel solves it?
      //repaint();
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
      elementsFiles.remove(index);
      elementsOptionPanels.remove(index);

      // Refresh
      updateElementsComboBox();

      // Calculate new index of selected element and select it
      int newIndex = calculateIndexAfterRemoval(index);
      if(newIndex != -1) {
         elementsBox.setSelectedIndex(newIndex);
      }
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

/*
   private boolean isClassValid(Class aClass) {
      AppOptionEnumSetup setup = setups.get(0);
      Class panelEnum = ((Enum)setup).getDeclaringClass();
      System.out.println("Panel enum class:"+panelEnum+"; Loaded class:"+aClass);

      return aClass.equals(panelEnum);
   }
*/

   public List<String> getPackedValues() {
      List<String> packedValues = new ArrayList<String>();

      // Get folder
      File masterF = masterFile.getOptionFile();
      if (masterF == null) {
         LoggingUtils.getLogger().
                 warning("No option file defined.");
         return packedValues;
      }
      String newFoldername = masterF.getPath()+"."+id.getName();
     // System.out.println(masterF);
     // System.out.println(id);
     // System.out.println(id.getName());
     // File panelFolder = new File(masterF.getParentFile(), id.getName());
     // IoUtils.safeFolder(panelFolder.getPath());
      File panelFolder = IoUtils.safeFolder(newFoldername);
      // Delete previous values
      File[] filesForDeletion = panelFolder.listFiles();
      for(File deletedFile : filesForDeletion) {
         if(!deletedFile.delete()) {
            LoggingUtils.getLogger().
                    info("Could not delete file '"+deletedFile.getPath()+"'");
         }
      }


      // For each selected panel, create an file and write the corresponding optionfile
      for(int i=0; i<elementsOptionPanels.size(); i++) {
         int choicesIndex = elementsBoxShadow.get(i);
         String choiceEnumName = choicesBoxShadow.get(choicesIndex);
         // Form filename
         String filename = (i+1)+"-"+choiceEnumName;
         File setupFile = new File(panelFolder, filename);

         // Pack information and store
         packedValues.add(AppUtils.packSetup(choiceEnumName, setupFile));

         // Save information
         AppOptionFile optionFile = elementsFiles.get(i);
         optionFile.setOptionFile(setupFile);
         // Get info from panels
         optionFile.update(elementsOptionPanels.get(i).getMapWithValues());
         // Write file
         optionFile.write();
      }

      return packedValues;
   }


   @Override
   public void updateMasterFile(AppOptionFile optionFile) {
      masterFile = optionFile;
   }


   private void clearElements() {
      elementsBox.removeAllItems();

      elementsBoxShadow = new ArrayList<Integer>();
      elementsFiles = new ArrayList<AppOptionFile>();
      elementsOptionPanels = new ArrayList<AppFilePanel>();
   }

   /**
    * INSTANCE VARIABLES
    */
   private JPanel currentOptionsPanel;
   private JPanel choicePanel;

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

   private AppOptionEnum id;
   private AppOptionFile masterFile;






}
