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

package org.ancora.SharedLibrary.Concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Can only be created by a ConcurrentChannel object, and represents a consumer
 * end of that channel.
 * 
 * @author Joao Bispo
 */
public class ChannelConsumer<T> {

   ChannelConsumer(BlockingQueue<T> channel) {
      this.channel = channel;
   }

   /**
    * Retrieves and removes the head of this queue, or returns null if this queue is empty.
    *
    * @return the head of this queue, or null if this queue is empty
    */
   public T poll() {
      return channel.poll();
   }

   /**
    * Retrieves and removes the head of this queue,
    * waiting up to the specified wait time if necessary
    * for an element to become available.
    *
    * @param timeout how long to wait before giving up, in units of unit
    * @param unit a TimeUnit determining how to interpret the timeout parameter
    * @return the head of this queue, or null if the specified waiting time
    * elapses before an element is available
    * @throws InterruptedException if interrupted while waiting
    */
   public T poll(long timeout, TimeUnit unit) throws InterruptedException {
      return channel.poll(timeout, unit);
   }

   /**
    * Retrieves and removes the head of this queue, waiting if necessary until
    * an element becomes available.
    *
    * @return the head of this queue
    * @throws InterruptedException if interrupted while waiting
    */
   public T take() throws InterruptedException {
      return channel.take();
   }


   ///
   // INSTANCE VARIABLES
   ///
   private final BlockingQueue<T> channel;
}
