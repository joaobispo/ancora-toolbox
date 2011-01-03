/*
 *  Copyright 2010 SPeCS Research Group.
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

package org.ancora.SharedLibrary;

import java.util.Collection;

/**
 * Utility methods related to classes.
 *
 * @author Joao Bispo
 */
public class ClassUtils {

   /**
    * @param values
    * @return the class of the first element returned by this Collection iterator.
    * If there are no elements, returns null
    */
   public static Class getClass(Collection<?> values) {
      if(values.isEmpty()) {
         LoggingUtils.getLogger().
                 warning("Given collection is empty.");
         return null;
      }

      return values.iterator().next().getClass();
      
   }
}
