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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Wrapper for a bounded Blocking Queue, which can only be accessed by
 * ChannelProducer and ChannelConsumer objects.
 * 
 * @author Joao Bispo
 */
public class ConcurrentChannel<T> {

   /**
    * Creates a bounded blocking queue with the specified capacity.
    *
    * @param capacity the capacity of this Concurrent Channel.
    */
   public ConcurrentChannel(int capacity) {
      channel = new ArrayBlockingQueue<T>(capacity);
   }
   
   /**
    * Creates a ChannelProducer which is connected to this ConcurrentChannel.
    * @return a ChannelProducer
    */
   public ChannelProducer<T> createProducer() {
      return new ChannelProducer<T>(channel);
   }

   public ChannelConsumer<T> createConsumer() {
      return new ChannelConsumer<T>(channel);
   }

   ///
   // INSTANCE VARIABLES
   ///
   private final BlockingQueue<T> channel;
}
