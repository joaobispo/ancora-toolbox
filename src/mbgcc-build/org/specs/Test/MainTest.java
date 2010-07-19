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

package org.specs.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.specs.OptionsTable.OptionsTable;
import org.specs.ProgramLauncher.ProgramLauncher;
import org.specs.ProgramLauncher.ProgramOption;
import org.specs.ProgramLauncher.SimpleProgram;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class MainTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         //testProgramLauncher();
         testSimpleProgram(args);
    }
/*
    public static OptionsTable testOptions() {
        File optionsFile = new File("mbgcc.options");
        OptionsTable optionsTable = OptionsTable.newOptionsTable(optionsFile);
        System.out.println(optionsTable.getAvaliableOptions());

        optionsTable.add(TestOption.option1, "1");
        optionsTable.add(TestOption.option1, "2");
        System.out.println(optionsTable.getList(TestOption.option1));
        System.out.println(optionsTable.getList(TestOption.option2));
        optionsTable.add(TestOption.option2, "alone");
        System.out.println(optionsTable.getList(TestOption.option2));

        return optionsTable;
    }
*/
    /*
    public static void testProgramLauncher() {
       LoggingUtils.setupConsoleOnly();
       File programsFile = new File("mbgcc.programs");
       ProgramLauncher launcher = ProgramLauncher.newProgramLauncher(programsFile);
       
       OptionsTable state = testOptions();
       // For help to work, we need to set the information about the supported programs
       state.set(ProgramOption.supportedPrograms, programsFile.getPath());

       launcher.runShell(state);
       
       //launcher.runScript(new File("example_script.txt"), state);
    }
*/
    public static void testSimpleProgram(String[] args) {
      //try {
         LoggingUtils.setupConsoleOnly();
               String optionsFilename = "./data/mbgcc.options";
               String programsFilename = "./data/mbgcc.programs";
         //String optionsFilename = "org/specs/Test/mbgcc.options";
         //String programsFilename = "org/specs/Test/mbgcc.programs";

         //this.getClass().getClassLoader().getResourceAsStream(optionsFilename)
         //InputStream inputStream = MainTest.class.getClassLoader().getResourceAsStream(optionsFilename);
         //BufferedInputStream input= new BufferedInputStream(inputStream);
         //Scanner scanner = new Scanner(inputStream);
/*
         System.out.println("Scanner output:");
         while(true) {
            System.out.println(scanner.nextLine());
         }
 *
 */
         //BufferedReader reader = new BufferedReader(inputStream);
         //reader.
         
         //URL url = MainTest.class.getClassLoader().getResource(optionsFilename);
         //System.out.println("URL:" + url);
         File optionsFile = IoUtils.existingFile(optionsFilename);
         //File optionsFile = new File(url.toString());
         //File optionsFile = IoUtils.existingFile(MainTest.class.getClassLoader().getResource(optionsFilename).getPath());
         //File optionsFile = new File(MainTest.class.getClassLoader().getResource("dd").getPath());
         //File programsFile = IoUtils.existingFile(MainTest.class.getClassLoader().getResource(programsFilename).getPath());
         File programsFile = IoUtils.existingFile(programsFilename);
         SimpleProgram simpleProgram = SimpleProgram.newSimpleProgram(programsFile, optionsFile);
         simpleProgram.start(args);
        
    /*
    } catch (URISyntaxException ex) {
         Logger.getLogger(MainTest.class.getName()).
                 warning("URI Syntax Exception. Message:"+ex.getMessage());
      }
     * 
     */
    }
}
