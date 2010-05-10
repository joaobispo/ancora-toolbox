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

package org.ancora.InstructionBlock;



/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class TestElfReader {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      //String systemConfigFile = "./systemconfig.xml";
      String systemConfigFile = "Configuration Files\\systemconfig.xml";
      String binaryFile = "aluno_adpcm_coder.elf";


     // BasicBlock bb = new MbBasicBlock();
      InstructionBusReader reader = ElfBusReader.createElfReader(systemConfigFile, binaryFile);



      GenericInstruction instruction = reader.nextInstruction();
      while (instruction != null) {
  //       InstructionBlock block = bb.acceptInstruction(instruction);
         //System.out.println(instruction);
 //        if (block != null) {
  //          System.out.println(block);
 //        }
         instruction = reader.nextInstruction();
      }

  //    InstructionBlock block = bb.lastInstruction();
     // System.out.println(block);

   }

}
