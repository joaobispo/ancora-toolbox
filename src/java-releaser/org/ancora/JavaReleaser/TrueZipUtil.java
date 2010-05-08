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

package org.ancora.JavaReleaser;

import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import java.awt.EventQueue;
import java.util.logging.Logger;
import org.ancora.JavaReleaser.Gui.FrameData;
import org.ancora.JavaReleaser.Gui.ReleaserFrame;

/**
 *
 * @author Joao Bispo
 */
public class TrueZipUtil {

   public static boolean zipNetbeansDist(ReleaserFrame frame) {
      FrameData data = frame.buildFrameData();


      // Create names
      String releaseFilename = data.getReleaseName() + ".zip";
      String javadocFilename = data.getReleaseName() + "-javadoc.zip";

      // Create Folders
      File outputFolder = new File(data.getOutputFoldername());
      outputFolder.mkdirs();
      if(!outputFolder.exists()) {
         writeTextField(frame, "Could not create output folder.");
         frame.returnFromZip();
         return false;
      }
      
      File distFolder = new File(data.getDistFoldername());
      File javadocFolder = new File(distFolder, "javadoc");
      java.io.File tempFolder = new File(outputFolder, TEMP_FOLDER);

      // Create ZipFiles
      File releaseZip = new File(outputFolder, releaseFilename);
      File javadocZip = new File(outputFolder, javadocFilename);

      // Delete zipfiles
      writeTextField(frame, "Deleting previous zip files.");
      releaseZip.delete();
      javadocZip.delete();

      // Create Javadoc Zip
      if(data.isBuildJavadocZip()) {
         writeTextField(frame, "Creating javadoc zip.");
         javadocFolder.copyAllTo(javadocZip);
      }
      //System.out.println("Written "+javadocZip.getName()+".");

      // Copy dist folder to temporary folder
      writeTextField(frame, "Copying files to temporary folder.");
      distFolder.copyAllTo(tempFolder);

      // Delete Javadoc
      File javadocTempFolder = new File(tempFolder, "javadoc");
      javadocTempFolder.deleteAll();

      // Delete readme.txt
      File readme = new File(tempFolder, "README.TXT");
      readme.delete();

      // If RunFolder different than Null, copy it too
      if(data.getRunFoldername() != null) {
         File runFolder = new File(data.getRunFoldername());
         runFolder.copyAllTo(tempFolder);
      }

      // Zip contents of tempFolder
      writeTextField(frame, "Creating release zip.");
      File tempZipFolder = new File(tempFolder);
      tempZipFolder.copyAllTo(releaseZip);

      // Delete temporary folder
      writeTextField(frame, "Deleting temporary files.");
      tempZipFolder.deleteAll();

      
      // Unmount Zip Files.
      try {
         File.umount(releaseZip);
         File.umount(javadocZip);
      } catch (ArchiveException ex) {
         Logger.getLogger(TrueZipUtil.class.getName()).
                 warning("ArchiveException while trying to unmount Zip Files:"+
                 ex.getMessage());
      }

      writeTextField(frame, "Finished!");
      frame.returnFromZip();
      return true;
   }

   private static void writeTextField(final ReleaserFrame frame, final String message) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            frame.writeMessage(message);
         }

      });
   }

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteDir(java.io.File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

   private static String TEMP_FOLDER = "release_temp";
}
