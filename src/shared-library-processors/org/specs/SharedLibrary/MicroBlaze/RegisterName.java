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

package org.specs.SharedLibrary.MicroBlaze;

import org.specs.SharedLibrary.Processors.RegisterId;


/**
 * Mapping between Fireworks 3 Stage Pipelined processor registers and the
 * register number used in the DTool simulator.
 *
 * @author Joao Bispo
 */
public enum RegisterName implements RegisterId {

   REG0(0),
   REG1(1),
   REG2(2),
   REG3(3),
   REG4(4),
   REG5(5),
   REG6(6),
   REG7(7),
   REG8(8),
   REG9(9),
   REG10(10),
   REG11(11),
   REG12(12),
   REG13(13),
   REG14(14),
   REG15(15),
   REG16(16),
   REG17(17),
   REG18(18),
   REG19(19),
   REG20(20),
   REG21(21),
   REG22(22),
   REG23(23),
   REG24(24),
   REG25(25),
   REG26(26),
   REG27(27),
   REG28(28),
   REG29(29),
   REG30(30),
   REG31(31),
   PC(32),
   MSR(33),
   EAR(34),
   ESR(35),
   FSR(36),
   BTR(37),
   PVR0(38),
   PVR1(39),
   PVR2(40),
   PVR3(41),
   PVR4(42),
   PVR5(43),
   PVR6(44),
   PVR7(45),
   PVR8(46),
   PVR9(47),
   PVR10(48),
   PVR11(49);

   public RegisterId getGeneralRegStart() {
      return REG0;
   }

   public RegisterId getGeneralRegEnd() {
      return REG31;
   }

   public RegisterId getPvrRegStart() {
      return PVR0;
   }

   public RegisterId getPvrRegEnd() {
      return PVR11;
   }

   /*
   public int getGeneralRegStart() {
      return REG0.registerNumber;
   }

   public int getGeneralRegEnd() {
      return REG31.registerNumber;
   }

   public int getPvrRegStart() {
      return PVR0.registerNumber;
   }

   public int getPvrRegEnd() {
      return PVR11.registerNumber;
   }
*/

   private RegisterName(int registerNumber) {
      this.registerNumber = registerNumber;
   }

   @Override
   public int getRegisterNumber() {
      return registerNumber;
   }


   @Override
   public String getRegisterName() {
      return this.name();
   }

   public static String getCarryFlagName() {
      return MSR.name() + "[29]";
   }

   private final int registerNumber;

}
