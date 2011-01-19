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

import java.io.File;
import org.specs.DToolPlus.DToolUtils;
import org.specs.LoopOptimization.V2.Application;
import org.suikasoft.Jani.App;
import org.suikasoft.Jani.SimpleGui;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.MicroBlaze.MbInstructionName;
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
       DToolUtils.prepareDtoolMicroblaze();

//       LoopOptimizationApplication app = new LoopOptimizationApplication();
       App app = new Application();
       SimpleGui simpleGui = new SimpleGui(app);
       simpleGui.setTitle("Straigh-Line Loop Optimizations v0.2");
       simpleGui.execute();
  

       //generateMbInstructionProperties();
    }

   private static void generateMbInstructionProperties() {
      StringBuilder builder = new StringBuilder();

      for(MbInstructionName instName : MbInstructionName.values()) {
         builder.append(instName.getName());
         builder.append(" = 1\n");
      }

      IoUtils.write(new File("C:/microblaze-cycles.properties"), builder.toString());
   }

}
