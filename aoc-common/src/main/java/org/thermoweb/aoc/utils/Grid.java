package org.thermoweb.aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid<T extends Element> {
  String[][] rawElements;
  Map<Position, T> elements = new HashMap<Position, T>();
  public int width;
  public int height;
  public Class<T> clazz;

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
        elements.put(new Position(i, j), createElem(clazz, i, j, rawElements[j][i]));
      }
    }
  }

  public Grid(int width, int height, String fill, Class<T> clazz) {
    this.width = width;
    this.height = height;
    this.clazz = clazz;
    this.rawElements = IntStream.range(0, width).mapToObj((i) -> {
      return IntStream.range(0, height).mapToObj((j) -> {
        elements.put(new Position(i, j), createElem(clazz, i, j, fill));
        return ".";
      }).toArray(String[]::new);
    }).toArray(String[][]::new);
  }

  public Stream<Stream<T>> rows() {
    Stream.Builder<Stream<T>> ss = Stream.builder();
    for (int j = 0; j < this.height; j++) {
      Stream.Builder<T> s = Stream.builder();
      for (int i = 0; i < this.width; i++) {
        this.get(i, j).ifPresent(s::add);
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
        this.get(i, j).ifPresent(s::add);
      }
      ss.add(s.build());
    }
    return ss.build();
  }

  public void forEach(Consumer<T> func) {
    this.elements.values().stream().forEach(func);
  }

  public Stream<Object> map(Function<T, Object> func) {
    return this.elements.values().stream().map(func);
  }

  public Stream<T> stream() {
    return this.elements.values().stream();
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

  public Optional<T> get(Position p) {
    return Optional.ofNullable(this.elements.get(p));
  }

  public Optional<T> get(int x, int y) {
    return this.get(new Position(x, y));
  }

  public void set(int x, int y, T elem) {
    this.rawElements[y][x] = elem.getContent();
    this.set(new Position(x, y), elem);
  }

  public void set(Position p, T elem) {
    this.rawElements[p.getY()][p.getX()] = elem.getContent();
    elem.setX(p.getX());
    elem.setY(p.getY());
    this.elements.put(new Position(p.getX(), p.getY()), elem);
  }

  public Optional<T> neighbour(T e, int xOffset, int yOffset) {
    return this.get(e.getX() + xOffset, e.getY() + yOffset);
  }

  public Optional<T> neighbour(T e, Direction d) {
    return this.neighbour(e, d, 1);
  }

  public Optional<T> neighbour(T e, Direction d, int range) {
    return this.get(e.getX() + d.x * range, e.getY() + d.y * range);
  }

  public Stream<T> neighbours(T e) {
    return this.neighbours(e, false, 1);
  }

  public Stream<T> neighbours(T e, boolean diagonal) {
    return this.neighbours(e, diagonal, 1);
  }

  public Stream<T> neighbours(T e, boolean diagonal, int range) {
    return this.neighbours(e, diagonal, range, false);
  }

  public Stream<T> neighbours(T e, boolean diagonal, int range, boolean includeNulls) {
    List<T> results = new ArrayList<>();
    for (Direction d : Direction.values()) {
      for (int i = 1; i <= range; i++) {
        if (!diagonal && ((d.x + d.y) % 2 == 0)) {
          continue;
        }
        var val = this.get(e.getX() + d.x * i, e.getY() + d.y * i);
        if (val.isPresent()) {
          results.add(val.get());
        } else if (includeNulls) {
          results.add(null);
        } else {
          continue;
        }
      }
    }
    return results.stream();
  }

  public Optional<T> getElementAtOffset(T element, Position p) {
    return this.get(element.getLocationAtOffset(p));
  }

  public void moveElement(Position from, Position to, Function<T, T> mutation, T replacementElement) {
    var fromElem = this.get(from).get();
    var toElem = mutation.apply(fromElem);
    this.set(to, toElem);
    this.set(from, replacementElement);
  }

  public void moveElement(Position from, Position to, Function<T, T> mutation) {
    this.moveElement(from, to, mutation, createElem(clazz, from.getX(), to.getY(), "."));
  }

  public void moveElement(Position from, Position to) {
    this.moveElement(from, to, (e) -> e, createElem(clazz, from.getX(), to.getY(), "."));
  }

  public void swap(T a, T b) {
    Position temp = new Position(a.getX(), a.getY());
    this.set(b, a);
    this.set(temp, b);
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
