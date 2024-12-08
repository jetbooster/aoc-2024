package org.thermoweb.aoc.utils;

public class TerminalUtils {
  public enum Colour {
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m");

    String code;

    Colour(String code) {
      this.code = code;
    }

    @Override
    public String toString() {
      return this.code;
    }
  }

  TerminalUtils() {
  };

  public static String printWithColour(String s, Colour c) {
    return "%s%s%s".formatted(c, s, Colour.ANSI_RESET);
  }
}
