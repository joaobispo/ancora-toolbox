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
 * HeapWindow.java
 *
 * Created on 24/Jun/2010, 16:08:44
 */

package org.ancora.SharedLibrary.Processes;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shows a Swing frame with information about the current and maximum memory
 * of the heap.
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class HeapWindow extends javax.swing.JFrame {

    /** Creates new form HeapWindow */
    public HeapWindow() {
        initComponents();
        long heapMaxSize = Runtime.getRuntime().maxMemory();

        long maxSizeMb = (long)(heapMaxSize / (Math.pow(1024, 2)));
        jLabel2.setText(jLabel2Prefix+maxSizeMb+"Mb");

        final MemProgressBarUpdater memProgressBar = new MemProgressBarUpdater(jProgressBar1);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

         @Override
         public void run() {
            try {
               //(new MemProgressBarUpdater(jProgressBar1)).doInBackground();
               memProgressBar.doInBackground();
            } catch (Exception ex) {
               Logger.getLogger(HeapWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      }, 0, 1000);

/*
        SwingWorker swingWorker = new SwingWorker() {

         @Override
         protected Object doInBackground() throws Exception {
            long heapSize = Runtime.getRuntime().totalMemory();
            long heapFreeSize = Runtime.getRuntime().freeMemory();
            long usedMemory = heapSize - heapFreeSize;

            //jProgressBar1.setString(jLabel2Prefix);
            System.err.println(jProgressBar1.isStringPainted());

            return null;
         }
      };
*/
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jProgressBar1 = new javax.swing.JProgressBar();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      jLabel1.setText("Heap Use/Size");

      jLabel2.setText("Max. Size:");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                  .addContainerGap())
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                  .addComponent(jLabel2)
                  .addContainerGap(44, Short.MAX_VALUE))))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

    public void run() {
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    public void close() {
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               timer.cancel();
                dispose();
            }
        });
    }
    /**
    * @param args the command line arguments
    */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HeapWindow().setVisible(true);
            }
        });
    }
     *
     */

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JProgressBar jProgressBar1;
   // End of variables declaration//GEN-END:variables
   private String jLabel2Prefix = "Max. Size: ";
   private Timer timer;
}
