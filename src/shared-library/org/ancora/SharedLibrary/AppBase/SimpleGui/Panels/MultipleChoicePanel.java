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
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.AppValueType;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 *
 * @author Joao Bispo
 */
public class MultipleChoicePanel extends AppOptionPanel {

   public MultipleChoicePanel(String labelName, List<String> choices) {
      label = new JLabel(labelName+":");
      values = new JComboBox();
      availableChoices = choices;

      for(String choice : choices) {
         values.addItem(choice);
      }

      add(label);
      add(values);

      setLayout(new FlowLayout(FlowLayout.LEFT));
   }



   public JComboBox getValues() {
      return values;
   }

   /*
   public void setValues(JComboBox values) {
      this.values = values;
   }
    * 
    */

   /**
    * Selects the option in AppValue object. If the option could not be found,
    * selects the first option.
    *
    * @param value
    * @return true if the option is one of the available choices and could be
    * selected, false otherwise
    */
   public boolean updatePanel(AppValue value) {
         // Check if the value in AppValue is one of the possible choices

         final JComboBox box = this.values;
         final String currentChoice = value.get();

         boolean foundChoice = availableChoices.contains(currentChoice);


         if (!foundChoice) {
            LoggingUtils.getLogger().
                    warning("Could not find choice '" + currentChoice + "'. Available "
                    + "choices: " + availableChoices);

            return false;
         }


         ProcessUtils.runOnSwing(new Runnable() {
            @Override
            public void run() {
               box.setSelectedItem(currentChoice);
            }
         });

         return true;
   }


   @Override
   public AppValueType getType() {
      return AppValueType.multipleChoice;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JComboBox values;
   private List<String> availableChoices;

}
