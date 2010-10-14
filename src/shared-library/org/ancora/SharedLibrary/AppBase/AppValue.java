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
 * Represents the value of an option of the program.
 * 
 * <p>Stores current value, type and default value of an option.
 * 
 * @author Joao Bispo
 */
public class AppValue {

   public AppValue(String value, AppValueType type) {


      if (type.isList()) {
         LoggingUtils.getLogger().
                 warning("This constructor does not support List types. Building"
                 + "a String type instead.");
         type = AppValueType.string;
      }

      this.type = type;
      this.value = value;
      this.list = null;

   }

   public AppValue(List<String> values, AppValueType type) {
      
      
      if (!type.isList()) {
         LoggingUtils.getLogger().
                 warning("This constructor is only for List types. Building"
                 + "a ListString type instead.");
         type = AppValueType.stringList;
      }

      this.type = type;
      this.value = null;
      this.list = values;

   }

   public AppValue(int integer) {
      this(Integer.toString(integer), AppValueType.integer);
   }

   public AppValue(boolean bool) {
      this(Boolean.toString(bool), AppValueType.bool);
   }

   public AppValue(String string) {
      this(string, AppValueType.string);
   }

   public AppValue(List<String> list) {
      this.type = AppValueType.stringList;
      this.list = list;
      this.value = null;
   }

   public AppValue(String[] stringArray) {
      this(new ArrayList(Arrays.asList(stringArray)));
      //this(Arrays.asList(stringArray));
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
   public static AppValue newAppOption(AppValueType type) {
      switch(type) {
         case bool:
            return new AppValue(false);
         case integer:
            return new AppValue(0);
         case string:
            return new AppValue("");
         case stringList:
            return new AppValue(new String[0]);
         case multipleChoice:
            return new AppValue("", AppValueType.multipleChoice);
         case multipleChoiceStringList:
            return new AppValue(new ArrayList<String>(), AppValueType.multipleChoiceStringList);
         case multipleSetupList:
            return new AppValue(new ArrayList<String>(), AppValueType.multipleSetupList);
         default:
            LoggingUtils.getLogger().
                    warning("Case not defined: '"+type+"'. Returning null.");
            return null;
      }
   }

   public AppValueType getType() {
      return type;
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
    * Sets the value of the option to the same value in the given AppValue.
    * @param appValue
    */
   public void set(AppValue appValue) {
      if(type.isList()) {
         list = appValue.getList();
      } else {
         this.value = appValue.get();
      }
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

   @Override
   public String toString() {
      if(type.isList()) {
         return list.toString();
      } else {
         return value;
      }
   }

   
   private String value;
   private List<String> list;
   private AppValueType type;
}
