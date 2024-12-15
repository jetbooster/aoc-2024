package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;
import org.thermoweb.aoc.utils.Pair;
import org.thermoweb.aoc.utils.Position;
import org.thermoweb.aoc.utils.Timer;

class RobotElement extends Element {
    int robots = 0;

    RobotElement(int x, int y, String content) {
        super(x, y, content);
    }

    public void addRobot() {
        this.robots++;
    }

    @Override
    public String toString() {
        if (this.robots == 0) {
            return " ";
        }
        return Integer.toString(robots);
    }
}

@DaySolver(14)
public class Day14 implements Day {
    Timer t = new Timer();
    final static Pattern p = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");

    @Override
    public Optional<BigInteger> partOne(String input) {
        int w = 101;
        int h = 103;
        t.restart();
        HashMap<String, Integer> resultsMap = new HashMap<String, Integer>();
        resultsMap.put("a", 0);
        resultsMap.put("b", 0);
        resultsMap.put("c", 0);
        resultsMap.put("d", 0);

        // implementation
        return t.runAndStop(() -> {
            Grid<RobotElement> g = new Grid<>(w, h, ".", RobotElement.class);

            parseInput(input).forEach((parsed) -> {
                Position newLocation = new Position(
                        Math.floorMod(parsed.a().getX() + parsed.b().getX() * 100, w),
                        Math.floorMod(parsed.a().getY() + parsed.b().getY() * 100, h));
                g.get(newLocation).get().addRobot();
            });

            g.forEach((elem) -> {
                if (elem.getX() < ((w - 1) / 2)) {
                    if (elem.getY() < ((h - 1) / 2)) {
                        resultsMap.compute("a", (a, b) -> {
                            return b + elem.robots;
                        });
                    }
                    if (elem.getY() > ((h - 1) / 2)) {
                        resultsMap.compute("c", (a, b) -> {
                            return b + elem.robots;
                        });
                    }
                }
                if (elem.getX() > ((w - 1) / 2)) {
                    if (elem.getY() < ((h - 1) / 2)) {
                        resultsMap.compute("b", (a, b) -> {
                            return b + elem.robots;
                        });
                    }
                    if (elem.getY() > ((h - 1) / 2)) {
                        resultsMap.compute("d", (a, b) -> {
                            return b + elem.robots;
                        });
                    }
                }
            });

            return Optional.of(
                    BigInteger.valueOf(
                            resultsMap.entrySet().stream()
                                    .mapToInt((e) -> e.getValue())
                                    .reduce(1, (a, b) -> {
                                        return a * b;
                                    })));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        int w = 101;
        int h = 103;
        AtomicInteger iteration = new AtomicInteger(0);
        // implementation
        return t.runAndStop(() -> {
            List<Pair<Position, Position>> inputs = parseInput(input).collect(Collectors.toList());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (rotationMatch(iteration.get())) {
                    Grid<RobotElement> g = new Grid<>(w, h, ".", RobotElement.class);
                    inputs.stream().forEach((parsed) -> {
                        Position newLocation = new Position(
                                Math.floorMod(parsed.a().getX() + parsed.b().getX() * iteration.get(), w),
                                Math.floorMod(parsed.a().getY() + parsed.b().getY() * iteration.get(), h));
                        g.get(newLocation).get().addRobot();
                    });
                    String s = g.toString((elem) -> {
                        return elem.toString();
                    });

                    System.out.println(s);
                    System.out.println(iteration);
                    String result = scanner.nextLine();
                    if (result.equals("exit")) {
                        break;
                    }
                }
                iteration.incrementAndGet();

            }
            scanner.close();
            return Optional.empty();
        });
    }

    Stream<Pair<Position, Position>> parseInput(String input) {
        return Arrays.stream(input.split("\n")).map((in) -> {
            var match = p.matcher(in).results().findFirst().get();
            return new Pair<Position, Position>(
                    new Position(match.group(1), match.group(2)),
                    new Position(match.group(3), match.group(4)));
        });
    }

    boolean rotationMatch(int i) {
        // loops and offsets found analytically by looking at many printed frames
        if (((i - 12) % 101) == 0) {
            return true;
        }
        if (((i - 88) % 103) == 0) {
            return true;
        }
        return false;
    }
}
