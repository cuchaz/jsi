//   Script.java
//   Java Spatial Index Library
//   Copyright (C) 2002-2005 Infomatiq Limited.
//  
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//  
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

package net.sf.jsi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Script
 */
public class Script {
  
  private static final Logger log = LoggerFactory.getLogger(Script.class);

  static final int REFERENCE_COMPARISON = 1;
  static final int REFERENCE_GENERATE = 2;
  
  private int canvasSize = 100000;

  private void writeOutput(String outputLine, PrintWriter outputFile, LineNumberReader referenceFile) {
   try {
      outputFile.println(outputLine);
      outputFile.flush();
      if (referenceFile != null) { 
        String referenceLine = referenceFile.readLine();
        if (!outputLine.equals(referenceLine)) {
          log.error("Output does not match reference on line " + referenceFile.getLineNumber());
          log.error(" Reference result: " + referenceLine);
          log.error(" Test result:      " + outputLine);
          TestCase.assertTrue("Output does not match reference on line " + referenceFile.getLineNumber(), false);
        }
      }
    }  catch (IOException e) {
       log.error("IOException while writing test results");
    }  
  }
  
  private int quantize(double d, int quantizer) {
    if (quantizer <= 0) {
      return (int)Math.round(d);
    }
    
    d /= quantizer;
    int i = (int)Math.round(d);
    i *= quantizer;
    
    return i;
  }
  
  private Rectangle getRandomRectangle(Random r, int rectangleSize, int canvasSize, int quantizer) {
    int x1 = quantize(r.nextGaussian() * canvasSize, quantizer);
    int y1 = quantize(r.nextGaussian() * canvasSize, quantizer);
    int x2 = x1 + quantize(r.nextGaussian() * rectangleSize, quantizer);
    int y2 = y1 + quantize(r.nextGaussian() * rectangleSize, quantizer);
    
    return new Rectangle(x1, y1, x2, y2);
  }
              
  /**
   * @return Time taken to execute method, in milliseconds.
   */
  public long run(String indexType, Properties indexProperties, String testId, int testType) {
    if (log.isInfoEnabled()) {
      log.info("runScript: " + indexType + ", testId=" + testId);
      if (indexProperties != null) {
        log.info("minEntries=" + indexProperties.getProperty("MinNodeEntries") +
              ", maxEntries=" + indexProperties.getProperty("MaxNodeEntries") +
              ", treeVariant=" + indexProperties.getProperty("TreeVariant"));
      }
    }
    
    SpatialIndex si = SpatialIndexFactory.newInstance(indexType, indexProperties);
    
    ListDecorator ld = new SortedListDecorator(si);
    
    Random random = new Random();
    DecimalFormat df = new DecimalFormat();
    df.setMinimumFractionDigits(0);
    df.setMaximumFractionDigits(0);
    df.setMinimumIntegerDigits(7);
    df.setMaximumIntegerDigits(7);
    df.setPositivePrefix(" ");
    df.setGroupingUsed(false);
    
    int quantizer = -1;
 
    String strTestInputRoot = "/test-inputs" + File.separator + "test-" + testId;
    
    String strTestResultsRoot = "target/test-results" + File.separator + "test-" + testId;
    
    // open test input file for read-only
    LineNumberReader inputFile = null;
    String inputFilename = strTestInputRoot + "-in";
    
    try {
      inputFile = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream(inputFilename)));          
    }
    catch (Throwable t) {
      log.error("Unable to open test input file " + inputFilename);
      TestCase.assertTrue("Unable to open test input file " + inputFilename, false);
      return -1;
    }
    
    // open reference results file for read-only. Filename is of form:
    // test-testId-reference
    LineNumberReader referenceFile = null;
    if (testType == REFERENCE_COMPARISON) {
      String referenceFilename = strTestResultsRoot + "-reference";
      try {
        referenceFile = new LineNumberReader(new InputStreamReader(new FileInputStream(referenceFilename)));
      } catch (FileNotFoundException e) {
        log.error("Unable to open reference test results file " + referenceFilename);
        TestCase.assertTrue("Unable to open reference test results file " + referenceFilename, false);
        return -1;
      }
    }
   
    // open actual results file for writing. Filename is of form
    // test-testId-indexType-revision-datetime, unless generating reference results.
    PrintWriter outputFile = null;

    String outputFilename = null;
    if (testType == REFERENCE_COMPARISON) {
      outputFilename = strTestResultsRoot + "-" + si.getVersion() +
      	"-" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    } else {
      outputFilename = strTestResultsRoot + "-reference";
      if (new File(outputFilename).exists()) {
          log.info("Reusing existing reference file: " + outputFilename);
          return 0;
      }
    }
       
    new File(outputFilename).getParentFile().mkdirs();
      
    try {
      outputFile = new PrintWriter(new FileOutputStream(outputFilename));
    } catch (FileNotFoundException e) {
      log.error("Unable to open test output results file " + outputFilename);
      TestCase.assertTrue("Unable to open test output results file " + outputFilename, false);
      return -1;
    }
    
    long scriptStartTime = System.currentTimeMillis();
    
    try {
      // read lines from the test input file
      while (inputFile.ready()) {
        String inputLine = inputFile.readLine();
        
        if (inputLine.startsWith("#")) {
          continue; 
        }
        
        StringBuffer outputBuffer = new StringBuffer(inputLine);
            
        StringTokenizer st = new StringTokenizer(inputLine);
        while (st.hasMoreTokens()) {
          String operation = st.nextToken().toUpperCase();
          if (operation.equals("DISTANCEQUANTIZER")) {
            quantizer = Integer.parseInt(st.nextToken());
          } else if (operation.equals("RANDOMIZE")) {
            random.setSeed(Integer.parseInt(st.nextToken()));
            writeOutput(outputBuffer.toString() + " : OK", outputFile, referenceFile);
          } else if (operation.equals("ADDRANDOM")) {
            int count = Integer.parseInt(st.nextToken());
            int startId = Integer.parseInt(st.nextToken());
            int rectangleSize = Integer.parseInt(st.nextToken());
            
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);
            
            long startTime = System.currentTimeMillis();
               
            for (int id = startId; id < startId + count; id++) {
              Rectangle r = getRandomRectangle(random, rectangleSize, canvasSize, quantizer);
              si.add(r, id);
            
              String outputLine = "  " + id + " " + r.toString() + " : OK";
              writeOutput(outputLine, outputFile, referenceFile);
            }
            long time = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
              log.debug("Added " + count + " entries in " + time +  "ms (" + time / (float) count + " ms per add)");
            }
          } else if (operation.equals("DELETERANDOM")) {
            int count = Integer.parseInt(st.nextToken());
            int startId = Integer.parseInt(st.nextToken());
            int rectangleSize = Integer.parseInt(st.nextToken());
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);

            long startTime = System.currentTimeMillis();
            
            int successfulDeleteCount = 0;   
            for (int id = startId; id < startId + count; id++) {
              Rectangle r = getRandomRectangle(random, rectangleSize, canvasSize, quantizer);
              boolean deleted = si.delete(r, id);
             
              if (deleted) {
                successfulDeleteCount++;
              }
              
              String outputLine = "  " + id + " " + r.toString() + " : " + deleted;
              writeOutput(outputLine, outputFile, referenceFile);
            }
            long time = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
              log.debug("Attempted to delete " + count + " entries (" + successfulDeleteCount + " successful) in " + time +  "ms (" + time / (float) count + " ms per delete)");
            }
          } 
          else if (operation.equals("NEARESTRANDOM")) {
            int queryCount = Integer.parseInt(st.nextToken());
            
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);

            long startTime = System.currentTimeMillis();
            int totalEntriesReturned = 0;
            
            for (int id = 0; id < queryCount; id++) {
              int x = (int)Math.round(random.nextGaussian() * canvasSize);
              int y = (int)Math.round(random.nextGaussian() * canvasSize);
              
              List<Integer> l = ld.nearest(new Point(x, y), Float.POSITIVE_INFINITY);
              totalEntriesReturned += l.size();
              
              StringBuffer tempBuffer = new StringBuffer("  " + id + " " +
                                                     df.format(x) + " " +
                                                     df.format(y) + " : OK");
  
              Iterator<Integer> i = l.iterator();
              while (i.hasNext()) {
                tempBuffer.append(' ');
                tempBuffer.append(i.next()).toString();
              }
              writeOutput(tempBuffer.toString(), outputFile, referenceFile);
            } 
            long time = System.currentTimeMillis() - startTime;
            if (log.isInfoEnabled()) {
              log.info("NearestQueried " + queryCount + " times in " + time + "ms. Per query: " + time / (float) queryCount + " ms, " + (totalEntriesReturned / (float) queryCount) + " entries");
            }
          } else if (operation.equals("NEARESTNRANDOM")) {
            int queryCount = Integer.parseInt(st.nextToken());
            int n = Integer.parseInt(st.nextToken());
            
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);

            long startTime = System.currentTimeMillis();
            int totalEntriesReturned = 0;
            
            for (int id = 0; id < queryCount; id++) {
              int x = (int)Math.round(random.nextGaussian() * canvasSize);
              int y = (int)Math.round(random.nextGaussian() * canvasSize);
              
              List<Integer> l = ld.nearestN(new Point(x, y), n, Float.POSITIVE_INFINITY);
              
              totalEntriesReturned += l.size();
              
              StringBuffer tempBuffer = new StringBuffer("  " + id + " " +
                                                     df.format(x) + " " +
                                                     df.format(y) + " : OK");
  
              Iterator<Integer> i = l.iterator();
              while (i.hasNext()) {
                tempBuffer.append(' ');
                tempBuffer.append(i.next()).toString();
              }

              writeOutput(tempBuffer.toString(), outputFile, referenceFile);
            } 
            long time = System.currentTimeMillis() - startTime;
            if (log.isInfoEnabled()) {
              log.info("NearestNQueried " + queryCount + " times in " + time + "ms. Per query: " + time / (float) queryCount + " ms, " + (totalEntriesReturned / (float) queryCount) + " entries");
            }
          } else if (operation.equals("INTERSECTRANDOM")) {
            int queryCount = Integer.parseInt(st.nextToken());
            int rectangleSize = Integer.parseInt(st.nextToken());
            
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);

            long startTime = System.currentTimeMillis();
            int totalEntriesReturned = 0;
            
            for (int id = 0; id < queryCount; id++) {
              Rectangle r = getRandomRectangle(random, rectangleSize, canvasSize, quantizer);
              List<Integer> l = ld.intersects(r);
              totalEntriesReturned += l.size();
              
              Iterator<Integer> i = l.iterator();
              StringBuffer tempBuffer = new StringBuffer("  " + id + " " + r.toString() + " : OK");

              while (i.hasNext()) {
                tempBuffer.append(' ');
                tempBuffer.append(i.next()).toString();
              }
              writeOutput(tempBuffer.toString(), outputFile, referenceFile);
            }
            long time = System.currentTimeMillis() - startTime;
            if (log.isInfoEnabled()) {
              log.info("IntersectQueried " + queryCount + " times in " + time + "ms. Per query: " + time / (float) queryCount + " ms, " + (totalEntriesReturned / (float) queryCount) + " entries");
            }
          } 
          else if (operation.equals("CONTAINSRANDOM")) {
            int queryCount = Integer.parseInt(st.nextToken());
            int rectangleSize = Integer.parseInt(st.nextToken());
            
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);

            long startTime = System.currentTimeMillis();
            int totalEntriesReturned = 0;
            
            for (int id = 0; id < queryCount; id++) {
              Rectangle r = getRandomRectangle(random, rectangleSize, canvasSize, quantizer);
              List<Integer> l = ld.contains(r);
              totalEntriesReturned += l.size();
              
              Iterator<Integer> i = l.iterator();
              StringBuffer tempBuffer = new StringBuffer("  " + id + " " + r.toString() + " : OK");

              while (i.hasNext()) {
                tempBuffer.append(' ');
                tempBuffer.append((Integer)i.next()).toString();
              }
              writeOutput(tempBuffer.toString(), outputFile, referenceFile);
            }
            long time = System.currentTimeMillis() - startTime;
            if (log.isInfoEnabled()) {
              log.info("ContainsQueried " + queryCount + " times in " + time + "ms. Per query: " + time / (float) queryCount + " ms, " + (totalEntriesReturned / (float) queryCount) + " entries");
            }
          } 
          else if (operation.equals("ADD")) {
            int id = Integer.parseInt(st.nextToken());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
             
            si.add(new Rectangle(x1, y1, x2, y2), id);
             
            outputBuffer.append(" : OK");
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);
          } 
          else if (operation.equals("DELETE")) {
            int id = Integer.parseInt(st.nextToken());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
             
            boolean deleted = si.delete(new Rectangle(x1, y1, x2, y2), id);
             
            if (deleted) {
              outputBuffer.append(" : OK");
            } else {
              outputBuffer.append(" : Not found");
            }
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);
          } 
          else if (operation.equals("NEAREST")) {
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
             
            List<Integer> l = ld.nearest(new Point(x, y), Float.POSITIVE_INFINITY);
            
            outputBuffer.append(" : OK");

            Iterator<Integer> i = l.iterator();
            while (i.hasNext()) {
              outputBuffer.append(" ");
              outputBuffer.append((Integer)i.next()).toString();
            }
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);
          } 
          else if (operation.equals("INTERSECT")) {
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            
            List<Integer> l = ld.intersects(new Rectangle(x1, y1, x2, y2));
            
            outputBuffer.append(" : OK");
             
            Iterator<Integer> i = l.iterator();
            while (i.hasNext()) {
              outputBuffer.append(" ");
              outputBuffer.append(i.next()).toString();
            }
            writeOutput(outputBuffer.toString(), outputFile, referenceFile);
          }
        } // for each token on the current input line
      } // for each input line
    } catch (IOException e) {
      log.error("IOException while running test script in SpatialIndexTest", e); 
      return -1;
    } 
    long scriptEndTime = System.currentTimeMillis();
    
    // try and clean up the largest objects to prevent garbage collection 
    // from slowing down a future run.
    ld = null;
    si = null; 
    System.gc();
    
    return scriptEndTime - scriptStartTime;
  }
}
