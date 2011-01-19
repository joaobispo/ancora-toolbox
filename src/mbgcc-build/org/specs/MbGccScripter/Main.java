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

package org.specs.MbGccScripter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ancora.SharedLibrary.Utilities.LineParser;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.OptionsTable.OptionName;
import org.ancora.SharedLibrary.OptionsTable.OptionsTable;
import org.ancora.SharedLibrary.OptionsTable.OptionsTableFactory;
import org.specs.MbGccScripter.Programs.Compile;
import org.specs.MbGccScripter.Programs.Mode;
import org.specs.Scripter.SimpleScripter;

/**
 * Calls the Scripter
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      LoggingUtils.setupConsoleOnly();

       LineParser lineParser = LineParser.getDefaultLineParser();
       OptionsTable options = OptionsTableFactory.fromEnumNamesList(enumWithOptions, optionsListSeparator);

        SimpleScripter scripter = new SimpleScripter(mbgccPrograms, lineParser, options);
        scripter.start(args);
    }

    private static final Map<String, String> mbgccPrograms;
    private static final List<String> enumWithOptions;
    private static final String optionsListSeparator = ";";

    static {
       mbgccPrograms = new HashMap<String, String>();
       mbgccPrograms.put("mode", Mode.class.getName());
       mbgccPrograms.put("compile", Compile.class.getName());
       // mode
       // compile

       enumWithOptions = new ArrayList<String>();
       enumWithOptions.add(org.specs.Scripter.ScripterOption.class.getName());
       enumWithOptions.add(org.specs.MbGccScripter.MbGccOption.class.getName());
    }



}
