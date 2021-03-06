package net.sf.jsi;

import junit.framework.TestCase;

import net.sf.jsi.Rectangle;
import net.sf.jsi.rtree.RTree;

public class RectangleMaxValueTest extends TestCase {
  public RectangleMaxValueTest(String name) {
    super(name);
  }

  public void testMaxValue() {
    RTree rTree = new RTree();
    rTree.init(null);
    rTree.add(new Rectangle(8, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 1);
    rTree.add(new Rectangle(1, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 2);
    rTree.add(new Rectangle(10, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 3);
    rTree.add(new Rectangle(5, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 4);
    rTree.add(new Rectangle(6, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 6);
    rTree.add(new Rectangle(3, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 7);
    rTree.add(new Rectangle(9, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 8);
    rTree.add(new Rectangle(5, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 9);
    rTree.add(new Rectangle(2, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 10);
    rTree.add(new Rectangle(2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 11);
    rTree.add(new Rectangle(5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 14);
    rTree.add(new Rectangle(7, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 15);
    rTree.add(new Rectangle(6, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 16);
    rTree.delete(new Rectangle(9, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 8);
    rTree.add(new Rectangle(7, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 17);
    rTree.add(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 18);
    rTree.delete(new Rectangle(5, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 9);
    rTree.add(new Rectangle(4, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 19);
    rTree.delete(new Rectangle(2, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 10);
    rTree.add(new Rectangle(2, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 20);
    rTree.add(new Rectangle(9, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 21);
    rTree.add(new Rectangle(7, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 22);
    rTree.delete(new Rectangle(10, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 3);
    rTree.add(new Rectangle(3, 4, Integer.MAX_VALUE, Integer.MAX_VALUE), 23);
    rTree.delete(new Rectangle(3, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 7);
    rTree.add(new Rectangle(3, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 24);
    rTree.delete(new Rectangle(8, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 1);
    rTree.add(new Rectangle(3, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 25);
    rTree.delete(new Rectangle(7, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 17);
    rTree.add(new Rectangle(7, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 26);
    rTree.delete(new Rectangle(2, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 20);
    rTree.add(new Rectangle(0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 27);
    rTree.delete(new Rectangle(2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 11);
    rTree.add(new Rectangle(2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 28);
    rTree.delete(new Rectangle(5, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 4);
    rTree.add(new Rectangle(4, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 29);
    rTree.delete(new Rectangle(5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 14);
    rTree.add(new Rectangle(5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 30);
    rTree.add(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 31);
    rTree.delete(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 31);
    rTree.add(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 32);
    rTree.add(new Rectangle(5, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 33);
    rTree.delete(new Rectangle(2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 28);
    rTree.add(new Rectangle(1, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 34);
    rTree.delete(new Rectangle(0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 27);
    rTree.add(new Rectangle(0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 35);
    rTree.add(new Rectangle(8, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 36);
    rTree.delete(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 18);
    rTree.add(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 37);
    rTree.add(new Rectangle(8, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 39);
    rTree.delete(new Rectangle(7, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 22);
    rTree.add(new Rectangle(5, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 40);
    rTree.delete(new Rectangle(3, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 24);
    rTree.add(new Rectangle(3, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), 41);
    rTree.delete(new Rectangle(7, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 26);
    rTree.add(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 42);
    rTree.delete(new Rectangle(7, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 15);
    rTree.add(new Rectangle(3, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 43);
    rTree.delete(new Rectangle(4, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 29);
    rTree.add(new Rectangle(4, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 44);
    rTree.delete(new Rectangle(3, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 25);
    rTree.add(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 45);
    rTree.delete(new Rectangle(3, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 43);
    rTree.add(new Rectangle(1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), 46);
    rTree.delete(new Rectangle(1, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 2);
    rTree.add(new Rectangle(1, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 47);
    rTree.delete(new Rectangle(9, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 21);
    rTree.add(new Rectangle(7, 6, Integer.MAX_VALUE, Integer.MAX_VALUE), 48);
    rTree.delete(new Rectangle(4, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 19);
    rTree.add(new Rectangle(4, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), 49);
    rTree.delete(new Rectangle(5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 30);
    rTree.add(new Rectangle(0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 50);
    rTree.delete(new Rectangle(5, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 33);
    rTree.add(new Rectangle(4, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 51);
    rTree.delete(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 45);
    rTree.add(new Rectangle(3, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 52);
    rTree.delete(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 42);
    rTree.add(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 53);
    rTree.delete(new Rectangle(5, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 40);
    rTree.add(new Rectangle(3, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 54);
    rTree.delete(new Rectangle(6, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 6);
    rTree.add(new Rectangle(6, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 55);
    rTree.delete(new Rectangle(4, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 51);
    rTree.add(new Rectangle(4, 8, Integer.MAX_VALUE, Integer.MAX_VALUE), 56);
    rTree.delete(new Rectangle(1, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 47);
    rTree.add(new Rectangle(1, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 57);
    rTree.delete(new Rectangle(0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 50);
    rTree.add(new Rectangle(0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 58);
    rTree.delete(new Rectangle(3, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), 41);
    rTree.add(new Rectangle(3, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), 59);
    rTree.delete(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 53);
    rTree.add(new Rectangle(0, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 60);
    rTree.delete(new Rectangle(6, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 55);
    rTree.add(new Rectangle(2, 1, Integer.MAX_VALUE, Integer.MAX_VALUE), 61);
    rTree.delete(new Rectangle(7, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 32);
    rTree.add(new Rectangle(0, 5, Integer.MAX_VALUE, Integer.MAX_VALUE), 62);
    rTree.delete(new Rectangle(0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 58);
    rTree.add(new Rectangle(0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE), 63);
    rTree.delete(new Rectangle(3, 4, Integer.MAX_VALUE, Integer.MAX_VALUE), 23);
    rTree.add(new Rectangle(3, 3, Integer.MAX_VALUE, Integer.MAX_VALUE), 64);
    rTree.delete(new Rectangle(8, 7, Integer.MAX_VALUE, Integer.MAX_VALUE), 39);
  }
}
