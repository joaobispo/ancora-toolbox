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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionEnumSetup;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleChoice;
import org.ancora.SharedLibrary.AppBase.AppOption.AppOptionMultipleSetup;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.AppOptionPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.BooleanPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.IntegerPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.MultipleChoicePanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.MultipleChoiceListPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.MultipleSetupListPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.MultipleSetupPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.StringListPanel;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.StringPanel;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Utility methods related to handling of options panels.
 *
 * @author Joao Bispo
 */
public class PanelUtils {

   /**
    * Extracts an AppValue from an AppOptionPanel.
    * 
    * @param panel
    * @return
    */
      public static AppValue getAppValue(AppOptionPanel panel) {
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

      if(panel.getType() == AppValueType.integer) {
         IntegerPanel iPanel = (IntegerPanel)panel;
         String stringValue = iPanel.getText();

            // Check if empty string
            if (stringValue.isEmpty()) {
               stringValue = AppUtils.getZeroValue(iPanel.getType());
               return new AppValue(Integer.parseInt(stringValue));
            }


            Integer newValue = null;
            try {
               newValue = Integer.parseInt(stringValue);
            } catch (NumberFormatException ex) {
               LoggingUtils.getLogger().
                       info("Could not parse '" + iPanel.getText() + "' into an integer.");

               String integerString = AppUtils.getZeroValue(iPanel.getType());
               newValue = Integer.parseInt(integerString);
            }
            return new AppValue(newValue);
         }

      if(panel.getType() == AppValueType.bool) {
         BooleanPanel bPanel = (BooleanPanel)panel;
         return new AppValue(bPanel.getCheckBox().isSelected());
      }

      if(panel.getType() == AppValueType.multipleChoice) {
         MultipleChoicePanel mcPanel = (MultipleChoicePanel)panel;
         return new AppValue((String)mcPanel.getValues().getSelectedItem(), AppValueType.multipleChoice);
      }

      if(panel.getType() == AppValueType.multipleChoiceStringList) {
         MultipleChoiceListPanel mclPanel = (MultipleChoiceListPanel)panel;
         List<String> values = mclPanel.getSelectedValues();
         return new AppValue(values, AppValueType.multipleChoiceStringList);
      }


      if(panel.getType() == AppValueType.multipleSetupList) {
         MultipleSetupListPanel mslPanel = (MultipleSetupListPanel)panel;
         List<String> values = mslPanel.getPackedValues();
         return new AppValue(values, AppValueType.multipleSetupList);
      }
      
      if(panel.getType() == AppValueType.multipleSetup) {
         MultipleSetupPanel mslPanel = (MultipleSetupPanel)panel;
         String value = mslPanel.getSetupFile();
         return new AppValue(value, AppValueType.multipleSetup);
      }


      LoggingUtils.getLogger().
              warning("AppValue extraction for type '"+panel.getType()+"' not implemented yet.");
      return null;
   }


   /**
    * Reads a value from AppValue and updates the value in the corresponding
    * AppOptionPanel.
    *
    * @param panel
    * @param value
    */
   public static void updatePanel(AppOptionPanel panel, AppValue value) {
      AppValueType type = value.getType();
      if (type == AppValueType.string) {
         StringPanel stringPanel = (StringPanel) panel;
         stringPanel.setText(value.toString());
         return;
      }

      if (type == AppValueType.integer) {
         IntegerPanel intPanel = (IntegerPanel) panel;
         intPanel.setText(value.toString());
         return;
      }

      if (type == AppValueType.bool) {
         final BooleanPanel boolPanel = (BooleanPanel) panel;
         final Boolean b = Boolean.parseBoolean(value.get());
         ProcessUtils.runOnSwing(new Runnable() {

            @Override
            public void run() {
               boolPanel.getCheckBox().setSelected(b);
            }
         });

         return;
      }

      if (type == AppValueType.stringList) {
         StringListPanel stringListPanel = (StringListPanel) panel;
         final List<String> values = value.getList();
         final JComboBox box = stringListPanel.getValues();

         ProcessUtils.runOnSwing(new Runnable() {

            @Override
            public void run() {
               box.removeAllItems();
               for (String v : values) {
                  box.addItem(v);
               }
            }
         });

         return;
      }

      if(type == AppValueType.multipleChoice) {
         // Check if the value in AppValue is one of the possible choices
         MultipleChoicePanel multipleChoicePanel = (MultipleChoicePanel) panel;
         multipleChoicePanel.updatePanel(value);

         return;
      }

      if (type == AppValueType.multipleChoiceStringList) {
         MultipleChoiceListPanel multipleChoiceListPanel = (MultipleChoiceListPanel) panel;
         multipleChoiceListPanel.updatePanel(value);
         return;
      }

      if (type == AppValueType.multipleSetupList) {
         MultipleSetupListPanel multipleSetupListPanel = (MultipleSetupListPanel) panel;
         multipleSetupListPanel.updateValue(value);
         return;
      }

      if (type == AppValueType.multipleSetup) {
         MultipleSetupPanel multipleSetupPanel = (MultipleSetupPanel) panel;
         multipleSetupPanel.updateValue(value);
         return;
      }

      LoggingUtils.getLogger().
              warning("Update for type '" + value.getType() + "' not implemented yet.");
   }

   /**
    * Creates a new AppOptionPanel from the given AppOptionEnum.
    *
    * @param enumOption
    * @return
    */
   public static AppOptionPanel newPanel(AppOptionEnum enumOption) {
      AppValueType type = enumOption.getType();
      String panelLabel = AppUtils.parseEnumName(enumOption);
      // Parse label a bit. Add spaces between camel case
      panelLabel = insertSpacesOnCamelCase(panelLabel);

      if(type == AppValueType.string) {
         //return new StringPanel(AppUtils.parseEnumName(enumOption.getName()));
         return new StringPanel(panelLabel);
      }

      if(type == AppValueType.stringList) {
         return new StringListPanel(panelLabel);
      }

      if(type == AppValueType.integer) {
         return new IntegerPanel(panelLabel);
      }


      if(type == AppValueType.bool) {
         return new BooleanPanel(panelLabel);
      }

      if(type == AppValueType.multipleChoice) {
         Enum[] choices = ((AppOptionMultipleChoice)enumOption).getChoices();
         if(choices == null) {
            LoggingUtils.getLogger().
                    warning("No enum values defined for multiple choice option '"+enumOption.getName()+"'");
            return null;
         }

//         return new MultipleChoicePanel(panelLabel, EnumUtils.buildList(choices));
         return new MultipleChoicePanel(panelLabel, EnumUtils.buildListToString(choices));
      }

      if(type == AppValueType.multipleChoiceStringList) {
         Enum[] choices = ((AppOptionMultipleChoice)enumOption).getChoices();
         if(choices == null) {
            LoggingUtils.getLogger().
                    warning("No enum values defined for multiple choice option '"+enumOption.getName()+"'");
            return null;
         }

         //return new MultipleChoiceListPanel(panelLabel, EnumUtils.buildList(choices));
         return new MultipleChoiceListPanel(panelLabel, EnumUtils.buildListToString(choices));
      }

      if(type == AppValueType.multipleSetupList) {
         AppOptionMultipleSetup choices = ((AppOptionMultipleSetup)enumOption);
         if(choices == null) {
            LoggingUtils.getLogger().
                    warning("No enum values defined for multiple setup option '"+enumOption.getName()+"'");
            return null;
         }

         return new MultipleSetupListPanel(enumOption, panelLabel, choices);
      }

      if(type == AppValueType.multipleSetup) {
         AppOptionEnumSetup choices = ((AppOptionEnumSetup)enumOption);
         if(choices == null) {
            LoggingUtils.getLogger().
                    warning("No enum values defined for multiple setup option '"+enumOption.getName()+"'");
            return null;
         }
         
         return new MultipleSetupPanel(enumOption, panelLabel, choices);
      }

      LoggingUtils.getLogger().
              warning("Option type '"+type+"' in option '"+enumOption.getName()+"' not implemented yet.");
      return null;
   }

   private static String insertSpacesOnCamelCase(String panelLabel) {
      //System.out.println("Input String:"+panelLabel);
      List<Integer> upperCaseLetters = new ArrayList<Integer>();
      for(int i=1; i<panelLabel.length(); i++) {
         char c = panelLabel.charAt(i);
         if(!Character.isUpperCase(c)) {
            continue;
         }

         /*
         // Check if previous character is also uppercase.
         // If it is, skip it (to account for cases like OneACRONYM)
         char previousC = panelLabel.charAt(i-1);
         if(Character.isUpperCase(previousC)) {
            continue;
         }
          * 
          */

         upperCaseLetters.add(i);
      }

      //System.out.println("UpperCaseLetters:"+upperCaseLetters);

      // Insert spaces in the found indexes
      String newString = panelLabel;
      for(int i=upperCaseLetters.size()-1; i>=0; i--) {
         int index = upperCaseLetters.get(i);
         newString = newString.substring(0, index) + " " + newString.substring(index, newString.length());
      }

      return newString;
   }


}
