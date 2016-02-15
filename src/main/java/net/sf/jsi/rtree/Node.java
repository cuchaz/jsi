//   Node.java
//   Java Spatial Index Library
//   Copyright (C) 2002-2005 Infomatiq Limited
//   Copyright (C) 2008-2010 aled@users.sourceforge.net
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
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package net.sf.jsi.rtree;

import java.io.Serializable;

/**
 * <p>Used by RTree. There are no public methods in this class.</p>
 */
public class Node implements Serializable {
  private static final long serialVersionUID = -2823316966528817396L;
  int nodeId = 0;
  int mbrMinX = Integer.MAX_VALUE;
  int mbrMinY = Integer.MAX_VALUE;
  int mbrMaxX = -Integer.MAX_VALUE;
  int mbrMaxY = -Integer.MAX_VALUE;
  
  int[] entriesMinX = null;
  int[] entriesMinY = null;
  int[] entriesMaxX = null;
  int[] entriesMaxY = null;
  
  int[] ids = null;
  int level;
  int entryCount;

  Node(int nodeId, int level, int maxNodeEntries) {
    this.nodeId = nodeId;
    this.level = level;
    entriesMinX = new int[maxNodeEntries];
    entriesMinY = new int[maxNodeEntries];
    entriesMaxX = new int[maxNodeEntries];
    entriesMaxY = new int[maxNodeEntries];
    ids = new int[maxNodeEntries];
  }
   
  void addEntry(int minX, int minY, int maxX, int maxY, int id) {
    ids[entryCount] = id;
    entriesMinX[entryCount] = minX;
    entriesMinY[entryCount] = minY;
    entriesMaxX[entryCount] = maxX;
    entriesMaxY[entryCount] = maxY;
   
    if (minX < mbrMinX) mbrMinX = minX;
    if (minY < mbrMinY) mbrMinY = minY;
    if (maxX > mbrMaxX) mbrMaxX = maxX;
    if (maxY > mbrMaxY) mbrMaxY = maxY;
    
    entryCount++;
  }
  
  // Return the index of the found entry, or -1 if not found
  int findEntry(int minX, int minY, int maxX, int maxY, int id) {
    for (int i = 0; i < entryCount; i++) {
    	if (id == ids[i] && 
          entriesMinX[i] == minX && entriesMinY[i] == minY &&
          entriesMaxX[i] == maxX && entriesMaxY[i] == maxY) {
    	  return i;	
    	}
    }
    return -1;
  }
  
  // delete entry. This is done by setting it to null and copying the last entry into its space.
  void deleteEntry(int i) {
	  int lastIndex = entryCount - 1;
    int deletedMinX = entriesMinX[i];
    int deletedMinY = entriesMinY[i];
    int deletedMaxX = entriesMaxX[i];
    int deletedMaxY = entriesMaxY[i];
    
    if (i != lastIndex) {
      entriesMinX[i] = entriesMinX[lastIndex];
      entriesMinY[i] = entriesMinY[lastIndex];
      entriesMaxX[i] = entriesMaxX[lastIndex];
      entriesMaxY[i] = entriesMaxY[lastIndex];
    	ids[i] = ids[lastIndex];
	  }
    entryCount--;
    
    // adjust the MBR
    recalculateMBRIfInfluencedBy(deletedMinX, deletedMinY, deletedMaxX, deletedMaxY);
  } 
  
  // deletedMin/MaxX/Y is a rectangle that has just been deleted or made smaller.
  // Thus, the MBR is only recalculated if the deleted rectangle influenced the old MBR
  void recalculateMBRIfInfluencedBy(int deletedMinX, int deletedMinY, int deletedMaxX, int deletedMaxY) {
    if (mbrMinX == deletedMinX || mbrMinY == deletedMinY || mbrMaxX == deletedMaxX || mbrMaxY == deletedMaxY) { 
      recalculateMBR();   
    }
  }
   
  void recalculateMBR() {
    mbrMinX = entriesMinX[0];
    mbrMinY = entriesMinY[0];
    mbrMaxX = entriesMaxX[0];
    mbrMaxY = entriesMaxY[0];

    for (int i = 1; i < entryCount; i++) {
      if (entriesMinX[i] < mbrMinX) mbrMinX = entriesMinX[i];
      if (entriesMinY[i] < mbrMinY) mbrMinY = entriesMinY[i];
      if (entriesMaxX[i] > mbrMaxX) mbrMaxX = entriesMaxX[i];
      if (entriesMaxY[i] > mbrMaxY) mbrMaxY = entriesMaxY[i];
    }
  }
    
  /**
   * eliminate null entries, move all entries to the start of the source node
   */
  void reorganize(RTree rtree) {
    int countdownIndex = rtree.maxNodeEntries - 1; 
    for (int index = 0; index < entryCount; index++) {
      if (ids[index] == -1) {
         while (ids[countdownIndex] == -1 && countdownIndex > index) {
           countdownIndex--;
         }
         entriesMinX[index] = entriesMinX[countdownIndex];
         entriesMinY[index] = entriesMinY[countdownIndex];
         entriesMaxX[index] = entriesMaxX[countdownIndex];
         entriesMaxY[index] = entriesMaxY[countdownIndex];
         ids[index] = ids[countdownIndex];    
         ids[countdownIndex] = -1;
      }
    }
  }
  
  public int getEntryCount() {
    return entryCount;
  }
 
  public int getId(int index) {
    if (index < entryCount) {
      return ids[index];
    }
    return -1;
  }
  
  boolean isLeaf() {
    return (level == 1);
  }
  
  public int getLevel() {
    return level; 
  }
}
