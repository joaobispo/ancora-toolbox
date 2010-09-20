/*
 *  Copyright 2010 Ancora Research Group.
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

package org.specs.AutoCompile;

import org.ancora.SharedLibrary.AppBase.Frontend.CommandLine;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.AutoCompile.Target.TargetOption;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();

       // Check if there is option "-generate" or "-help"
       /*
       boolean foundArgument = preparseAguments(args);
       if(foundArgument) {
          return;
       }
        *
        */

        AutoCompile aComp = new AutoCompile();
        CommandLine frontend = new CommandLine(aComp);

        // Link this program options to frontend
        frontend.getOptionClasses().add(TargetOption.class);

        frontend.run(args);

    }






}
