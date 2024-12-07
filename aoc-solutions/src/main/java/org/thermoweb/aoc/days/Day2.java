package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;

class Item {
    Integer diffFromPrevious;
    Integer value;

    Item(Integer previous, Integer value) {
        if (previous != null) {
            this.diffFromPrevious = value - previous;
        }
        this.value = value;
    }

    Item(Integer value) {
        this.value = value;
    }

}

@DaySolver(2)
public class Day2 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        long result = InputParseUtils.lines(input)
                .map((line) -> Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray())
                .filter((line) -> {
                    int prev = Integer.MAX_VALUE;
                    Optional<Integer> prevDiff = Optional.empty();
                    Optional<Boolean> valid = Optional.empty();
                    for (int i : line) {
                        if (prev == Integer.MAX_VALUE) {
                            prev = i;
                            continue;
                        }
                        int diff = prev - i;
                        if (Math.abs(diff) > 3 || diff == 0) {
                            valid = Optional.of(false);
                            break;
                        }
                        if (!prevDiff.isPresent()) {
                            prevDiff = Optional.of(diff);
                            prev = i;
                            continue;
                        }
                        if (Math.signum(diff) != Math.signum(prevDiff.get())) {
                            valid = Optional.of(false);
                            break;
                        }
                        prev = i;
                        prevDiff = Optional.of(diff);
                    }
                    return valid.orElse(true);
                }).collect(Collectors.counting());
        return Optional.of(BigInteger.valueOf(result));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        List<int[]> result = InputParseUtils.lines(input)
                .map((line) -> Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray())
                .filter((line) -> {
                    Boolean valid = isValid(line);
                    if (!valid) {
                        valid = isAnyValid(line);
                    }
                    return valid;
                }).collect(Collectors.toList());
        result.stream()
                .map((k) -> Arrays.stream(k).mapToObj(l -> Integer.toString(l)).collect(Collectors.joining(",")))
                .forEach(System.out::println);
        return Optional.of(BigInteger.valueOf(result.size()));
    }

    private Boolean isValid(int[] line) {
        int prev = Integer.MAX_VALUE;
        Optional<Integer> prevDiff = Optional.empty();
        Optional<Boolean> valid = Optional.empty();
        for (int i : line) {
            if (prev == Integer.MAX_VALUE) {
                prev = i;
                continue;
            }
            int diff = prev - i;
            if (Math.abs(diff) > 3 || diff == 0) {
                valid = Optional.of(false);
                break;
            }
            if (!prevDiff.isPresent()) {
                prevDiff = Optional.of(diff);
                prev = i;
                continue;
            }
            if (Math.signum(diff) != Math.signum(prevDiff.get())) {
                valid = Optional.of(false);
                break;
            }
            prev = i;
            prevDiff = Optional.of(diff);
        }
        return valid.orElse(true);
    }

    private Boolean isAnyValid(int[] line) {
        for (int i = 0; i < line.length; i++) {
            List<Integer> temp = new ArrayList<Integer>(Arrays.stream(line).boxed().toList());
            temp.remove(i);
            int[] out = temp.stream().mapToInt(Integer::intValue).toArray();
            if (isValid(out)) {
                return true;
            }
        }
        return false;
    }
}
