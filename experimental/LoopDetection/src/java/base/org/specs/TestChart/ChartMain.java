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

package org.specs.TestChart;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.specs.DToolPlus.Config.SystemSetup;
import org.specs.DToolPlus.DToolUtils;
import org.specs.DToolPlus.DToolReader;
import org.specs.DymaLib.TraceReader;
import system.SysteM;
import system.SysteMException;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ChartMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();
        //demoPieChart();
       xyMultipleDataset();
       //traceReader();
    }

   private static void demoPieChart() {
      // create a dataset...
      DefaultPieDataset dataset = new DefaultPieDataset();
      dataset.setValue("Category 1", 43.2);
      dataset.setValue("Category 2", 27.9);
      dataset.setValue("Category 3", 79.5);

      // create a chart...
      JFreeChart chart = ChartFactory.createPieChart(
              "Sample Pie Chart",
              dataset,
              true, // legend?
              true, // tooltips?
              false // URLs?
              );

      // create and display a frame...
      ChartFrame frame = new ChartFrame("Test", chart);
      frame.pack();
      frame.setVisible(true);
   }

   private static void xyMultipleDataset() {
      // Create series for BasicBlock
      XYSeries series1 = new XYSeries("BasicBlock");
      series1.add(1, 1*100);
      series1.add(2, 0.47102431468976*100);
      series1.add(90, 0.257657303095461*100);

      // Create series for MegaBlock
      XYSeries series2 = new XYSeries("MegaBlock");
      series2.add(1, 1*100);
      series2.add(2, 0.92705632281993*100);
      series2.add(90, 0.639060454958277*100);

      // Create series for WarpBlock
      //XYSeries series3 = new XYSeries("WarpBlock");
      XYSeries series3 = new XYSeries("BackwardBranchBlock");
      series3.add(1, 1*100);
      series3.add(2, 0.673735793687178*100);
      series3.add(90, 0.32752345990641*100);


      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(series1);
      dataset.addSeries(series2);
      dataset.addSeries(series3);

      // Create Chart
      JFreeChart chart = ChartFactory.createXYLineChart(
              "XY Chart Demo", // chart title
              "Iterations", // x axis label
              "Coverage %", // y axis label
              dataset, // data
              PlotOrientation.VERTICAL,
              true, // include legend
              true, // tooltips
              false // urls
              );


      // get a reference to the plot for further customisation...
      XYPlot plot = (XYPlot) chart.getPlot();
      // Get renderer
      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
      renderer.setBaseShapesVisible(true);
      renderer.setBaseShapesFilled(true);
      
      // Create and display a frame
      ChartFrame frame = new ChartFrame("Test", chart);
      frame.pack();
      frame.setVisible(true);
   }

   private static void traceReader() {
      File elfFile = new File("E:\\temp\\elf\\bs-Os.elf");
      SystemSetup setup = SystemSetup.getDefaultConfig();
      
      //TraceReader traceReader = DToolReader.newDToolReader(elfFile, setup);

      //String systemConfig = "./Configuration Files/systemconfig.xml";
      String systemConfig = "D:\\Programming\\Ancora\\AncoraToolbox\\experimental\\LoopDetection\\run\\Configuration Files\\systemconfig.xml";
      String elfFilename = elfFile.getPath();
      try {
         //SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, false);
         //SysteM originalSystem = DToolUtils.newSysteM(systemConfig, elfFilename, setup);
         SysteM system = new SysteM(systemConfig, elfFilename, false, true);
      } catch (SysteMException ex) {
         System.out.println("Exception!");
         System.out.println(ex.getMessage());
         System.out.println(Arrays.toString(ex.getStackTrace()));
      }
   }



}
