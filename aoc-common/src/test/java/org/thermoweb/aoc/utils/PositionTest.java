package org.thermoweb.aoc.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PositionTest {
  @Test
  void testPositionString() {
    Assert.assertEquals(new Position(5, 10).toString(), "(5,10)");
  }

  @Test
  void testGetDirectionTo() {
    var a = new Position(5, 6);
    var b = new Position(8, 4);
    Assert.assertEquals(a.getDirectionTo(b), new Position(3, -2));
  }
}
