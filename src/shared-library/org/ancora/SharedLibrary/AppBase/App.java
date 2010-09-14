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
 * The contract for any class implementing “App” is that any AppOption that was
 * defined may be used by the program. It is not guaranteed that the program
 * will check if a given AppOption exists in the Map.
 *
 * @author Joao Bispo
 */
public interface App {

   int execute(Map<String, AppOption> options);
}
