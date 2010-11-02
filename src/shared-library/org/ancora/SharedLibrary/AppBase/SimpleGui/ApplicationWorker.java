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

package org.ancora.SharedLibrary.AppBase.SimpleGui;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Launches an App object from the ProgramPanel.
 *
 * @author Joao Bispo
 */
public class ApplicationWorker {

   public ApplicationWorker(ProgramPanel programPanel) {
      this.mainWindow = programPanel;
      this.returnValue = null;
      workerExecutor = null;

   }


   /**
    * Executes the application in another thread.
    * 
    * @param options
    */
   public void execute(final Map<String,AppValue> options) {

      // Run
     ExecutorService monitor = Executors.newSingleThreadExecutor();
     monitor.submit(new Runnable() {

         @Override
         public void run() {
            runner(options);
         }
      });

   }

   /**
    * To be run on Monitor thread, so the Gui is not waiting for the result of
    * task.
    *
    * @param options
    */
   private void runner(Map<String, AppValue> options) {
      // Disable buttons
      setButtons(false);


      // Create task
      Callable<Integer> task = getTask(options);

      // Submit task
      workerExecutor = Executors.newSingleThreadExecutor();
      Future<Integer> future = workerExecutor.submit(task);

      // Check if task finishes
      Integer result = null;
      try {
         result = future.get();
      } catch (InterruptedException ex) {
         Thread.currentThread().interrupt(); // ignore/reset
      } catch (ExecutionException ex) {
         LoggingUtils.getLogger().
                 //warning("Caught Exception:"+ex.getMessage());
                 warning(ex.toString());
      }

      if (result == null) {
         LoggingUtils.getLogger().
                 info("Cancelled application.");
      } else if (result.compareTo(0) != 0) {
         LoggingUtils.getLogger().
                 info("Application returned non-zero value:" + result);
      }

      // Enable buttons again
      setButtons(true);

   }



   private void setButtons(final boolean enable) {
            ProcessUtils.runOnSwing(new Runnable() {

         @Override
         public void run() {
            mainWindow.setButtonsEnable(enable);
         }
      });

   }

   /**
    * Builds a task out of the application
    * @return
    */
   private Callable<Integer> getTask(final Map<String,AppValue> options) {
      Callable<Integer> task = new Callable<Integer>() {

         @Override
         public Integer call() throws Exception {
            returnValue = mainWindow.getApplication().execute(options);
            return returnValue;
         }
      };

      return task;
   }

   public void shutdown() {
      if(workerExecutor == null) {
         LoggingUtils.getLogger().
                 warning("Application is not running.");
         return;
      }

      workerExecutor.shutdownNow();
   }

   public Integer getReturnValue() {
      return returnValue;
   }



   private ProgramPanel mainWindow;
   private Integer returnValue;
   private ExecutorService workerExecutor;



}
