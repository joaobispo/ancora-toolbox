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

import java.util.List;
import org.ancora.SharedLibrary.AppBase.AppValue;

/**
 * A single entry in AppOptionFile
 *
 * @author Joao Bispo
 */
public class Entry {
 public Entry(List<String> comments, String optionName, AppValue optionValue) {
         this.comments = comments;
         this.optionValue = optionValue;
         this.optionName = optionName;
      }

      public List<String> getComments() {
         return comments;
      }

      public AppValue getOptionValue() {
         return optionValue;
      }

      public String getOptionName() {
         return optionName;
      }

      @Override
      public String toString() {
         return super.toString();
      }



      final private List<String> comments;
      final private String optionName;
      final private AppValue optionValue;
}
