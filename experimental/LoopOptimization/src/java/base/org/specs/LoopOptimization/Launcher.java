/*
 *  Copyright 2011 SuikaSoft.
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

package org.specs.LoopOptimization;

import org.suikasoft.Jani.SimpleGui;
import org.suikasoft.SharedLibrary.ProcessUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       ProcessUtils.programStandardInit();

       LoopOptimizationApplication app = new LoopOptimizationApplication();
       SimpleGui simpleGui = new SimpleGui(app);
       simpleGui.setTitle("Straigh-Line Loop Optimizations v0.1");
       simpleGui.execute();
    }

}
