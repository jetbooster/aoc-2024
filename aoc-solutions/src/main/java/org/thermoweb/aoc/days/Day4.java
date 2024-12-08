package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Direction;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;

@DaySolver(4)
public class Day4 implements Day {

    List<String> sm = Arrays.asList("S", "M");

    @Override
    public Optional<BigInteger> partOne(String input) {
        try {
            Grid<Element> grid = new Grid<>(input, Element.class);
            int hits = grid.filter("X").mapToInt((elem) -> findXmas(grid, elem)).sum();
            return Optional.of(BigInteger.valueOf(hits));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        try {
            Grid<Element> grid = new Grid<>(input, Element.class);
            int hits = grid.filter("A").mapToInt((elem) -> findExMAS(grid, elem)).sum();
            return Optional.of(BigInteger.valueOf(hits));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    int findXmas(Grid<Element> g, Element e) {
        String searchString = "MAS";
        int hits = 0;
        for (Direction d : Direction.values()) {
            AtomicBoolean success = new AtomicBoolean(false);
            for (AtomicInteger range = new AtomicInteger(0); range.get() <= searchString.length(); range
                    .getAndIncrement()) {
                g.neighbour(e, d, range.get()).ifPresentOrElse((elem) -> {
                    if (!elem.getContent().equals(String.valueOf(searchString.charAt(range.get() - 1)))) {
                        success.set(false);
                    }

                }, () -> {
                    success.set(false);
                });
            }
            if (success.get()) {
                hits++;
            }
        }
        return hits;
    }

    int findExMAS(Grid<Element> g, Element e) {
        int hits = 0;
        Element[] one = new Element[] { g.neighbour(e, Direction.NE).orElse(null),
                g.neighbour(e, Direction.SW).orElse(null) };
        Element[] two = new Element[] { g.neighbour(e, Direction.NW).orElse(null),
                g.neighbour(e, Direction.SE).orElse(null) };
        if (!(one[0] == null || one[1] == null)) {
            var x = Arrays.asList(one).stream().map(Element::getContent).collect(Collectors.toList());
            if (x.containsAll(sm)) {
                hits++;
            }
        }
        if (!(two[0] == null || two[1] == null)) {
            var x = Arrays.asList(two).stream().map(Element::getContent).collect(Collectors.toList());
            if (x.containsAll(sm)) {
                hits++;
            }
        }
        return hits == 2 ? 1 : 0;
    }
}
