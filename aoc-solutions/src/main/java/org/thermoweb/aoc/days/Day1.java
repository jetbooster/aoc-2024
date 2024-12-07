package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;

@DaySolver(1)
public class Day1 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        InputParseUtils.lines(input).forEach(line -> {
            String[] inputs = line.split("\\s+");
            first.add(Integer.parseInt(inputs[0]));
            second.add(Integer.parseInt(inputs[1]));
        });
        first.sort(Comparator.comparingInt(Integer::valueOf));
        second.sort(Comparator.comparingInt(Integer::valueOf));
        int diff = 0;
        for (int i = 0; i < first.size(); i++) {
            diff += Math.abs(first.get(i) - second.get(i));
        }

        return Optional.of(BigInteger.valueOf(diff));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        InputParseUtils.lines(input).forEach(line -> {
            String[] inputs = line.split("\\s+");
            first.add(Integer.parseInt(inputs[0]));
            second.add(Integer.parseInt(inputs[1]));
        });
        Integer result = first.stream().collect(Collectors.reducing(0, (item1) -> {
            Long appearances = second.stream().filter(item2 -> item2.equals(item1)).count();
            return appearances.intValue() * item1;
        }, (a, b) -> a + b));
        return Optional.of(BigInteger.valueOf(result));
    }
}
