/*
 *  Copyright 2010 Ancora Research Group.
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

package org.ancora.SharedLibrary.Utilities;

import java.util.List;
import java.util.logging.Logger;

/**
 * Shows information about progress of elements in a List.
 *
 * @author Joao Bispo
 */
public class ProgressCounter<K>  {

   //public JobProgress(String jobFilename, int numJobs) {
   public ProgressCounter(List<K> list) {
      //this.jobFilename = jobFilename;
      this.elements = list;
      counter = -1;
      progressSeparator = DEFAULT_PROGRESS_SEPARATOR;
   }

   /**
    * Write a message to standard output. Can use the following strings to indicate
    * numbers:
    *
    * <p>NUM_ELEMENTS: total number of elements;
    * <br>CURRENT_ELEMENT: current element number;
    * <br>PROGRESS: "X of TOTAL";
    *
    * @param message
    */
   public void message(String message) {
      String newMessage = parseMessage(message);
      // Replace number of elements
      logger.info(newMessage);
   }

   private String parseMessage(String message) {
      String newMessage;
      newMessage = message.replace(NUM_ELEMENTS, Integer.toString(elements.size()));
      newMessage = message.replace(CURRENT_ELEMENT, Integer.toString(counter));
      newMessage = message.replace(PROGRESS, getProgress(progressSeparator));

      return newMessage;
   }

   public K nextElement() {
      // Initialize counter, if not yet.
      if(counter == -1) {
         counter = 0;
      }

      if (counter >= elements.size()) {
         return null;
      }

      K k = elements.get(counter);
      counter++;
      
      return k;
   }


   public String getProgress(String separator) {
      StringBuilder builder = new StringBuilder();

      builder.append(counter);
      builder.append(progressSeparator);
      builder.append(elements.size());

      return builder.toString();
   }

   public void setProgressSeparator(String progressSeparator) {
      this.progressSeparator = progressSeparator;
   }

   public List<K> getElements() {
      return elements;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   //private String jobFilename;
   private List<K> elements;
   private int counter;
   private String progressSeparator;
   private final static Logger logger = Logger.getLogger(ProgressCounter.class.getName());
   

   public final static String DEFAULT_PROGRESS_SEPARATOR = " of ";

   public final static String NUM_ELEMENTS = "&elements";
   public final static String CURRENT_ELEMENT = "&current_element";
   public final static String PROGRESS = "&progress";




}
