package org.thermoweb.aoc.utils;

public enum Direction {

  N(0, -1),
  NE(1, -1),
  E(1, 0),
  SE(1, 1),
  S(0, 1),
  SW(-1, 1),
  W(-1, 0),
  NW(-1, -1);

  int x;
  int y;

  Direction(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Direction[] diagonals() {
    return new Direction[] { Direction.NE, Direction.SE, Direction.SW, Direction.NW };
  }

  public static Direction[] compass() {
    return new Direction[] { Direction.N, Direction.E, Direction.S, Direction.W };
  }

  public Direction turnLeft() {
    return Direction.values()[Math.floorMod(this.ordinal() - 2, Direction.values().length)];
  }

  public Direction turnRight() {
    return Direction.values()[Math.floorMod(this.ordinal() + 2, Direction.values().length)];
  }

  public Direction reverse() {
    return Direction.values()[Math.floorMod(this.ordinal() + 4, Direction.values().length)];
  }

  public boolean isDiagonal() {
    return this.equals(Direction.NE)
        || this.equals(Direction.SE)
        || this.equals(Direction.SW)
        || this.equals(Direction.NW);
  }

  public boolean isCompass() {
    return this.equals(Direction.N)
        || this.equals(Direction.E)
        || this.equals(Direction.S)
        || this.equals(Direction.W);
  }

  @Override
  public String toString() {
    return String.valueOf("↑↗→↘↓↙←↖".charAt(this.ordinal()));
  }

  public static Direction convertToDirection(String s) {
    if (s.equals("^")) {
      return N;
    }
    if (s.equals(">")) {
      return E;
    }
    if (s.equals("v")) {
      return S;
    }
    if (s.equals("<")) {
      return W;
    }
    throw new IllegalArgumentException("unrecognised char %s".formatted(s));
  }
}
