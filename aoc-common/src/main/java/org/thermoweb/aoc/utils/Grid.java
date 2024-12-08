package org.thermoweb.aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid<T extends Element> {
  String[][] rawElements;
  Map<String, T> elements = new HashMap<String, T>();
  public int width;
  public int height;

  @SuppressWarnings("unchecked")
  T createElem(Class<T> clazz, Object... initArgs) {
    try {
      var constructor = clazz.getDeclaredConstructor(new Class[] { int.class, int.class, String.class });
      constructor.setAccessible(true);
      return constructor.newInstance(initArgs);

    } catch (Exception e) {
      return (T) new Element((int) initArgs[0], (int) initArgs[1], (String) initArgs[2]);
    }

  }

  public Grid(String input, Class<T> clazz) {
    String[] rows = input.split(System.lineSeparator());
    this.height = rows.length;
    this.rawElements = Arrays.stream(rows).map((row) -> {
      var elems = row.split("");
      this.width = elems.length;
      return elems;
    }).collect(Collectors.toList()).toArray(String[][]::new);

    for (int j = 0; j < this.height; j++) {
      for (int i = 0; i < this.width; i++) {
        elements.put(i + "," + j, createElem(clazz, i, j, rawElements[j][i]));
      }
    }
  }

  public Stream<Stream<T>> rows() {
    Stream.Builder<Stream<T>> ss = Stream.builder();
    for (int j = 0; j < this.height; j++) {
      Stream.Builder<T> s = Stream.builder();
      for (int i = 0; i < this.width; i++) {
        s.add(this.get(i, j));
      }
      ss.add(s.build());
    }
    return ss.build();
  }

  public Stream<Stream<T>> columns() {
    Stream.Builder<Stream<T>> ss = Stream.builder();
    for (int i = 0; i < this.width; i++) {
      Stream.Builder<T> s = Stream.builder();
      for (int j = 0; j < this.height; j++) {
        s.add(this.get(i, j));
      }
      ss.add(s.build());
    }
    return ss.build();
  }

  public Optional<T> find(String content) {
    Predicate<T> p = (elem) -> {
      return elem.getContent().equals(content);
    };
    return this.find(p);
  }

  public Optional<T> find(Pattern pattern) {
    var pred = pattern.asMatchPredicate();
    Predicate<T> p = (elem) -> {
      return pred.test(elem.getContent());
    };
    return this.find(p);
  }

  public Optional<T> find(Predicate<T> p) {
    return this.elements.values().stream().filter(p).findFirst();
  }

  public Stream<T> filter(String content) {
    Predicate<T> p = (elem) -> {
      return elem.getContent().equals(content);
    };
    return this.filter(p);
  }

  public Stream<T> filter(Predicate<T> p) {
    return this.elements.values().stream().filter(p);
  }

  public T get(Position p) {
    return this.elements.get(x + "," + y);
  }

  public T get(int x, int y) {
    return this.elements.get(x + "," + y);
  }

  public T neighbour(T e, int xOffset, int yOffset) {
    return this.get(e.getX() + xOffset, e.getY() + yOffset);
  }

  public T neighbour(T e, Direction d) {
    return this.neighbour(e, d, 1);
  }

  public T neighbour(T e, Direction d, int range) {
    return this.get(e.getX() + d.x * range, e.getY() + d.y * range);
  }

  public Stream<T> neighbours(T e) {
    return this.neighbours(e, false, 1);
  }

  public Stream<T> neighbours(T e, boolean diagonal) {
    return this.neighbours(e, diagonal, 1);
  }

  public Stream<T> neighbours(T e, boolean diagonal, int range) {
    List<T> results = new ArrayList<>();
    for (Direction d : Direction.values()) {
      for (int i = 1; i <= range; i++) {
        if (!diagonal && ((d.x + d.y) % 2 == 0)) {
          continue;
        }
        T potentialElem = elements.get(String.format("%s,%s", e.getX() + d.x * i, e.getY() + d.y * i));
        if (potentialElem != null) {
          results.add(potentialElem);
        }
      }
    }
    return results.stream();
  }

  Optional<T> getElementAtOffset(T element, Position p){
    this.get(element.getLocationAtOffset(p))
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (String[] row : rawElements) {
      for (String element : row) {
        s.append(element);
      }
      s.append("\n");
    }
    return s.toString();
  }

  public String toString(Function<T, String> extractor) {
    StringBuilder s = new StringBuilder();
    this.rows().forEach(row -> {
      row.forEach(elem -> {
        s.append(extractor.apply(elem));
      });
      s.append("\n");
    });
    return s.toString();
  }

}
