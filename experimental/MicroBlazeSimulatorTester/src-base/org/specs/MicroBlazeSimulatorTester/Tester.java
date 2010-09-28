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
import org.specs.DToolPlus.Support.ElfBusReader;
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

      TraceFile traceFile = TraceFile.newTraceFile(options);

     

      // First step
      busReader.step();
      String instruction;
      long counter = 1l;

      int modulo = 10000;
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
             dumpInstructionMemoryAfterError(options, busReader);
             break;
         }

         busReader.step();
      }

      // Close trace file
      if (traceFile != null) {
         traceFile.close();
      }

      return 0;
   }

   public Class getAppOptionEnum() {
      return TesterOption.class;
   }

   private void dumpInstructionMemoryBeforeExecution(Map<String, AppValue> options, ElfBusReader busReader) {
      // Check if we want to dump the instruction memory
      boolean dumpInstructionMemory = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.DumpInstructionMemory));
      if(!dumpInstructionMemory) {
         return;
      }

      // Get output file
      String filename = AppUtils.getString(options, TesterOption.DumpInstructionMemoryFile);
      File file = new File(filename);

      try {
         IoUtils.write(file, busReader.dumpInstructionMemory());
      } catch (MemoryException ex) {
         LoggingUtils.getLogger().
                 warning("Memory Exception while dumping instruction memory: "+ex.getMessage());
      }
   }

   private void dumpInstructionMemoryAfterError(Map<String, AppValue> options, ElfBusReader busReader) {
      // Check if we want to dump the instruction memory
      boolean dumpInstructionMemory = Boolean.parseBoolean(AppUtils.getString(options, TesterOption.DumpInstructionMemoryAfterError));
      if(!dumpInstructionMemory) {
         return;
      }

      // Get output file
      String filename = AppUtils.getString(options, TesterOption.DumpInstructionMemoryAfterErrorFile);
      File file = new File(filename);

      try {
         IoUtils.write(file, busReader.dumpInstructionMemory());
      } catch (MemoryException ex) {
         LoggingUtils.getLogger().
                 warning("Memory Exception while dumping instruction memory: "+ex.getMessage());
      }
   }

}
