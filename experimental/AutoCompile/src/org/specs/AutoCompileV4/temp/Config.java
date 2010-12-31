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

package org.specs.AutoCompileV4.temp;

import org.suikasoft.Jani.Base.EnumKey;
import org.suikasoft.Jani.Base.OptionType;


/**
 * Options available for targets
 *
 * @author Joao Bispo
 */
public enum Config implements EnumKey {

   targetFolder(OptionType.string);
   //targetFolder("targetFolder", OptionType.string);
   //jobFile("jobFile", OptionType.string);
   //targetOptions("targetOptions", OptionType.stringList),
   //targetFiles("targetFiles", OptionType.stringList);

   //private Config(String name, OptionType type) {
   private Config(OptionType type) {
      //this.name = name;
      this.type = type;
   }

   public OptionType getType() {
      return type;
   }

   //final private String name;
   final private OptionType type;

   //final private static String prefix = "config.";
}
