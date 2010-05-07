/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.SharedLibrary.DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * "Pushing Queue" of fixed size.
 *
 * <p>Elements can only be added at the head of the queue.
 * Everytime an element is added, every other elements gets "pushed" (its index
 * increments by one).
 * If an element is added when the queue is full, the last element in the queue
 * gets dropped.
 *
 * @author Joao Bispo
 */
public class PushingQueue<T> {

   /**
    * Creates a PushingQueue with the specified size.
    *
    * @param capacity the size of the queue
    */
   public PushingQueue(int capacity) {
      size = capacity;
      queue = new ArrayList<T>(capacity);
      mostRecentItem = capacity-1;
      this.capacity = capacity;
   }

   /**
    * Inserts an element at the head of the queue, pushing all other elements
    * one position forward. If the queue is full, the last element is dropped.
    *
    * @param element an element to insert in the queue
    */
   public void insertElement(T element) {
      //Get index
      mostRecentItem = advancePointer(mostRecentItem);

      //Check if list already has an element
      if(queue.size() == mostRecentItem ) {
         queue.add(element);
      } else {
         queue.set(mostRecentItem, element);
      }
   }

   /**
    * Returns the element at the specified position in this queue.
    *
    * @param index index of the element to return
    * @return the element at the specified position in this queue
    */
   public T getElement(int index) {
      // Normalize index to size of queue
      index = index % size;

      // Translate wanted index to queue index
      int queueIndex = mostRecentItem - index;
      // If negative, add size
      if(queueIndex < 0) {
         queueIndex += size;
      }

      // Check if element already exists in queue
      if(queueIndex < queue.size()) {
         return queue.get(queueIndex);
      } else {
         return null;
      }
   }

   /**
    * Returns the number of elements in this list.
    *
    * @return the number of elements in this list
    */
   public int size() {
      return size;
   }

   /**
    * @return the maximum number of elements this queue supports
    */
   public int capacity() {
      return capacity;
   }

   /**
    * Move the pointer of the queue by one place.
    *
    * @param oldPointer
    * @return
    */
   private int advancePointer(int oldPointer) {
      int pointer = oldPointer+1;
      if(pointer < size) {
         return pointer;
      } else {
         return 0;
      }
   }

   ///
   // INSTANCE VARIABLES
   ///
   private List<T> queue;
   private int mostRecentItem;
   private int size;
   private final int capacity;
}
