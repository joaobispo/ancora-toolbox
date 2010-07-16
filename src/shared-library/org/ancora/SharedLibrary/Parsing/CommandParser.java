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

package org.ancora.SharedLibrary.Parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.LineReader;

/**
 * Given a string, splits the string into its arguments. 
 * 
 * <p>Detects commented lines.
 *
 * @author Joao Bispo
 */
public class CommandParser {

   /**
    * Returns a new CommandParser where the command separator is a whitespace
    * (' '), the argument 'gatherer' is a quotation mark ('"') and the comment
    * prefix is double slash ('//')
    */
   public CommandParser() {
      this.commandSeparator = COMMAND_SEPARATOR;
      this.commandGatherer = COMMAND_GATHERER;
      this.commentPrefix = COMMENT_PREFIX;
   }

   public CommandParser(String commandSeparator, String commandGatherer, String commandCommentPrefix) {
      this.commandSeparator = commandSeparator;
      this.commandGatherer = commandGatherer;
      this.commentPrefix = commandCommentPrefix;
   }



   /**
    * 
    * @param command
    * @return
    */
   public List<String> splitCommand(String command) {
      // Trim string
      command = command.trim();

      // Check if it starts with comment
      if(command.startsWith(commentPrefix)) {
         return new ArrayList<String>();
      }

      List<String> commands = new ArrayList<String>();

      while(command.length() > 0) {
         // Get indexes
         int spaceIndex = command.indexOf(commandSeparator);
         int quoteIndex = command.indexOf(commandGatherer);

         // Check which comes first
         if(spaceIndex == -1 && quoteIndex == -1) {
            commands.add(command);
            command = "";
            continue;
         }

         if(spaceIndex < quoteIndex) {
            String argument = command.substring(0, spaceIndex);
            commands.add(argument);
            command = command.substring(spaceIndex+1).trim();
         } else {
            // Find second quote
            int quoteIndex2Increment = command.substring(quoteIndex+1).indexOf(commandGatherer);
            if(quoteIndex2Increment == -1 && spaceIndex == -1) {
               // Capture last argument
               commands.add(command.trim());
               command = "";
            } else if(quoteIndex2Increment == -1 && spaceIndex != -1){
               String argument = command.substring(quoteIndex+1, spaceIndex);
               commands.add(argument);
               command = command.substring(spaceIndex+1);
            } else {
               //System.out.println("Quote:"+quoteIndex);
               //System.out.println("Quote2:"+quoteIndex2Increment);
               //System.out.println("Quote2 Real:"+(quoteIndex+quoteIndex2Increment+1));
               int quote2 = (quoteIndex+quoteIndex2Increment+1);
               String argument = command.substring(quoteIndex+1, quote2);
               commands.add(argument);
               command = command.substring(quote2+1);
            }
         }
      }


      return commands;
   }

   /**
    * Reads a Settings file and returns a table with the key-value pairs.
    *
    * <p> Any line with two or more parameters, as defined by the settings of this
    * class, is put in the table. The first parameters is used as the key, and
    * the second as the value. If a line has more than two parameters, they are
    * ignored.
    * 
    * @param settingsFile
    * @return a table with key-value pairs.
    */
   public Map<String, String> getTableFromFile(File settingsFile) {
    LineReader lineReader = LineReader.createLineReader(settingsFile);

      String line;
      Map<String, String> programsTable = new HashMap<String, String>();

      while((line = lineReader.nextLine()) != null) {
         List<String> arguments = splitCommand(line);
         if(arguments.size() >= 2) {
            String key = arguments.get(0);
            String value = arguments.get(1);
            programsTable.put(key, value);
         }
      }

      return programsTable;
   }

   /**
    * INSTANCE VARIABLES
    */
   private String commandSeparator;
   private String commandGatherer;
   private String commentPrefix;

   public static final String COMMAND_SEPARATOR = " ";
   public static final String COMMAND_GATHERER = "\"";
   public static final String COMMENT_PREFIX = "//";
}
