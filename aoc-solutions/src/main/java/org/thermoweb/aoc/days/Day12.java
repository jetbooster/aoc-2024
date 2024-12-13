package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Direction;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;
import org.thermoweb.aoc.utils.Timer;

class PlantElement extends Element {
    int groupId = -1;

    PlantElement(int x, int y, String content) {
        super(x, y, content);
    }

    boolean hasGroup() {
        return this.groupId > -1;
    }

    int getGroup() {
        return this.groupId;
    }

    void setGroup(int grp) {
        this.groupId = grp;
    }
}

@DaySolver(12)
public class Day12 implements Day {
    Timer t = new Timer();
    int groupId = 0;
    Grid<PlantElement> g;

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            g = new Grid<>(input, PlantElement.class);
            g.forEach((elem) -> {
                if (elem.hasGroup()) {
                    return;
                }
                walkField(g, elem, groupId++);
            });

            var x = g.stream().collect(Collectors.groupingBy(PlantElement::getGroup));

            var result = x.entrySet().stream().mapToLong((elem) -> {
                return fences(g, elem);
            }).sum();

            return Optional.of(BigInteger.valueOf(result));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            var x = g.stream().collect(Collectors.groupingBy(PlantElement::getGroup));

            var result = x.entrySet().stream().mapToInt((elem) -> {
                return corners(g, elem);
            }).sum();

            return Optional.of(BigInteger.valueOf(result));
        });
    }

    void walkField(Grid<PlantElement> g, PlantElement elem, int gId) {
        elem.setGroup(gId);
        g.neighbours(elem)
                .filter((e) -> elem.getContent().equals(e.getContent()))
                .filter((e) -> !e.hasGroup())
                // these elements are neighbours, and have no group of their own
                .forEach((e) -> {
                    e.setGroup(gId);
                    walkField(g, e, gId);
                });

    }

    public long fences(Grid<PlantElement> g, Map.Entry<Integer, List<PlantElement>> entry) {
        long numFences = entry.getValue().stream().mapToLong((elem) -> {
            return 4l - g.neighbours(elem, false, 1, true)
                    .filter(e -> e != null && elem.getContent().equals(e.getContent()))
                    .count();
        }).sum();
        System.out.println("%s %s %s".formatted(entry.getKey(), entry.getValue(), numFences));
        return numFences * entry.getValue().size();
    }

    public int corners(Grid<PlantElement> g, Map.Entry<Integer, List<PlantElement>> entry) {
        return entry.getValue().stream().mapToInt((e) -> cornersAtThisCell(g, e)).sum() * entry.getValue().size();
    }

    public int cornersAtThisCell(Grid<PlantElement> g, PlantElement elem) {
        int corners = 0;
        var boolList = Arrays.asList(Direction.values()).stream().map((dir) -> {
            // start north
            String neighbourVal = g.neighbour(elem, dir).map(PlantElement::getContent).orElse(null);
            if (neighbourVal == null || !neighbourVal.equals(elem.getContent())) {
                return false;
            }
            return true;
        }).reduce(new ArrayList<Boolean>(), (x, y) -> {
            x.add(y);
            return x;
        }, (x, y) -> {
            x.addAll(y);
            return x;
        });
        for (int i = 0; i < 8; i += 2) {
            boolean a = boolList.get(i % 8).booleanValue(),
                    b = boolList.get((i + 1) % 8).booleanValue(),
                    c = boolList.get((i + 2) % 8).booleanValue();
            if (!a && !c) {
                corners++;
            } else if (a && c && !b) {
                corners++;
            }
        }
        return corners;
    }
}
