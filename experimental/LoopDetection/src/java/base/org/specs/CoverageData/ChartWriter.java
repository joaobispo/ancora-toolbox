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

package org.specs.CoverageData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.AppBase.AppOptionFile.AppOptionFile;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.suikasoft.Jani.Base.BaseUtils;
import org.suikasoft.Jani.Setup;

/**
 *
 * @author Joao Bispo
 */
public class ChartWriter {

   public ChartWriter(boolean saveChart, boolean displayChart, int drawWidth,
           int drawHeight, int scaleFactor, String chartName) {
      this.saveChart = saveChart;
      this.displayChart = displayChart;
      this.drawWidth = drawWidth;
      this.drawHeight = drawHeight;
      this.scaleFactor = scaleFactor;
      this.chartName = chartName;
   }

   public void createChart(XYSeriesCollection dataset, File outputFolder) {
      String chartTile = "Trace Coverage";
      String xAxisLabel = "Iterations";
      String yAxisLabel = "Coverage %";

      NumberAxis xAxis = new LogarithmicAxis(xAxisLabel);
      // Create Chart
      JFreeChart chart = ChartFactory.createXYLineChart(
              chartTile, // chart title
              xAxisLabel, // x axis label
              yAxisLabel, // y axis label
              dataset, // data
              PlotOrientation.VERTICAL,
              true, // include legend
              true, // tooltips
              false // urls
              );

      ((XYPlot) chart.getPlot()).setDomainAxis(xAxis);

      // get a reference to the plot for further customisation...
      XYPlot plot = (XYPlot) chart.getPlot();
      // Get renderer
      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
      renderer.setBaseShapesVisible(true);
      renderer.setBaseShapesFilled(true);

      // Check if is to be saved
      if(saveChart) {
         saveChart(chart, outputFolder);
      }

      // Check if it is to be displayed
      if (displayChart) {
         displayChart(chart);
      }
   }

   public static void createChart(XYSeriesCollection dataset) {
      String chartTile = "Trace Coverage";
      String xAxisLabel = "Iterations";
      String yAxisLabel = "Coverage %";

      NumberAxis xAxis = new LogarithmicAxis(xAxisLabel);
      // Create Chart
      JFreeChart chart = ChartFactory.createXYLineChart(
              chartTile, // chart title
              xAxisLabel, // x axis label
              yAxisLabel, // y axis label
              dataset, // data
              PlotOrientation.VERTICAL,
              true, // include legend
              true, // tooltips
              false // urls
              );

      ((XYPlot) chart.getPlot()).setDomainAxis(xAxis);

      // get a reference to the plot for further customisation...
      XYPlot plot = (XYPlot) chart.getPlot();
      // Get renderer
      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
      renderer.setBaseShapesVisible(true);
      renderer.setBaseShapesFilled(true);


      try {
         
         int width = 480;
         int height = (int)((double)width / DEFAULT_WIDTH_HEIGHT_RATIO);
         ChartUtilities.saveChartAsPNG(new File("E:\\chart.png"), chart, width, height, null);
         
         OutputStream outputStream = new FileOutputStream(new File("E:\\chart-scaled.png"));
         ChartUtilities.writeScaledChartAsPNG(outputStream, chart, width, height, 4, 4);
         outputStream.close();
      } catch (IOException ex) {
         Logger.getLogger(ChartWriter.class.getName()).log(Level.SEVERE, null, ex);
      }

      // Create and display a frame
      ChartFrame frame = new ChartFrame("Trace Coverage", chart);
      frame.pack();
      frame.setVisible(true);
   }

   

   public static ChartWriter create(File chartConfigFile) {
      AppOptionFile appOption = AppOptionFile.parseFile(chartConfigFile, ChartOptions.class);
       Map<String,AppValue> options = appOption.getMap();

       boolean saveChart = AppUtils.getBool(options, ChartOptions.SaveChart);
       boolean displayChart = AppUtils.getBool(options, ChartOptions.DisplayChart);

       int drawWidth = AppUtils.getInteger(options, ChartOptions.DrawingWidth);
       int drawHeight = AppUtils.getInteger(options, ChartOptions.DrawingHeight);
       int scaleFactor = AppUtils.getInteger(options, ChartOptions.ScaleFactor);

       String chartName = AppUtils.getString(options, ChartOptions.ChartName);

       return new ChartWriter(saveChart, displayChart, drawWidth, drawHeight,
               scaleFactor, chartName);
   }

   public static ChartWriter create(Setup options) {
      //AppOptionFile appOption = AppOptionFile.parseFile(chartConfigFile, ChartOptions.class);
       //Map<String,AppValue> options = appOption.getMap();

       boolean saveChart = BaseUtils.getBoolean(options.get(ChartOptionsV4.SaveChart));
       boolean displayChart = BaseUtils.getBoolean(options.get(ChartOptionsV4.DisplayChart));

       int drawWidth = BaseUtils.getInteger(options.get(ChartOptionsV4.DrawingWidth));
       int drawHeight = BaseUtils.getInteger(options.get(ChartOptionsV4.DrawingHeight));
       int scaleFactor = BaseUtils.getInteger(options.get(ChartOptionsV4.ScaleFactor));

       String chartName = BaseUtils.getString(options.get(ChartOptionsV4.ChartName));

       return new ChartWriter(saveChart, displayChart, drawWidth, drawHeight,
               scaleFactor, chartName);
   }

   private void saveChart(JFreeChart chart, File outputFolder) {
      File outputFile = new File(outputFolder, chartName+".png");
      try {

         OutputStream outputStream = new FileOutputStream(outputFile);
         ChartUtilities.writeScaledChartAsPNG(outputStream, chart, drawWidth, drawHeight,
                 scaleFactor, scaleFactor);
         outputStream.close();
      } catch (IOException ex) {
         Logger.getLogger(ChartWriter.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void displayChart(JFreeChart chart) {
      // Create and display a frame
      ChartFrame frame = new ChartFrame("Trace Coverage", chart);
      frame.pack();
      frame.setVisible(true);
   }

   /**
    * INSTANCE VARIABLES
    */
   private final boolean saveChart;
   private final boolean displayChart;
   private final int drawWidth;
   private final int drawHeight;
   private final int scaleFactor;
   private final String chartName;

   public static final String DEFAULT_CHART_FILENAME = "chart";
   public static final double DEFAULT_WIDTH_HEIGHT_RATIO = 1.619d;




}
