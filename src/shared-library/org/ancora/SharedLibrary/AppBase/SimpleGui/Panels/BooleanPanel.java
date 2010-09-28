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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.ancora.SharedLibrary.AppBase.AppValueType;

/**
 *
 * @author Joao Bispo
 */
public class BooleanPanel extends AppOptionPanel {

   public BooleanPanel(String labelName) {
      label = new JLabel(labelName+":");
      checkBox = new JCheckBox();

      add(label);
      add(checkBox);
      setLayout(new FlowLayout(FlowLayout.LEFT));
   }

   public JCheckBox getCheckBox() {
      return checkBox;
   }



   @Override
   public AppValueType getType() {
      return AppValueType.bool;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JCheckBox checkBox;
}
