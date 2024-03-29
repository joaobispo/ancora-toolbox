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
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.ancora.SharedLibrary.AppBase.AppValueType;

/**
 *
 * @author Joao Bispo
 */
public class StringPanel extends AppOptionPanel {

   public StringPanel(String labelName) {
      label = new JLabel(labelName+":");
      value = new JTextField(15);

      add(label);
      add(value);
      //setLayout(new FlowLayout(FlowLayout.LEFT));
      FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
      //flowLayout.setVgap(10);
      setLayout(flowLayout);
      //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
   }

   public void setText(String text) {
      value.setText(text);
   }

   public String getText() {
      return value.getText();
   }

   @Override
   public AppValueType getType() {
      return AppValueType.string;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JTextField value; 
}
