/*
 *  Copyright 2010 SPECS Research Group.
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

package org.specs.Scripter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.Files.LineParser;
import org.ancora.SharedLibrary.OptionsTable.OptionName;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;


/**
 * Options for the ProgramLauncher
 *
 * @author Joao Bispo
 */
public enum ScripterOption implements OptionName {

   programsLaunchnameList("launchNameList",""),
   programsFilenameList("filenameList",""),
   lineParserSplitter("lineParserSplitter",""),
   lineParserJoiner("lineParserJoiner",""),
   lineParserComment("lineParserComment",""),
   isRunningShell("shellcyclestate","true"),
   loggerLevel("logger-level","ALL");

   private ScripterOption(String optionSuffix, String defaultValue) {
      this.optionSuffix = optionSuffix;
      this.defaultValue = defaultValue;
   }

   

   @Override
   public String getOptionName() {
      return OPTION_PREFIX + SEPARATOR + optionSuffix;
   }

   @Override
   public String getDefaultValue() {
      return defaultValue;
   }

      /**
    * If OptionsTable has stored information about the programs table, the method
    * builds the program table from that information.
    *
    * @param state
    * @return
    */
   public static Map<String, String> buildProgramsTable(OptionsTable state) {
      List<String> launchnamesList = state.getList(programsLaunchnameList);
      List<String> filenameList = state.getList(programsFilenameList);

      Map<String, String> table = new HashMap<String, String>();
      for(int i=0; i<launchnamesList.size(); i++) {
         table.put(launchnamesList.get(i), filenameList.get(i));
      }

      if(table.isEmpty()) {
         Logger.getLogger(ScripterOption.class.getName()).
                 warning("Returning empty programs table.");
      }

      return table;
   }

   /**
    * Stores the program table into the OptionsTable.
    * @param scripterPrograms
    * @param optionsTable
    */
   public static void storeProgramsTable(Map<String, String> scripterPrograms, OptionsTable optionsTable) {
      // Clear before storing
      optionsTable.clear(programsLaunchnameList);
      optionsTable.clear(programsFilenameList);

      for(String key : scripterPrograms.keySet()) {
         String value = scripterPrograms.get(key);
         optionsTable.add(programsLaunchnameList, key);
         optionsTable.add(programsFilenameList, value);
      }
   }

   /**
    * Stores the Line Parser into the OptionsTable.
    * @param scripterPrograms
    * @param optionsTable
    */
   public static void storeLineParser(LineParser lineParser, OptionsTable optionsTable) {
      // Clear before storing
      //optionsTable.clear(lineParserList);

      String joiner = lineParser.getJoinerString();
      String splitting = lineParser.getSplittingString();

      optionsTable.set(lineParserJoiner, joiner);
      optionsTable.set(lineParserSplitter, splitting);
      optionsTable.set(lineParserComment, lineParser.getOneLineComment());
   }

   /**
    * Stores the Line Parser into the OptionsTable.
    * @param scripterPrograms
    * @param optionsTable
    */
   public static LineParser buildLineParser(OptionsTable optionsTable) {
      /*
      List<String> parserList = optionsTable.getList(lineParserList);

      int numberOfElements = 3;
      if(parserList.size() != numberOfElements) {
         Logger.getLogger(ScripterOption.class.getName()).
                 warning("List which stores settings for LineParser has number "
                 + "of elements different than "+numberOfElements+" ("+
                 parserList.size()+")");
         return null;
      }
       * 
       */

      String joiner = optionsTable.get(lineParserJoiner);
      String splitting = optionsTable.get(lineParserSplitter);
      String comment = optionsTable.get(lineParserComment);

      return new LineParser(joiner, splitting, comment);
   }

   /**
    * INSTANCE VARIABLES
    */
   private final String optionSuffix;
   private final String defaultValue;
   private static final String OPTION_PREFIX = "scripter";
   private static final String SEPARATOR = ".";
}
