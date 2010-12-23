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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.specs.LoopDetection.LoopDetectionInfo;
import org.specs.LoopDetection.SegmentProcessorJobs.TcDataCollector;

/**
 *
 * @author Joao Bispo
 */
public class GlobalDataColector {

   public GlobalDataColector() {

      // Initialize main table
      mainTable = new HashMap<String, List<TcData>>();

      // Calculate maximum number of repetitions
      maxRep = 0;
      maxBlockSize = 0;

      keys = new ArrayList<String>();
   }

   public Map<String, List<TcData>> getMainTable() {
      return mainTable;
   }

   public void addTcData(TcDataCollector tcDataCollector, LoopDetectionInfo loopInfo) {
      //String detectorName = loopInfo.detectorName;
      //String detectorName = getName(loopInfo);
      String detectorName = loopInfo.loopDetectorId;
      TcData tcData = tcDataCollector.tcData;

      long currentMaxBlockSize = tcDataCollector.getMaxBlockSize();
      int currentMaxRep = tcDataCollector.getMaxRep();

      // Check if we have an entry for the given detector
      List<TcData> detectorData = mainTable.get(detectorName);
      if (detectorData == null) {
         detectorData = new ArrayList<TcData>();
         mainTable.put(detectorName, detectorData);
         keys.add(detectorName);
      }

      detectorData.add(tcData);

      // Update maximum values
      maxRep = Math.max(maxRep, currentMaxRep);
      maxBlockSize = Math.max(maxBlockSize, currentMaxBlockSize);
   }

   public XYSeriesCollection processData() {
   //public void processData() {
      int maxRepetitions = maxRep+1;

      // Create data collection
      XYSeriesCollection dataset = new XYSeriesCollection();

      // Get x axis
      List<Integer> masterLine = TcProcess.getMasterLine(mainTable, maxRepetitions);
      for (String detector : keys) {
      //for (String detector : mainTable.keySet()) {
         List<TcData> stats = mainTable.get(detector);

         double ratioTotals[] = new double[masterLine.size()];
         // Calculate Partitioner Average
         for (TcData stat : stats) {
            // Get line with values for the number of instructions
            List<Long> absValues = TcProcess.getAbsReduxLine(stat, maxRepetitions, masterLine);
            // Calculate ratio
            double[] ratioValues = new double[masterLine.size()];
            long totalInst = stat.getTotalInstructions();

            for (int i = 0; i < masterLine.size(); i++) {
               ratioValues[i] = (double) absValues.get(i) / (double) totalInst;

               // Add to totals
               ratioTotals[i] += ratioValues[i];
               //absTotals[i] += absValues.get(i);
            }

            // Save data to the csv file
            //TcProcess.csvAppend(ratioFile, stat.getFilename(), ratioValues);
         }

         // Calculate averages
         double iterations = stats.size();
         //double absAvg[] = new double[masterLine.size()];
         double ratioAvg[] = new double[masterLine.size()];
         // Calculate max value from absAvg to use for normalization
         //double absNormFactor = (double) absTotals[0] / iterations;

         for (int i = 0; i < masterLine.size(); i++) {
            //absAvg[i] = ((double) absTotals[i] / (double) iterations) / absNormFactor;
            ratioAvg[i] = ratioTotals[i] / iterations;
         }

         XYSeries series = newSeries(detector, masterLine, ratioAvg);
         dataset.addSeries(series);
      }

      return dataset;
      //ChartUtils.createChart(dataset);
      /*
      for(String detector : mainTable.keySet()) {
         List<TcData> tcData = mainTable.get(detector);
      }
       *
       */
   }

   private XYSeries newSeries(String detector, List<Integer> masterLine, double[] ratioAvg) {
      XYSeries series = new XYSeries(detector);
      for(int i=0; i<masterLine.size(); i++) {
         series.add((double)masterLine.get(i), ratioAvg[i]*100);
      }

      return series;
   }

   /*
   private String getName(LoopDetectionInfo loopInfo) {

      loopInfo.detectorSetup;
   }
    *
    */

     

   private Map<String, List<TcData>> mainTable;
   private int maxRep;
   private long maxBlockSize;
   private List<String> keys;






}
