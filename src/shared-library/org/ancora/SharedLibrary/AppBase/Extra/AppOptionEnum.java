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

package org.ancora.SharedLibrary.AppBase.Extra;

import org.ancora.SharedLibrary.AppBase.AppOptionType;

/**
 * We suggest storing information about options inside an enumeration file
 * implementing this interface. It gives convenient access to the options and
 * might enables other features, such as automatic construction of a table with
 * empty AppOption objects.
 * 
 * @author Joao Bispo
 */
public interface AppOptionEnum {

   String getName();

   AppOptionType getType();


}
