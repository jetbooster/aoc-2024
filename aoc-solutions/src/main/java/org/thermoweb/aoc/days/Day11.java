package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Timer;

@DaySolver(11)
public class Day11 implements Day {
    Timer t = new Timer();

    Map<String, Long> map = new HashMap<>();

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            var x = Arrays.stream(input.split("\\s+")).map(Long::valueOf).collect(Collectors.toList());
            return Optional.of(BigInteger.valueOf(dfStoneCount(x, 25, 0)));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            var x = Arrays.stream(input.split("\\s+")).map(Long::valueOf).collect(Collectors.toList());
            return Optional.of(BigInteger.valueOf(dfStoneCount(x, 75, 0)));
        });
    }

    public long dfStoneCount(List<Long> list, int remainingBlinks, long accumulator) {
        if (map.containsKey(keygen(list, remainingBlinks))) {
            return map.get(keygen(list, remainingBlinks));
        }
        if (remainingBlinks == 0) {
            return list.size();
        }
        int newAccumulator = 0;
        for (long stone : list) {
            if (stone == 0) {
                accumulator += dfStoneCount(List.of(1l), remainingBlinks - 1, newAccumulator);
                continue;
            }
            String longString = Long.toString(stone);
            if (longString.length() % 2 == 0) {
                accumulator += dfStoneCount(splitHalf(longString), remainingBlinks - 1, newAccumulator);
                continue;
            }
            accumulator += dfStoneCount(List.of(stone * 2024), remainingBlinks - 1, newAccumulator);
        }
        map.put(keygen(list, remainingBlinks), accumulator);
        return accumulator;
    }

    List<Long> splitHalf(String l) {
        return List.of(Long.parseLong(l.substring(0, (l.length() / 2))), Long.parseLong(l.substring((l.length() / 2))));
    }

    String keygen(List<Long> list, int remainingBlinks) {
        return "%s:%s".formatted(remainingBlinks,
                list.stream().map((l) -> l.toString()).collect(Collectors.joining(",")));
    }
}
