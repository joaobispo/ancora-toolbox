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

/*
 * ProgramPanel.java
 *
 * Created on 27/Set/2010, 10:32:12
 */

package org.ancora.SharedLibrary.AppBase.SimpleGui;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ProgramPanel extends javax.swing.JPanel {

    /** Creates new form ProgramPanel */
    public ProgramPanel(App application) {
       initComponents();

      this.application = application;
      customInit();
   }

   private void customInit() {
              // Set last used file
        Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
        String lastFile =prefs.get(OPTION_LAST_USED_FILE, "");

        filenameTextField.setText(lastFile);

        // Init file chooser
        fc = new JFileChooser();

        // Redirect output to jtextfiled
        Handler[] handlersTemp = LoggingUtils.getRootLogger().getHandlers();
        Handler[] newHandlers = new Handler[handlersTemp.length+1];
        System.arraycopy(handlersTemp, 0, newHandlers, 0, handlersTemp.length);
        newHandlers[handlersTemp.length] = new JTextAreaHandler(outputArea);
        newHandlers[handlersTemp.length].setLevel(Level.FINEST);
        LoggingUtils.setRootHandlers(newHandlers);


        //LoggingUtils.setLevel(Level.FINEST);
        //System.err.println("Instantiate logger level 2:"+logger.getLevel());
        //logger.addHandler(new JTextAreaHandler(outputArea));
      


        // Init buttons
        setButtonsEnable(true);
        worker = null;
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLabel1 = new javax.swing.JLabel();
      filenameTextField = new javax.swing.JTextField();
      browseButton = new javax.swing.JButton();
      cancelButton = new javax.swing.JButton();
      startButton = new javax.swing.JButton();
      jScrollPane1 = new javax.swing.JScrollPane();
      outputArea = new javax.swing.JTextArea();

      jLabel1.setText("Input file:");

      browseButton.setText("Browse...");
      browseButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            browseButtonActionPerformed(evt);
         }
      });

      cancelButton.setText("Cancel");
      cancelButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelButtonActionPerformed(evt);
         }
      });

      startButton.setText("Start");
      startButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            startButtonActionPerformed(evt);
         }
      });

      outputArea.setColumns(20);
      outputArea.setEditable(false);
      outputArea.setRows(5);
      jScrollPane1.setViewportView(outputArea);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(filenameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(browseButton)
            .addContainerGap())
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(startButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cancelButton)
            .addContainerGap(298, Short.MAX_VALUE))
         .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(filenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(browseButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(startButton)
               .addComponent(cancelButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
      );
   }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed

       fc.setCurrentDirectory(new File(filenameTextField.getText()));
       int returnVal = fc.showOpenDialog(this);

       if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          filenameTextField.setText(file.getAbsolutePath());
       }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
       // Check if there is a worker
       if(worker == null) {
          outputArea.append("Application is not running.");
          return;
       }

       //boolean interruptIfRunning = true;
       //worker.cancel(interruptIfRunning);

       workerExecutor.shutdownNow();
       setButtonsEnable(true);
}//GEN-LAST:event_cancelButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
       // Clear text area
       outputArea.setText("");

       // Check if file is valid
       String filename = filenameTextField.getText();
       File file = new File(filename);
       if(!file.exists()) {
          outputArea.append("File '"+filename+"' does not exist.");
          return;
       }

       // Save accessed file
       Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
       prefs.put(OPTION_LAST_USED_FILE, filename);

       // Get Options from file
       AppOptionFile optionFile = AppOptionFile.parseFile(file, application.getAppOptionEnum());
       if(optionFile == null) {
          outputArea.append("Could not load options from '"+filename+"'.");
          return;
       }

       worker = new ApplicationWorker(this, optionFile.getMap());
       //ApplicationWorker runner = new ApplicationWorker(this, optionFile.getMap());
       //ApplicationWorker runner = new ApplicationWorker(this, filename);
       //runner.execute();
       workerExecutor = Executors.newSingleThreadExecutor();
       workerExecutor.submit(worker);

    }//GEN-LAST:event_startButtonActionPerformed

   public final void setButtonsEnable(boolean enable) {
      browseButton.setEnabled(enable);
      startButton.setEnabled(enable);
      cancelButton.setEnabled(!enable);
   }

   public App getApplication() {
      return application;
   }

   public JTextField getFilenameTextField() {
      return filenameTextField;
   }



   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton browseButton;
   private javax.swing.JButton cancelButton;
   private javax.swing.JTextField filenameTextField;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JTextArea outputArea;
   private javax.swing.JButton startButton;
   // End of variables declaration//GEN-END:variables


   // Other variables
   private JFileChooser fc;
   final private App application;
   private ApplicationWorker worker;
   private ExecutorService workerExecutor;

   private static final String OPTION_LAST_USED_FILE = "lastUsedFile";

}
