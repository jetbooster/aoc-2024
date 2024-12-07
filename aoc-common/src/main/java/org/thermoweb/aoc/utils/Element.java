package org.thermoweb.aoc.utils;

public class Element {
  int x;
  int y;
  String content;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getContent() {
    return content;
  }

  public Element(int x, int y, String content) {
    this.x = x;
    this.y = y;
    this.content = content;
  }

  public Element(Integer x, Integer y, String content) {
    this.x = x.intValue();
    this.y = y.intValue();
    this.content = content;
  }
}
