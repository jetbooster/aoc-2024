package org.thermoweb.aoc.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GridTest {

  Grid<Element> grid;

  @BeforeClass
  public void setup() throws IOException, URISyntaxException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    var input = Files.readString(Path.of(ClassLoader.getSystemResource("grid.txt").toURI()));
    this.grid = new Grid<Element>(input, Element.class);
  }

  @Test
  public void testGridParsing() {
    Assert.assertEquals(grid.elements.size(), 18 * 5);
    Assert.assertEquals(grid.get(5, 0), Optional.of(new Element(5, 0, "4")));
    Assert.assertEquals(grid.get(0, 4), Optional.of(new Element(0, 4, "3")));
    Assert.assertEquals(grid.get(17, 4), Optional.of(new Element(17, 4, "7")));
  }

  @Test
  public void testFiltering() {
    Assert.assertEquals(grid.filter("7").collect(Collectors.toList()).size(), 3);
    Assert.assertEquals(grid.filter(elem -> (elem.getX() % 2) == 0).collect(Collectors.toList()).size(), 45);

  }

}
