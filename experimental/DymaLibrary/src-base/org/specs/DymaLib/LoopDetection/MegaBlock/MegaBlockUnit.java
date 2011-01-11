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

package org.specs.DymaLib.LoopDetection.MegaBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.DataStructures.CodeSegment;
import org.specs.DymaLib.LoopDetection.LoopUtils;
import org.specs.DymaLib.TraceUnit.TraceUnit;
import org.specs.DymaLib.TraceUnit.TraceUnitUtils;
import org.suikasoft.SharedLibrary.Processors.RegisterId;
import org.suikasoft.SharedLibrary.Processors.RegisterTable;

/**
 * Represents a Loop. Can also represent a sequence of instructions.
 *
 * @author Joao Bispo
 */
public class MegaBlockUnit implements CodeSegment {

   /**
    * Builds a new LoopUnit of the type Sequence.
    * 
    * @param storeInstuctions
    */
   public MegaBlockUnit(boolean storeInstuctions) {
      sequencialStoreInstructions = storeInstuctions;
      seqInstructions = 0;
      patternSize = 0;
      iterations = 1;
      type = Type.Sequence;
      instructions = new ArrayList<TraceUnit>();
      instructions.add(new TraceUnit(new ArrayList<String>(), new ArrayList<Integer>(), 0));
      verification = null;
      id = null;
      registerValues = null;
   }

   /**
    * Builds a new LoopUnit of the type Loop
    * 
    * @param patternSize
    */
   public MegaBlockUnit(int patternSize) {
      sequencialStoreInstructions = true;
      seqInstructions = -1;
      this.patternSize = patternSize;
      iterations = 0;
      type = Type.Loop;
      instructions = new ArrayList<TraceUnit>();
      verification = new ArrayList<TraceUnit>();
      id = null;
   }

   /*
   public static LoopUnit newLoopUnit(List<TraceUnit> units, int id, Type type,
           int iterations) {
      LoopUnit loopUnit = new LoopUnit(-1);
   }
    * 
    */

   public void addTraceUnit(TraceUnit traceUnit) {
      switch(type) {
         case Sequence:
            addTraceUnitSequence(traceUnit);
            break;
         case Loop:
            addTraceUnitLoop(traceUnit);
            break;
         default:
            LoggingUtils.getLogger().
                    warning("Case not implemented: '"+type+"'");
            break;
      }
   }


   private void addTraceUnitSequence(TraceUnit traceUnit) {
      id = getNewId(traceUnit);
      seqInstructions += traceUnit.getInstructions().size();

      if(sequencialStoreInstructions) {
         instructions.add(traceUnit);
         return;
      }

      int currentIndex = 0;

      TraceUnit seqTraceUnit = instructions.get(0);

      // If this is the first TraceUnit, add first instruction
      if(seqTraceUnit.getInstructions().isEmpty()) {
         List<String> inst = new ArrayList<String>();
         List<Integer> addresses = new ArrayList<Integer>();

         inst.add(traceUnit.getInstructions().get(0));
         addresses.add(traceUnit.getAddresses().get(0));

         instructions.set(0, new TraceUnit(inst, addresses, id));
         currentIndex++;
      }

      // Check if there are more instructions to check
      if(currentIndex >= traceUnit.getInstructions().size()) {
         return;
      }

       // Set last instruction
       seqTraceUnit = instructions.get(0);
       List<String> inst = seqTraceUnit.getInstructions();
       List<Integer> addresses = seqTraceUnit.getAddresses();

       int lastIndex = traceUnit.getInstructions().size() - 1 ;
      String lastInst = traceUnit.getInstructions().get(lastIndex);
      int lastAddr = traceUnit.getAddresses().get(lastIndex);

      if(inst.size() == 1) {
         inst.add(lastInst);
         addresses.add(lastAddr);
      } else {
         inst.set(1,lastInst);
         addresses.set(1, lastAddr);
      }

      instructions.set(0, new TraceUnit(inst, addresses, id));
   }

   /**
    * If there is no block, return traceUnit id. Else, build id.
    *
    * @param traceUnit
    * @return
    */
   private int getNewId(TraceUnit traceUnit) {
      if(id == null) {
         return traceUnit.getIdentifier();
      }

      //TraceUnit seqTraceUnit = instructions.get(0);
      //return BitUtils.superFastHash(traceUnit.getIdentifier(), seqTraceUnit.getIdentifier());
      return BitUtils.superFastHash(traceUnit.getIdentifier(), id);
   }


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      List<String> inst = TraceUnitUtils.extractInstructionsWithAddresses(instructions);

      builder.append("iterations:");
      builder.append(iterations);
      builder.append("\n");


      builder.append("sequence instructions: ");
      builder.append(getNumInstructions());
      builder.append("\n");
      builder.append("instructions x iterations: ");
      builder.append(getNumInstructions() * iterations);
      builder.append("\n");
      for(String instruction : inst) {
         builder.append(instruction);
         builder.append("\n");
      }
     

      return builder.toString();
   }


   public int getNumInstructions() {
      List<String> inst = TraceUnitUtils.extractInstructionsWithAddresses(instructions);

      if (type == Type.Sequence) {
         //Check
         if (sequencialStoreInstructions) {
            if(inst.size() != seqInstructions) {
               LoggingUtils.getLogger().
                       warning("Difference: inst size ("+inst.size()+") and "
                       + "seqInstructions ("+seqInstructions+").");
            }
            //return inst.size();
         }
         /*
         else {
            return seqInstructions;
         }
          *
          */
         return seqInstructions;
      }

      if(type == Type.Loop) {
         return inst.size();
      }

      LoggingUtils.getLogger().
              warning("Not yet implemented.");
      return 0;
   }

   /**
    * The number of instructions in the Unit, multiplied by the number of
    * iterations.
    * 
    * @return
    */
   public int getTotalInstructions() {
      int numInst = getNumInstructions();
      return numInst * iterations;
   }

   /**
    * If loop is being formed, add to instructions.
    *
    * If instructions size is equal to pattern size, we are verifying
    *
    * @param traceUnit
    */
   private void addTraceUnitLoop(TraceUnit traceUnit) {
      // While iterations equals zero, add units to instructions
      if(iterations == 0) {
         instructions.add(traceUnit);
         id = getNewId(traceUnit);
         if(instructions.size() == patternSize) {
            iterations++;
         }

         // Verification
         if(instructions.size() > patternSize) {
            LoggingUtils.getLogger().
                    warning("Bug: MegaBlock is bigger than pattern size");
         }

         return;
      }

      // Verifying phase
      verification.add(traceUnit);
      if(verification.size() == patternSize) {
         iterations++;
         LoopUtils.verifyLists(instructions, verification);
         verification = new ArrayList<TraceUnit>();
      }

      // Verification
      if (instructions.size() > patternSize) {
         LoggingUtils.getLogger().
                 warning("Bug: Verification array is bigger than pattern size");
      }

   }

   /**
    * Can only be applied to Loop type. When we want to transform a Loop type into
    * a Sequential type and get the Loop built until then. Returns a LoopUnit of
    * the type Loop, and the this LoopUnit transforms into a Sequential type.
    *
    * @return
    */
   public MegaBlockUnit splitUnit(boolean storeInstructions) {
      if(type != Type.Loop) {
         LoggingUtils.getLogger().
                 warning("This method can only be applied to Loop types.");
         return null;
      }

      // Recover current loop
      MegaBlockUnit loop = getCurrentLoop();

      // Transform loop into sequential
      transformLoopToSeq(storeInstructions);

      return loop;
   }

   public List<String> getInstructions() {
      return TraceUnitUtils.extractInstructions(instructions);
   }

   public List<Integer> getAddresses() {
      return TraceUnitUtils.extractAddresses(instructions);
   }

   public int getId() {
      return id;
   }

   public int getIterations() {
      return iterations;
   }

   
   public Type getType() {
      return type;
   }

   public boolean isSequenceInstructionsStored() {
      return sequencialStoreInstructions;
   }


   public boolean isLoop() {
      if(type == MegaBlockUnit.Type.Loop) {
         return true;
      }

      return false;
   }

   public boolean areAllInstructionsStored() {
      return sequencialStoreInstructions;
   }

   

   private MegaBlockUnit getCurrentLoop() {
      if(iterations == 0) {
         return null;
      }

      MegaBlockUnit loop = new MegaBlockUnit(patternSize);
      loop.id = id;
      loop.iterations = iterations;
      loop.instructions = instructions;

      return loop;
   }


   private void transformLoopToSeq(boolean storeInstructions) {
      // Determine instructions
      List<TraceUnit> inst;
      if(iterations == 0) {
         inst = instructions;
      } else {
         inst = verification;
      }
      
      // Initialize
      sequencialStoreInstructions = storeInstructions;
      seqInstructions = 0;
      patternSize = 0;
      iterations = 1;
      type = Type.Sequence;
      instructions = new ArrayList<TraceUnit>();
      instructions.add(new TraceUnit(new ArrayList<String>(), new ArrayList<Integer>(), 0));
      verification = null;
      id = null;
      
      // Feed trace units
      for(TraceUnit unit : inst) {
         addTraceUnit(unit);
      }
   }

//   public Map<RegisterId, Integer> getRegisterValues() {
   public RegisterTable getRegisterValues() {
      return registerValues;
   }

   //public void setRegisterValues(Map<RegisterId, Integer> registerValues) {
   public void setRegisterValues(RegisterTable registerValues) {
      this.registerValues = registerValues;
   }

   private boolean sequencialStoreInstructions;
   private int seqInstructions;
   private int patternSize;
   private int iterations;
   private Type type;
   private List<TraceUnit> instructions;
   private List<TraceUnit> verification;
   private Integer id;
   //private Map<RegisterId, Integer> registerValues;
   private RegisterTable registerValues;

   private static final long serialVersionUID = 1;






   public enum Type {
      Loop,
      Sequence;
   }
}
