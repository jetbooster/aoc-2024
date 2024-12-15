package org.thermoweb.aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

class ChineseRemainderElement {
  long offset;
  long modulus;
}

public class ChineeseRemainderSolver {
  public long gcd(long a, long b) {
    if (b == 0)
      return a;
    return gcd(a, a % b);
  }

  AtomicLong multedModulos = new AtomicLong(1);

  List<ChineseRemainderElement> list = new ArrayList<>();

  ChineeseRemainderSolver(ChineseRemainderElement... elem) {

    Arrays.stream(elem).forEach((e) -> {
      multedModulos.getAndUpdate((curr) -> curr * e.modulus);
    });

  }

  // i gave up
}
