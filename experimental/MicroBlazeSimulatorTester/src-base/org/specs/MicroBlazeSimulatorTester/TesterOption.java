/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.specs.MicroBlazeSimulatorTester;

import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValueType;

/**
 *
 * @author Joao Bispo
 */
public enum TesterOption implements AppOptionEnum {

   ElfFile(AppValueType.string),
   ExecuteUpToXInstructions(AppValueType.bool),
   StopInstruction(AppValueType.integer),
   StopAtAddressX(AppValueType.bool),
   StopAddress(AppValueType.integer),
   WriteTrace(AppValueType.bool),
   TraceFile(AppValueType.string),
   WriteLmbMemoryBeforeExecution(AppValueType.bool),
   LmbBeforeFile(AppValueType.string),
   WriteLmbMemoryAfterExecution(AppValueType.bool),
   LmbAfterFile(AppValueType.string),
   RegistersValuesAfterExecution(AppValueType.bool),
   FileForRegisters(AppValueType.string);
   

   private TesterOption(AppValueType type) {
      this.type = type;
   }



   public String getName() {
      return AppUtils.buildEnumName(this);
   }

   public AppValueType getType() {
      return type;
   }



   final private AppValueType type;

}
