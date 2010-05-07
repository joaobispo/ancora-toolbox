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

package org.ancora.SharedLibrary.Logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;


/**
 * Extension of Formatter class, used for presenting logging information on a
 * screen.
 *
 * @author Joao Bispo
 */
public class ConsoleFormatter extends Formatter {

   @Override
   public String format(LogRecord record) {

      Level level = record.getLevel();

      if (level == Level.WARNING || level == Level.SEVERE) {
         // Parse Class name
         String classNamespace = record.getSourceClassName();
         int lastIndex = classNamespace.lastIndexOf(CLASS_SEPARATOR);
         String className = classNamespace.substring(lastIndex + 1);


         StringBuilder builder = new StringBuilder(100);
         if (level == Level.SEVERE) {
            builder.append("!!!");
         }
         builder.append("<");
         builder.append(className);
         builder.append(".");
         builder.append(record.getSourceMethodName());
         builder.append("> ");
         builder.append(record.getMessage());
         builder.append(NEWLINE);

         return builder.toString();
      } else {
         return record.getMessage() + NEWLINE;
      }
   }

   ///
   // INSTANCE VARIABLES
   ///

   // Newline
   private final String NEWLINE = System.getProperty("line.separator");
   private final char CLASS_SEPARATOR = '.';
}
