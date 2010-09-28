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

package org.specs.AutoCompile.extra;

import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Job.JobOption;
import org.specs.AutoCompile.Targets.TargetOption;

/**
 *
 * @author Joao Bispo
 */
public class JaniProgram implements App {

   public static void main(String[] args) {
      LoggingUtils.setupConsoleOnly();

      JaniProgram jani = new JaniProgram();
      SimpleGui gui = new SimpleGui(jani);
      gui.execute();
   }

   public int execute(Map<String, AppValue> options) {
      System.err.println("LOVE!!!!!");
      return 1000;
   }

   public Class getAppOptionEnum() {
      return JobOption.class;
   }

}
