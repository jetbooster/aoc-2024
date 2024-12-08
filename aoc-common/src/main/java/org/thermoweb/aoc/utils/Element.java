package org.thermoweb.aoc.utils;

public class Element extends Position {
  Position p;
  String content;

  public int getX() {
    return p.getX();
  }

  public int getY() {
    return p.getY();
  }

  public String getContent() {
    return content;
  }

  public Element(int x, int y, String content) {
    super(x, y);
    this.content = content;
  }

  @Override
  public String toString() {
    return "%s %s".formatted(super.toString(), this.content);
  }
}
