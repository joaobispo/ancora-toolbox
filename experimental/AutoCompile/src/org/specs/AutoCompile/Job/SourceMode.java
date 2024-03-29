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

package org.specs.AutoCompile.Job;

/**
 * Distiguishes between two situations about the given source folder:
 *
 * 1) files: Each .c file inside the source folder is a program;
 * 3) folders: Each folder inside the source folder is a program;
 * 1) singleFile: the source folder is interpreted as a single file, which corresponds to a program;
 * 2) singleFolder: The files inside the source folder is a program;
 * 
 *
 * @author Joao Bispo
 */
public enum SourceMode {
   files,
   folders,
   singleFile,
   singleFolder;

}
