package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Direction;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;

class VisitedElement extends Element {
    boolean visted = true;
    boolean tempBlock = false;
    Optional<Direction> guardFacing;
    Set<Direction> historicGuardFacing = new HashSet<>();

    public Optional<Direction> getGuardFacing() {
        return guardFacing;
    }

    public void setGuardFacing(Direction guardFacing) {
        this.guardFacing = Optional.of(guardFacing);
        historicGuardFacing.add(guardFacing);
    }

    public boolean isVisted() {
        return visted;
    }

    public void setVisted(boolean visted) {
        this.visted = visted;
    }

    public boolean isTempBlock() {
        return tempBlock;
    }

    public void setTempBlock(boolean tempBlock) {
        this.tempBlock = tempBlock;
    }

    public VisitedElement(int x, int y, String content) {
        super(x, y, content);
        switch (content) {
            case "v": {
                this.setGuardFacing(Direction.S);
                break;
            }
            case "^": {
                this.setGuardFacing(Direction.N);
                break;
            }
            case ">": {
                this.setGuardFacing(Direction.E);
                break;
            }
            case "<": {
                this.setGuardFacing(Direction.W);
                break;
            }
            default: {
                this.guardFacing = Optional.empty();
                this.visted = false;
            }
        }
    }

    boolean beenBefore(Direction d) {
        return historicGuardFacing.contains(d);
    }
}

@DaySolver(6)
public class Day6 implements Day {
    Grid<VisitedElement> calculatedP1Grid;
    VisitedElement guardStart;
    int loops = 0;
    boolean print = false;

    @Override
    public Optional<BigInteger> partOne(String input) {
        try {
            Grid<VisitedElement> grid = new Grid<>(input, VisitedElement.class);
            VisitedElement guardElem = grid.find(Pattern.compile("[><\\^v]")).orElseThrow();
            this.guardStart = guardElem;
            long visited = visitedSquares(grid, guardElem);
            calculatedP1Grid = grid;
            return Optional.of(BigInteger.valueOf(visited));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        if (calculatedP1Grid == null) {
            this.partOne(input);
        }
        List<VisitedElement> potentialBlockLocations = calculatedP1Grid.filter(elem -> {
            return elem.getGuardFacing().isPresent();
        }).collect(Collectors.toList());
        int iteration = 0;
        for (VisitedElement v : potentialBlockLocations) {
            this.print = false;
            if (v.getX() == guardStart.getX() && v.getY() == guardStart.getY()) {
                continue;
            }
            System.out.println("%s/%s".formatted(++iteration, potentialBlockLocations.size()));
            try {
                Grid<VisitedElement> grid = new Grid<>(input, VisitedElement.class);
                grid.get(v.getX(), v.getY()).setTempBlock(true);
                Optional<VisitedElement> step = stepDetectLoop(grid, Optional.of(guardStart));
                while (step.isPresent()) {
                    step = stepDetectLoop(grid, step);
                }
                grid.get(v.getX(), v.getY()).setTempBlock(false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return Optional.empty();
            }

        }

        return Optional.of(BigInteger.valueOf(loops));
    }

    Optional<VisitedElement> step(Grid<VisitedElement> grid, Optional<VisitedElement> elem) {
        if (elem.isEmpty()) {
            return Optional.empty();
        }
        VisitedElement validElem = elem.get();
        validElem.setVisted(true);
        Direction facingDirection = validElem.getGuardFacing().orElseThrow();

        VisitedElement next = grid.neighbour(validElem, facingDirection);
        if (next == null) {
            // exiting grid
            return Optional.empty();
        }

        while (next.getContent().equals("#")) {
            facingDirection = facingDirection.turnRight();
            next = grid.neighbour(validElem, facingDirection);
        }
        next.setVisted(true);
        next.setGuardFacing(facingDirection);
        // printStep(grid);
        return Optional.of(next);
    }

    Optional<VisitedElement> stepDetectLoop(Grid<VisitedElement> grid, Optional<VisitedElement> elem) {
        if (elem.isEmpty()) {
            System.out.println("isNotLoop");
            return Optional.empty();
        }
        VisitedElement validElem = elem.get();
        validElem.setVisted(true);
        Direction facingDirection = validElem.getGuardFacing().orElseThrow();

        VisitedElement next = grid.neighbour(validElem, facingDirection);
        if (next == null) {
            // exiting grid
            System.out.println("isNotLoop");
            return Optional.empty();
        }

        while (next.getContent().equals("#") || next.isTempBlock()) {
            if (next.isTempBlock()) {
                this.print = true;
            }
            facingDirection = facingDirection.turnRight();

            next = grid.neighbour(validElem, facingDirection);
            if (this.print) {
                // printStep(grid);
            }
        }
        if (next.beenBefore(facingDirection)) {
            // temp block has caused guard to turn onto a path he has walked before,
            // guaranteeing loop
            System.out.println("isLoop");
            loops++;
            return Optional.empty();
        }
        next.setVisted(true);
        next.setGuardFacing(facingDirection);
        // printStep(grid);
        return Optional.of(next);
    }

    long visitedSquares(Grid<VisitedElement> grid, VisitedElement startElem) {
        Optional<VisitedElement> step = step(grid, Optional.of(startElem));
        while (step.isPresent()) {
            step = step(grid, step);
        }
        return grid.filter(e -> {
            return e.isVisted();
        }).count();
    }

    void printStep(Grid<VisitedElement> grid) {
        System.out.println(grid.toString((elem) -> {
            if (elem.isTempBlock()) {
                return "\u001B[91mO\u001B[0m";
            }
            if (elem.getGuardFacing().isPresent()) {
                return elem.getGuardFacing().get().toString();
            } else {
                return elem.getContent();
            }
        }));
    }

}
