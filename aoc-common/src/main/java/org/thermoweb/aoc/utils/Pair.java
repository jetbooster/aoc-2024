package org.thermoweb.aoc.utils;

import java.util.Objects;

public record Pair<K, V>(K a, V b) {
  @Override
  public final int hashCode() {
    // TODO Auto-generated method stub
    return Objects.hash(a, b);
  }

  @Override
  public final boolean equals(Object arg0) {
    if (!(arg0 instanceof Pair<?, ?>)) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) arg0;

    if (this.a.getClass() != pair.a.getClass() || this.b.getClass() != pair.b.getClass()) {
      return false;
    }

    return (this.a.equals(pair.a) && this.b.equals(pair.b));
  }

}
