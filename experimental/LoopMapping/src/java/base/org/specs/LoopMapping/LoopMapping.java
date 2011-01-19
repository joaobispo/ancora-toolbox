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

package org.specs.LoopMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.ancora.FuMatrix.Mapper.GeneralMapper;
import org.ancora.FuMatrix.Mapper.NaiveMapper;
import org.ancora.InstructionBlock.GenericInstruction;
import org.ancora.InstructionBlock.InstructionBlock;
import org.ancora.IntermediateRepresentation.MbParser;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.IntermediateRepresentation.OperationType;
import org.ancora.IntermediateRepresentation.Operations.Nop;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.PreBuiltTypes.InputType;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.EnumUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;
import org.ancora.SharedLibrary.Utilities.LineReader;
import org.ancora.StreamTransform.RemoveR0Or;
import org.ancora.StreamTransform.ResolveLiteralInputs;
import org.ancora.StreamTransform.SingleStaticAssignment;
import org.ancora.StreamTransform.StreamTransformation;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DymaLib.Assembly.CodeSegment;
import org.specs.DymaLib.deprecated.LowLevelInstruction.Elements.LowLevelInstruction;
import org.specs.DymaLib.deprecated.LowLevelInstruction.LowLevelParser;
import org.specs.DymaLib.Mapping.Architecture.Architecture;
import org.specs.DymaLib.Mapping.Mapper;
import org.specs.DymaLib.MicroBlaze.MbImplementation;
import org.specs.DymaLib.ProcessorImplementation;
import org.specs.DymaLib.Utils.LoopDiskWriter.BlockParser;
import system.SysteM;
import system.SysteMException;

/**
 *
 * @author João Bispo
 */
public class LoopMapping implements App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       ProcessUtils.programStandardInit();

        LoopMapping program = new LoopMapping();
        SimpleGui gui = new SimpleGui(program);
        gui.setTitle("Loop Mapping v0.1");
        gui.execute();
    }

   public int execute(Map<String, AppValue> options) throws InterruptedException {
      boolean success = init(options);
      if (!success) {
         return -1;
      }

      // Open block files
      for(File inputFile : inputFiles) {
         processFile(inputFile);
         //processFileOldMapper(inputFile);
         //processElfOldMapper(inputFile);
      }
/*
       File elfFile = new File("E:\\temp\\elf\\bs-Os.elf");
      SystemSetup setup = SystemSetup.getDefaultConfig();

      //TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);

      //String systemConfig = "./Configuration Files/systemconfig.xml";
      String systemConfig = "D:\\Programming\\Ancora\\AncoraToolbox\\experimental\\LoopDetection\\run\\Configuration Files\\systemconfig.xml";
      String elfFilename = elfFile.getPath();
      try {
         //SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, false);
         //SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, setup);
         SysteM system = new SysteM(systemConfig, elfFilename, false, true);
      } catch (SysteMException ex) {
         System.out.println("Exception!");
         System.out.println(ex.getMessage());
         System.out.println(Arrays.toString(ex.getStackTrace()));
      }
      System.out.println("No exception?");
*/
      return 0;
   }

   public Class getAppOptionEnum() {
      return MappingOptions.class;
   }

   private boolean init(Map<String, AppValue> options) {
      inputFiles = getFiles(options);
      if(inputFiles == null) {
         return false;
      }

      outputFolder = AppUtils.getFolder(options, MappingOptions.OutputFolder);
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open folder.");
         return false;
      }

      return true;
   }

   /**
    * Check if input is is single file or folder
    * @param options
    * @return
    */
   private List<File> getFiles(Map<String, AppValue> options) {
      String inputTypeName = AppUtils.getString(options, MappingOptions.TypeOfInput);
      InputType inputType = EnumUtils.valueOf(InputType.class, inputTypeName);

      return InputType.getFiles(options, MappingOptions.InputBlockFile, inputType);
   }


   private void processFile(File inputFile) {
      BlockParser blockParser = BlockParser.newBlockParser(inputFile);
      ProcessorImplementation impl = new MbImplementation();
      LowLevelParser lowlevelParser = impl.getLowLevelParser();

      String line = null;
      while((line = blockParser.nextInstruction()) != null) {
         lowlevelParser.nextInstruction(blockParser.getCurrentAddress(), line);
      }
      lowlevelParser.close();

      processInstructions(lowlevelParser.getAndClearUnits());

   }

   private void processFileOldMapper(File inputFile) {
      BlockParser blockParser = BlockParser.newBlockParser(inputFile);
      String line = null;
      // Transform instructions in GenericInstructions
      List<GenericInstruction> genericInstructions = new ArrayList<GenericInstruction>();
      while((line = blockParser.nextInstruction()) != null) {
         genericInstructions.add(new GenericInstruction(blockParser.getCurrentAddress(), line));
         //lowlevelParser.nextInstruction(blockParser.getCurrentAddress(), line);
      }
      InstructionBlock block = new InstructionBlock(genericInstructions, 1, 1, genericInstructions.size());

      List<Operation> operations = MbParser.mbToOperations(block);
      // Put in SSA
      SingleStaticAssignment.transform(operations);

      // Transform operations
      transform(operations);

      // Use mapper
      /*
      GeneralMapper mapper = new NaiveMapper();
      boolean sucess = true;
      for (Operation operation : operations) {
         sucess = mapper.accept(operation);
         if (!sucess) {
            break;
         }
      }

      if(!sucess) {
         System.out.println("Could not map");
      } else {
         System.out.println("Mapping was successful");
         System.out.println(mapper.getMappedOps());
      }
       *
       */
   }

   private void processInstructions(List<LowLevelInstruction> lowlevelInstructions) {
      Architecture arch = Architecture.xPesYMemory(16, 1);
      int windowSize = 10;
      Mapper mapper = new Mapper(arch, windowSize);

      for(LowLevelInstruction inst : lowlevelInstructions) {
         mapper.nextInstruction(inst);
         //System.out.println(inst);
      }
      mapper.close();
      System.out.println("---------------");
      System.out.println("Total: "+lowlevelInstructions.size()+" instructions.");
   }

   private static void transform(List<Operation> operations) {
      List<StreamTransformation> transf = new ArrayList<StreamTransformation>();

      transf.add(new RemoveR0Or());
      transf.add(new ResolveLiteralInputs());

      for (StreamTransformation t : transf) {
         for (int i = 0; i < operations.size(); i++) {
            operations.set(i, t.transform(operations.get(i)));
         }
      }

   }

   /*
   private void processElfOldMapper(File inputFile) {
      throw new UnsupportedOperationException("Not yet implemented");
   }
    *
    */

   public static long removedInst(CodeSegment unit) {

      List<GenericInstruction> genericInstructions = new ArrayList<GenericInstruction>();
      for(int i=0; i<unit.getInstructions().size(); i++) {
         genericInstructions.add(new GenericInstruction(
                 unit.getAddresses().get(i), unit.getInstructions().get(i)));
      }

      InstructionBlock block = new InstructionBlock(genericInstructions, unit.getIterations(),
              unit.getId(), unit.getTotalInstructions());

      List<Operation> operations = MbParser.mbToOperations(block);
      // Put in SSA
      SingleStaticAssignment.transform(operations);

      // Transform operations
      transform(operations);

      // Count the number of nops
      long removedInst = 0l;
      for(Operation operation : operations) {
         if(operation.getType() == OperationType.Nop) {
            removedInst++;
         }
      }


      // Removed inst multiplied by the number of iterations
      return removedInst * unit.getIterations();
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<File> inputFiles;
   private File outputFolder;










}
