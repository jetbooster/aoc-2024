package org.thermoweb.aoc.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DirectionTest {

  @Test
  void testLeft() {
    Assert.assertEquals(Direction.N.turnLeft(), Direction.W);
    Assert.assertEquals(Direction.E.turnLeft(), Direction.N);
    Assert.assertEquals(Direction.NE.turnLeft(), Direction.NW);
    Assert.assertEquals(Direction.SW.turnLeft(), Direction.SE);
  }

  @Test
  void testRight() {
    Assert.assertEquals(Direction.N.turnRight(), Direction.E);
    Assert.assertEquals(Direction.W.turnRight(), Direction.N);
    Assert.assertEquals(Direction.NE.turnRight(), Direction.SE);
    Assert.assertEquals(Direction.SW.turnRight(), Direction.NW);
  }

  @Test
  void testReverse() {
    Assert.assertEquals(Direction.N.reverse(), Direction.S);
    Assert.assertEquals(Direction.W.reverse(), Direction.E);
    Assert.assertEquals(Direction.NE.reverse(), Direction.SW);
    Assert.assertEquals(Direction.SW.reverse(), Direction.NE);
  }

  @Test
  void testPrint() {
    Assert.assertEquals(Direction.N.toString(), "↑");
  }
}
