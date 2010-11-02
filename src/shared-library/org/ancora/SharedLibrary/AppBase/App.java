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

package org.ancora.SharedLibrary.AppBase;

import java.util.Map;

/**
 * Represents the entry point of an application.
 *
 * The contract for any class implementing “App” is that the program defines
 * an enum class implementing AppOptionEnum with the options it wants to use,
 * which may be access in the map that is passed to the application.
 *
 * @author Joao Bispo
 */
public interface App {

   /**
    * Executes the application with the options in the given map.
    *
    * Method throws InterruptedException to support cancelling the task. In the
    * application code, insert:
    *
    * if (Thread.currentThread().isInterrupted()) {
    *  throw new InterruptedException("Task Cancellation");
    * }
    *
    * On the places you want the task to be cancelled.
    *
    * @param options
    * @return
    */
   int execute(Map<String, AppValue> options);
   //int execute(Map<String, AppValue> options) throws InterruptedException ;

   /**
    *
    * @return the enum class implementing AppOptionEnum associated with this App.
    */
   Class getAppOptionEnum();
}
