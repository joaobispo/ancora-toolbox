/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.specs.MicroBlazeSimulatorTester;

import java.io.File;
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DToolPlus.Utils.Dumper;
import org.specs.DToolPlus.Utils.ElfBusReader;
import system.memory.MemoryException;

/**
 *
 * @author Joao Bispo
 */
public class Tester implements App {

   public int execute(Map<String, AppValue> options) {
      // Get ELF
      String elfFilename = AppUtils.getString(options, TesterOption.ElfFile);
      File elfFile = new File(elfFilename);

      String systemConfig = "./Configuration Files/systemconfig.xml";
      ElfBusReader busReader = ElfBusReader.createElfReader(systemConfig, elfFile.getPath());


      dumpInstructionMemoryBeforeExecution(options, busReader);
      BufferedWriter traceFile = BufferedWriter.newTraceFile(options);

      boolean stopAtInstructionNumberX = AppUtils.getBool(options, TesterOption.ExecuteUpToXInstructions);
      Integer stopInstruction = null;
      if(stopAtInstructionNumberX) {
         stopInstruction = AppUtils.getInteger(options, TesterOption.StopInstruction);
      }

      boolean stopAtAddressX = AppUtils.getBool(options, TesterOption.StopAtAddressX);
      Integer stopAddress = null;
      if(stopAtAddressX) {
         stopAddress = AppUtils.getInteger(options, TesterOption.StopAddress);
      }

      // First step
      busReader.step();
      String instruction;
      long counter = 1l;

      int modulo = 1000000;
      int currentValue = 1;
      while((instruction=busReader.getInstruction()) != null) {
         Integer pc = busReader.getPc();
         
         // Write trace, if so
         if(traceFile != null) {
            traceFile.writeInstruction(pc, instruction);
         }
         
         if(currentValue == modulo) {
            System.out.println("Executed "+counter+" instructions.");
            currentValue = 0;
         }
         currentValue++;
         counter++;

         if(busReader.foundEmptyInstruction()) {
             break;
         }

         // Check if stop cycle
         if(stopAtInstructionNumberX) {
            if(counter > stopInstruction) {
               break;
            }
         }

         // Check if stop address
         if(stopAtAddressX) {
            if(pc == stopAddress) {
               break;
            }
         }

         busReader.step();
      }

      // Close trace file
      if (traceFile != null) {
         traceFile.close();
      }

      dumpInstructionMemoryAfterError(options, busReader);

      dumpRegisters(busReader, options);

      return 0;
   }

   public Class getAppOptionEnum() {
      return TesterOption.class;
   }

   private void dumpInstructionMemoryBeforeExecution(Map<String, AppValue> options, ElfBusReader busReader) {
      // Check if we want to dump the instruction memory
      boolean dumpInstructionMemory = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.WriteLmbMemoryBeforeExecution));
      if(!dumpInstructionMemory) {
         return;
      }

      // Get output file
      String filename = AppUtils.getString(options, TesterOption.LmbBeforeFile);
      File file = new File(filename);

      try {
         //IoUtils.write(file, busReader.dumpInstructionMemory());
         IoUtils.write(file, Dumper.dumpLmbMemory(busReader.getSystem()));
      } catch (MemoryException ex) {
         LoggingUtils.getLogger().
                 warning("Memory Exception while dumping instruction memory: "+ex.getMessage());
      }
   }

   private void dumpInstructionMemoryAfterError(Map<String, AppValue> options, ElfBusReader busReader) {
      // Check if we want to dump the instruction memory
      boolean dumpInstructionMemory = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.WriteLmbMemoryAfterExecution));
      if(!dumpInstructionMemory) {
         return;
      }

      // Get output file
      String filename = AppUtils.getString(options, TesterOption.LmbAfterFile);
      File file = new File(filename);

      try {
         //IoUtils.write(file, busReader.dumpInstructionMemory());
         IoUtils.write(file, Dumper.dumpLmbMemory(busReader.getSystem()));
      } catch (MemoryException ex) {
         LoggingUtils.getLogger().
                 warning("Memory Exception while dumping instruction memory: "+ex.getMessage());
      }
   }

   private void dumpRegisters(ElfBusReader busReader, Map<String, AppValue> options) {
      boolean dumpRegisters = AppUtils.getBool(options, TesterOption.RegistersValuesAfterExecution);
      if(!dumpRegisters) {
         return;
      }

      String filename = AppUtils.getString(options, TesterOption.FileForRegisters);
      File file = new File(filename);

      /*
      StringBuilder builder = new StringBuilder();
      CPU cpu = busReader.getCpu();
      for(RegisterId regId : cpu.getRegisterIds()) {
         builder.append(regId.getRegisterName());
         builder.append(" = ");
         builder.append(cpu.getRegister(regId.getRegisterNumber()));
         builder.append("\n");
      }
*/

      //IoUtils.write(file, builder.toString());
      IoUtils.write(file, Dumper.dumpRegisters(busReader.getCpu()));
   }

}
