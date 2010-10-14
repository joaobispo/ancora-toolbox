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

package org.ancora.SharedLibrary.AppBase.SimpleGui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ancora.SharedLibrary.AppBase.AppOptionEnum;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.Entry;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.OptionFileUtils;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.AppBase.SimpleGui.Panels.AppOptionPanel;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ProcessUtils;

/**
 * Panel which will contain the options
 *
 * @author Joao Bispo
 */
public class AppFilePanel extends JPanel {

   public AppFilePanel(Class appOptionEnum) {
      panels = new HashMap<String, AppOptionPanel>();

      // Extract the enum values
      AppOptionEnum[] values = AppUtils.getEnumValues(appOptionEnum);

      LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      setLayout(layout);
      
      //JPanel options = new JPanel();

      // Add panels, in alphabetical order
      //List<String> keyList = new ArrayList<String>(appOptionEnum.keySet());
      //Collections.sort(keyList);
      //for(String key : keyList) {
      for(int i=0; i<values.length; i++) {
         //AppOptionPanel panel = PanelUtils.newPanel(appOptionEnum.get(key));
         AppOptionPanel panel = PanelUtils.newPanel(values[i]);

         if(panel == null) {
            continue;
         }

         add(panel);
         panels.put(AppUtils.buildEnumName(values[i]), panel);
      }

      //add(Box.createVerticalGlue());


      // Make the panel scrollable

      /*
      JScrollPane scrollPane = new JScrollPane();

      scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
      scrollPane.setViewportView(options);
*/
   }

   public Map<String, AppOptionPanel> getPanels() {
      return panels;
   }

   public void loadValues(AppOptionFile optionFile) {
      Map<String, AppValue> map = optionFile.getMap();
      Map<String, Entry> entries = optionFile.getEntryList().getEntriesMapping();
      for (String key : optionFile.getMap().keySet()) {
         AppValue value = map.get(key);

         // Get panel
         //JPanel panel = panels.get(key);
         AppOptionPanel panel = panels.get(key);
         PanelUtils.updatePanel(panel, value);
         // Get comments
         updateTooltipText(panel, entries.get(key).getComments());
      }
   }

   private void updateTooltipText(final JPanel panel, List<String> comments) {

      final String commentString = OptionFileUtils.parseComments(comments);
      ProcessUtils.runOnSwing(new Runnable() {

         @Override
         public void run() {
            panel.setToolTipText(commentString);
         }
      });
   }

   /**
    * Collects information in all the panels and returns a map with the information.
    *
    * @return
    */
   public Map<String, AppValue> getMapWithValues() {
      Map<String, AppValue> updatedMap = new HashMap<String, AppValue>();
      for(String key : panels.keySet()) {
         JPanel panel = panels.get(key);
         AppValue value = PanelUtils.getAppValue((AppOptionPanel)panel);
         if(value == null) {
            LoggingUtils.getLogger().
                    warning("value is null.");
            // No valid value for the table
            continue;
         }
         updatedMap.put(key, value);
      }

      return updatedMap;
   }

   private Map<String, AppOptionPanel> panels;

}
