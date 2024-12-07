package org.thermoweb.aoc.utils;

import java.util.Arrays;
import java.util.stream.Stream;

public class InputParseUtils {
  public static Stream<String> lines(String string) {
    return Arrays.stream(string.split(System.lineSeparator())).filter((line) -> line.length() != 0);
  }
}
