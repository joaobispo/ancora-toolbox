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

package org.ancora.SharedLibrary.Logging;


import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;

/**
 * Logger handler for a JTextArea.
 *
 * @author Joao Bispo
 */
public class JTextAreaHandler extends StreamHandler {

   public JTextAreaHandler(JTextArea jTextArea) {
      this.jTextArea = jTextArea;
      setFormatter(new ConsoleFormatter());
      setLevel(Level.ALL);
   }



   @Override
   public synchronized void publish(LogRecord record) {
      if(record.getLevel().intValue() < this.getLevel().intValue()) {
         return;
      }

      if(this.getFormatter() == null) {
         jTextArea.append(record.getMessage()+"\n");
      } else {
         jTextArea.append(this.getFormatter().format(record));
      }

      
   }

   
   private JTextArea jTextArea;
}
