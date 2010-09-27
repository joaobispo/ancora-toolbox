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

import java.io.File;
import javax.swing.JTextField;
import org.ancora.SharedLibrary.AppBase.App;

/**
 *
 * @author Joao Bispo
 */
public class OptionsPanel extends javax.swing.JPanel {

   public OptionsPanel(Class appOptionEnum) {
      //this.filenameTextField = filenameTextField;
      //this.application = application.;
   }

public void updateValues(String optionsFilename) {
   System.err.println("Updated! -> '"+optionsFilename+"'");
   System.out.println("Info level");
   //System.exit(0);
}

   //private JTextField filenameTextField;
   //private App application;
   //private Class appOptionEnum;
}
