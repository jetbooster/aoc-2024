package org.thermoweb.aoc.utils;

import java.util.Objects;

public class Element extends Position {
  String content;

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

  @Override
  public int hashCode() {
    return Objects.hash(this.getX(), this.getY(), content);
  }
}
