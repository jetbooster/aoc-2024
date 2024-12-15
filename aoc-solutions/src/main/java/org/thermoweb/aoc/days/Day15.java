package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Direction;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;
import org.thermoweb.aoc.utils.Timer;

@DaySolver(15)
public class Day15 implements Day {
    Timer t = new Timer();
    Grid<Element> g;
    Element robotElement;
    Scanner scanner = new Scanner(System.in);

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            String[] map__instructions = input.split("\n\n");
            g = new Grid<Element>(map__instructions[0], Element.class);
            robotElement = g.filter("@").findFirst().get();
            Arrays.stream(
                    map__instructions[1].replaceAll("\\s+", "").split(""))
                    .map(Direction::convertToDirection)
                    .forEach((d) -> this.runInstruction(d, false));

            var result = this.g.filter("O").mapToInt((e) -> e.getX() + (e.getY() * 100)).sum();
            return Optional.of(BigInteger.valueOf(result));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            String[] map__instructions = input.split("\n\n");
            String new_input = map__instructions[0]
                    .replace(".", "..")
                    .replaceAll("O", "[]")
                    .replaceAll("@", "@.")
                    .replaceAll("#", "##");

            g = new Grid<Element>(new_input, Element.class);
            System.out.println(g);
            robotElement = g.filter("@").findFirst().get();
            Arrays.stream(
                    map__instructions[1].replaceAll("\\s+", "").split(""))
                    .map(Direction::convertToDirection)
                    .forEach((d) -> this.runInstruction(d, false));

            var result = this.g.filter("[")
                    .mapToInt((e) -> e.getX() + (e.getY() * 100)).sum();
            return Optional.of(BigInteger.valueOf(result));
        });
    }

    void runInstruction(Direction d, boolean debug) {
        if (canMove(d, this.robotElement)) {
            performShoves(walkTree(this.robotElement, d), d);

        } else {
            if (debug) {
                System.out.println("couldn't move %s".formatted(d));
            }
        }
        if (debug) {
            System.out.println("Moving %s".formatted(d));
            System.out.println(this.g);
            System.out.println("Robot is at %s".formatted(this.robotElement));
            this.scanner.nextLine();
        }
    }

    boolean canMove(Direction d, Element e) {
        Optional<Element> potentialNext = this.g.neighbour(e, d);
        if (!potentialNext.isPresent()) {
            return false;
        }
        Element nextElem = potentialNext.get();
        if (nextElem.getContent().equals("#")) {
            return false;
        }
        if (nextElem.getContent().equals(".")) {
            return true;
        }
        if (d.equals(Direction.N) || d.equals(Direction.S)) {
            if (nextElem.getContent().equals("[")) {
                Element partnerElem = this.g.neighbour(nextElem, Direction.E).get();
                return canMove(d, nextElem) && canMove(d, partnerElem);
            }
            if (nextElem.getContent().equals("]")) {
                Element partnerElem = this.g.neighbour(nextElem, Direction.W).get();
                return canMove(d, nextElem) && canMove(d, partnerElem);
            }
        }
        return canMove(d, nextElem);
    }

    void performShoves(ArrayList<Element> elemsToShove, Direction d) {
        List<Element> noDuplicates = elemsToShove
                .stream()
                .distinct()
                .collect(Collectors.toList());
        for (Element e : noDuplicates) {
            var swapper = this.g.neighbour(e, d).get();
            if (swapper.getContent().equals("#")) {
                // shouldn't get here
                var a = 1;
            }
            this.g.swap(e, swapper);
            // System.out.println(this.g);
            // this.scanner.nextLine();
        }
        this.robotElement = this.g.find("@").get();
    }

    ArrayList<Element> walkTree(Element current, Direction d) {
        ArrayList<Element> elemsToShove = new ArrayList<>();
        var potentialNextElem = this.g.neighbour(current, d);
        if (potentialNextElem.isPresent()) {
            var nextElem = potentialNextElem.get();
            if (nextElem.getContent().equals("[")) {
                if (d.equals(Direction.N) || d.equals(Direction.S)) {
                    var sisterElem = this.g.neighbour(nextElem, Direction.E).get(); // should be ]
                    elemsToShove.addAll(walkTree(sisterElem, d));
                }
                elemsToShove.addAll(walkTree(nextElem, d));
            }
            if (nextElem.getContent().equals("]")) {
                if (d.equals(Direction.N) || d.equals(Direction.S)) {
                    var sisterElem = this.g.neighbour(nextElem, Direction.W).get(); // should be [
                    elemsToShove.addAll(walkTree(sisterElem, d));
                }
                elemsToShove.addAll(walkTree(nextElem, d));

            }
            if (nextElem.getContent().equals("O")) {
                elemsToShove.addAll(walkTree(nextElem, d));
            }
        }

        elemsToShove.add(current);
        return elemsToShove;
    }
}
