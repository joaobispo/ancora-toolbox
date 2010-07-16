/*
 *  Copyright 2010 SPECS Research Group.
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

package org.specs.Test;

import java.util.List;
import org.specs.ProgramLauncher.Program;
import org.specs.OptionsTable.OptionsTable;

/**
 *
 * @author Joao Bispo
 */
public class TestProgram implements Program {

   public boolean execute(List<String> arguments, OptionsTable state) {
      System.out.println("I'm alive!");
      System.out.println("My arguments:"+arguments);
      System.out.println("Some state:"+state.get(TestOption.option1));
      return true;
   }

   public String getHelpMessage(OptionsTable state) {
      return "No help message defined";
   }

}
