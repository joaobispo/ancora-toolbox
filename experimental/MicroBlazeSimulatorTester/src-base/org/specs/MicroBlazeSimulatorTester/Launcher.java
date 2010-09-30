/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.specs.MicroBlazeSimulatorTester;

import java.util.logging.Level;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.SimpleGui.SimpleGui;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
LoggingUtils.setupConsoleOnly();
       LoggingUtils.getRootLogger().setLevel(Level.INFO);

       // Create autocompile application
       App tester = new Tester();


       // Launch GUI

       SimpleGui gui = new SimpleGui(tester);
       gui.setTitle(TITLE_STRING);
       gui.execute();

    }

    public final static String TITLE_STRING = "Tester for FireWorks (MicroBlaze) Processor v1.2";
}
