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

package org.specs.SharedLibrary.MicroBlaze.ParsedInstruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;
import org.specs.SharedLibrary.MicroBlaze.ArgumentsProperties;
import org.specs.SharedLibrary.MicroBlaze.ArgumentsProperties.ArgumentProperty;
import org.specs.SharedLibrary.MicroBlaze.InstructionName;
import org.specs.SharedLibrary.MicroBlaze.ParsedInstruction.MbOperand.Type;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeParser {



   public MicroBlazeParser() {
      mbInstructions = new ArrayList<MbInstruction>();
   }



   /**
    * Feeds an instruction to this parser.
    *
    * @param address
    * @param instruction
    */
   public void nextInstruction(int address, String instruction) {
// Parse arguments
      String[] arguments = parseArguments(instruction);

      // Get arguments properties
      int separatorIndex = instruction.indexOf(" ");
      String instructionNameString = instruction.substring(0, separatorIndex);
      InstructionName instructionName = InstructionName.getEnum(instructionNameString);
      if(instructionName == null) {
         LoggingUtils.getLogger().
                 warning("Could not parse instruction name for instruction '"+instruction+"'");
         return;
      }

      ArgumentProperty[] argProps = ArgumentsProperties.getProperties(instructionName);

      // Check arguments properties have the same size as the arguments
      if(arguments.length != argProps.length) {
         LoggingUtils.getLogger(MicroBlazeParser.class.getName()).
                 warning("Number of arguments ("+arguments.length+") different from " +
                 "the number of properties ("+argProps.length+") for instruction '"+
                 instructionName+"'.");
         return;
      }

      // For each argument, return the correct operand
      List<MbOperand> operands = new ArrayList<MbOperand>();
      for (int i = 0; i < arguments.length; i++) {
         MbOperand mbOperand = parseMbArgument(arguments[i], argProps[i]);
         operands.add(mbOperand);
      }

      MbInstruction mbInst = new MbInstruction(address, instructionName, operands);
      mbInstructions.add(mbInst);

   }

   public static MbOperand parseMbArgument(String argument, ArgumentProperty argProp) {

      // Check type
      MbOperand.Type type = null;
      if(argument.startsWith(REGISTER_PREFIX)) {
         type = MbOperand.Type.register;
      } else {
         type = MbOperand.Type.immediate;
      }

      // Check flow
      MbOperand.Flow flow = null;
      if(argProp == ArgumentProperty.read) {
         flow = MbOperand.Flow.in;
      } else if(argProp == ArgumentProperty.write) {
         flow = MbOperand.Flow.out;
      } else {
         LoggingUtils.getLogger().
                 warning("Case not defined: '"+argProp+"'");
         return null;
      }

      // Get value
      Integer value = parseValue(type, argument);
      if(value == null) {
         return null;
      }


      return new MbOperand(flow, type, value);
   }

   private static Integer parseValue(Type type, String argument) {
      if (type == MbOperand.Type.register) {
         Integer value = null;
         try {
            String stringValue = argument.substring(REGISTER_PREFIX.length());
            value = Integer.parseInt(stringValue);
         } catch (NumberFormatException ex) {
            LoggingUtils.getLogger().
                    warning("Expecting an microblaze register (e.g., r3): '" + argument + "'.");
         }

         return value;
      }

      if (type == MbOperand.Type.immediate) {
         Integer value = null;
         try {
            value = Integer.parseInt(argument);
         } catch (NumberFormatException ex) {
            LoggingUtils.getLogger().
                    warning("Expecting an integer immediate: '" + argument + "'.");
         }
         return value;
      }

      LoggingUtils.getLogger().
              warning("Case not defined:"+type);
      return null;
   }

   /**
    * Indicates that the stream of instructions has ended.
    */
   public void close() {
      // Do nothing
   }

   /**
    * Transforms a list of strings containing MicroBlaze instructions into
    * a more convenient format.
    *
    * @param addresses
    * @param instructions
    * @return
    */
   public static List<MbInstruction> getMbInstructions(List<Integer> addresses,
           List<String> instructions) {

      MicroBlazeParser mbParser = new MicroBlazeParser();

      int numInstructions = instructions.size();
      List<MbInstruction> mbInstructions = new ArrayList<MbInstruction>();
      for (int i = 0; i < numInstructions; i++) {
         int address = addresses.get(i);
         String instruction = instructions.get(i);
         mbParser.nextInstruction(address, instruction);
         List<MbInstruction> mbInsts = mbParser.getAndClearUnits();
         if (mbInsts != null) {
            mbInstructions.addAll(mbInsts);
         }
      }
      mbParser.close();
      List<MbInstruction> mbInsts = mbParser.getAndClearUnits();
      if (mbInsts != null) {
         mbInstructions.addAll(mbInsts);
      }

      return mbInstructions;
   }

   /**
    *
    * @return a list of the MbInstructions found. After returning a non-null
    * list, it is cleared from the parser. Returns null if there are no
    * MbInstructions at the moment.
    */
   public List<MbInstruction> getAndClearUnits() {
      List<MbInstruction> returnList = mbInstructions;
      mbInstructions = new ArrayList<MbInstruction>();
      return returnList;
   }

   /**
    * INSTANCE VARIABLES
    * @param instructions
    * @return
    */
   List<MbInstruction> mbInstructions;
/*
   public static List<Operation> parseMbInstructions(List<GenericInstruction> instructions) {
      List<Operation> operations = new ArrayList(instructions.size());
      
      for(GenericInstruction instruction : instructions) {
         Operation op = parseMbInstruction(instruction);
         if(op != null) {
            operations.add(op);
         }
      }

      return operations;
   }
*/
   /*
   public static Operation parseMbInstruction(GenericInstruction instruction) {
      // Parse arguments
      String[] arguments = parseArguments(instruction.getInstruction());

      // Get arguments properties
      InstructionName instructionName = MbBlockUtils.getInstructionName(instruction);
      ArgumentProperty[] argProps = ArgumentsProperties.getProperties(instructionName);

      // Check arguments properties have the same size as the arguments
      if(arguments.length != argProps.length) {
         Logger.getLogger(MicroBlazeParser.class.getName()).
                 warning("Number of arguments ("+arguments.length+") different from " +
                 "the number of properties ("+argProps.length+") for instruction '"+
                 instructionName+"'. Returning null.");
         return null;
      }

      // For each argument, return the correct operand
      Operand[] operands = new Operand[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         //System.out.println("Arg:" + arguments[i]);
         //System.out.println("Prop:" + argProp[i]);
         operands[i] = parseMbArgument(arguments[i]);
      }

      // Build Input and Output Lists
      List<Operand> inputs = new ArrayList<Operand>();
      List<Operand> outputs = new ArrayList<Operand>();

      for(int i=0; i< argProps.length; i++) {
         if(argProps[i] == ArgumentProperty.read) {
            inputs.add(operands[i]);
         }

         if(argProps[i] == ArgumentProperty.write) {
            outputs.add(operands[i]);
         }
      }

      return new MbOperation(instruction.getAddress(), instructionName, inputs, outputs);
   }
   */

   /**
    * Simple, initial parsing of the arguments of the MicroBlaze instruction string.
    *
    * @param instruction
    * @return
    */
   public static String[] parseArguments(String instruction) {
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(instruction);
      String registersString = instruction.substring(whiteSpaceIndex).trim();

      String[] regs = registersString.split(",");
      for(int i=0; i<regs.length; i++) {
         regs[i] = regs[i].trim();
      }

      return regs;
   }

   /*
   public static Operand parseMbArgument(String argument) {
       // Check if register
      if(argument.startsWith(REGISTER_PREFIX)) {
         try {
         String stringValue = argument.substring(REGISTER_PREFIX.length());
         int value = Integer.parseInt(stringValue);
         return new MbRegister(argument, value);
         //return new MbOperand(Type.register, value, MbDefinitions.BITS_REGISTER);
         } catch(NumberFormatException ex) {
         Logger.getLogger(MicroBlazeParser.class.getName()).
                 warning("Expecting an microblaze register (e.g., R3): '" + argument + "'.");
      }
      }

      // Check if integer immediate
      try {
         int value = Integer.parseInt(argument);
         return new MbImm(value);
         //return new MbOperand(Type.immediate, value, MbDefinitions.BITS_IMMEDIATE);
      } catch(NumberFormatException ex) {
         Logger.getLogger(MicroBlazeParser.class.getName()).
                 warning("Expecting an integer immediate: '" + argument + "'.");
      }

      return null;
   }
*/
   /**
    * Transforms a block of MicroBlaze Instructions into a list of IR Operations.
    * 
    * @param block
    * @return
    */
   /*
   public static List<Operation> mbToOperations(InstructionBlock block) {
      // Transform block in List of operations
      List<Operation> operations = MicroBlazeParser.parseMbInstructions(block.getInstructions());

      // Transform operations in pure IR operations
      for(Transformation transf : microblazeTransformations) {
         transf.transform(operations);
         //operations = transf.transform(operations);
         // Update live-outs
      }

      if(!isPureIr(operations)) {
         return null;
      }

      return operations;
   }
*/

   public static final String REGISTER_PREFIX = "r";
}
