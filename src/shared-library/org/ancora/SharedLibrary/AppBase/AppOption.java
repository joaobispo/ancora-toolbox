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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Represents an option of the program. 
 * 
 * <p>Stores current value, type and default value of an option.
 * 
 * @author Joao Bispo
 */
public class AppOption {

   private AppOption(String value, AppOptionType type) {
      
      
      if (type.isList()) {
         LoggingUtils.getLogger().
                 warning("This constructor does not support List types. Building"
                 + "a String type instead.");
         type = AppOptionType.string;
      }

      this.type = type;
      //else {
         this.value = value;
         this.list = null;
      //}
   }

   public AppOption(int integer) {
      this(Integer.toString(integer), AppOptionType.integer);
   }

   public AppOption(boolean bool) {
      this(Boolean.toString(bool), AppOptionType.bool);
   }

   public AppOption(String string) {
      this(string, AppOptionType.string);
   }

   public AppOption(List<String> list) {
      this.type = AppOptionType.stringList;
      this.list = list;
      this.value = null;
   }

   public AppOption(String[] stringArray) {
      this(Arrays.asList(stringArray));
   }

   /**
    * Builds a new AppOption using a default value for the type.
    * 
    * <p>The default values are:
    * <br>string: "" (empty string)
    * <br>boolean: false
    * <br>integer: 0
    * <br>stringList: empty list
    * 
    * @param type
    */
   public static AppOption newAppOption(AppOptionType type) {
      switch(type) {
         case bool:
            return new AppOption(false);
         case integer:
            return new AppOption(0);
         case string:
            return new AppOption("");
         case stringList:
            return new AppOption(new String[0]);
         default:
            LoggingUtils.getLogger().
                    warning("Case not defined: '"+type+"'. Returning null.");
            return null;
      }
   }

   /**
    * Sets the value of the option. Does not work for list types.
    * 
    * @param value
    */
   public void set(String value) {
      if(type.isList()) {
         LoggingUtils.getLogger().
                 warning("This method is not defined for List types");
         return;
      }

      this.value = value;
   }

   /**
    * @return the value stored in the option. Does not work for list types.
    */
   public String get() {
      if(type.isList()) {
         LoggingUtils.getLogger().
                 warning("This method is not defined for List types. Returning null.");
         return null;
      }

      return value;
   }

   public List<String> getList() {
      if(!type.isList()) {
         LoggingUtils.getLogger().
                 warning("This method is only defined for List types. Returning null.");
         return null;
      }

      return list;
   }

   public void setList(List<String> list) {
     if(!type.isList()) {
         LoggingUtils.getLogger().
                 warning("This method is only defined for List types.");
      }

     this.list = list;
   }

   private String value;
   private List<String> list;
   private AppOptionType type;
}
