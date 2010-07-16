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

package org.ancora.SharedLibrary.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Given a string, splits the string into a list of arguments, following some
 * rules.
 *
 * <p>The current rules:
 * <br>- Spliting by a custom string (e.g. space ' ');
 * <br>- One line comments (e.g. //);
 * <br>- 'Joiner', to include characters left out by spliting character
 * (e.g. " -> "something written with spaces")
 *
 * @author Joao Bispo
 */
public class LineParser {

   /**
    * Returns a new CommandParser where the command separator is a whitespace
    * (' '), the argument 'gatherer' is a quotation mark ('"') and the comment
    * prefix is double slash ('//')
    */
   /*
   public LineParser() {
      this.commandSeparator = COMMAND_SEPARATOR;
      this.commandGatherer = COMMAND_GATHERER;
      this.commentPrefix = COMMENT_PREFIX;
   }
    *
    */

   public LineParser(String splittingString, String joinerString, String oneLineComment) {
      this.commandSeparator = splittingString;
      this.commandGatherer = joinerString;
      this.commentPrefix = oneLineComment;

      // Make some checks
      if(oneLineComment.length() == 0) {
         Logger.getLogger(LineParser.class.getName()).
                 warning("OneLineComment is an empty string. This will make all " +
                 "lines in the file appear as comments.");
      }
   }



   /**
    * Splits the String into arguments, following the rules of the parser.
    *
    * <p>The input string is trimmed before parsing.
    *
    * @param command
    * @return
    */
   public List<String> splitCommand(String command) {
      // Trim string
      command = command.trim();

      // Check if it starts with comment
      //if(commentPrefix.length() > 0) {
      if(command.startsWith(commentPrefix)) {
         return new ArrayList<String>();
      }
      //}

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
    * INSTANCE VARIABLES
    */
   private final String commandSeparator;
   private final String commandGatherer;
   private final String commentPrefix;

   //public static final String COMMAND_SEPARATOR = " ";
   //public static final String COMMAND_GATHERER = "\"";
   //public static final String COMMENT_PREFIX = "//";
}
