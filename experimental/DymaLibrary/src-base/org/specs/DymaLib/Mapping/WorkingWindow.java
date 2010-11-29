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

package org.specs.DymaLib.Mapping;

import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.DymaLib.Mapping.Tables.WorkingCycle;

/**
 *
 * @author Joao Bispo
 */
public class WorkingWindow {

   public WorkingWindow(int windowsSize) {
      if(windowsSize < 2) {
         windowsSize = 2;
         LoggingUtils.getLogger().
                 warning("Windows size ('"+windowsSize+"') needs to be greater than 1. "
                 + "Setting it to 2");
      }
      //this.windowSize = windowsSize;
      backPointer = null;
      frontPointer = null;
//      currentCycles = new ArrayList<WorkingCycle>();
      currentElements = new WorkingCycle[windowsSize];

   }


   public static int moduleNext(Integer value, int size) {
      // empty
      if(value == null) {
         return 0;
      }

      int evalPointer = value + 1;
      if(evalPointer == size) {
         return 0;
      }
      else {
         return evalPointer;
      }
   }

   /**
    *
    * @return the value of the frontPointer, if it moved one unit forward.
    */
   /*
   private int evalNextFront() {
      // empty
      if(frontPointer == null) {
         return 0;
      }

      int evalFrontPointer = frontPointer + 1;
      if(evalFrontPointer == currentElements.length) {
         return 0;
      }
      else {
         return evalFrontPointer;
      }
   }
    *
    */

   /**
    *
    * @return true if the window is full.
    */
   public boolean isFull() {
      if(frontPointer == null) {
         return false;
      }

      if(moduleNext(frontPointer, currentElements.length) == backPointer) {
         return true;
      }
      else {
         return false;
      }
   }

   public boolean isEmpty() {
      if (frontPointer == null) {
         return true;
      } else {
         return false;
      }

   }

   /**
    * 
    * @return null if empty
    */
   public WorkingCycle popOldestElement() {
      if(isEmpty()) {
      //if(backPointer == null) {
         return null;
      }

      WorkingCycle returnValue = currentElements[backPointer];
      // Check if last element
      if(backPointer == frontPointer) {
         backPointer = null;
         frontPointer = null;
      } else {
         backPointer = moduleNext(backPointer, currentElements.length);
      }

      return returnValue;
   }

   public boolean pushNewerElement(WorkingCycle element) {
      if(isFull()) {
         LoggingUtils.getLogger().
                 warning("Window is full.");
         return false;
      }



      if (isEmpty()) {
         backPointer = 0;
         frontPointer = 0;
      }
      else {
         // Get next front pointer
         frontPointer = moduleNext(frontPointer, currentElements.length);
      }
      
      currentElements[frontPointer] = element;

      return true;
   }


   /**
    * INSTANCE VARIABLES
    */
   //private int windowSize;
   private Integer frontPointer;
   private Integer backPointer;
   private WorkingCycle[] currentElements;

   @Override
   public String toString() {
      if(isEmpty()) {
         return "Empty Window";
      }

      StringBuilder builder = new StringBuilder();
      int tempBackPointer = backPointer;
      while(tempBackPointer != frontPointer) {
         builder.append(currentElements[tempBackPointer]);
         builder.append("\n");
         tempBackPointer = moduleNext(tempBackPointer, currentElements.length);
      }
      builder.append(currentElements[tempBackPointer]);
      builder.append("\n");

      /*
      for(int i=backPointer; i<=frontPointer; i++) {
         builder.append(currentElements[i]);
         builder.append("\n");
      }
      */
      return builder.toString();
   }


}
