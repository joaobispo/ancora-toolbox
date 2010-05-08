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

package org.ancora.DTool;

import java.io.File;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();
       testTraceReader();
    }

   private static void testTraceReader() {
      String filename = "example-fdct.trace";
      TraceReader traceReader = TraceReader.createTraceReader(new File(filename));

      TraceLine line = traceReader.nextLine();
      
      //System.out.println("Cycles 1:"+traceReader.getCycles());
      //System.out.println("Inst 1:"+traceReader.getNumberInstructions());

      while(line != null) {
         //System.out.println("Address:"+line.getAddress());
         //System.out.println("Instruction:"+line.getInstruction());
         line = traceReader.nextLine();
      }

      //System.out.println("Cycles 2:"+traceReader.getCycles());
      //System.out.println("Inst 2:"+traceReader.getNumberInstructions());
   }

}
