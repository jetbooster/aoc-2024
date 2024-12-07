package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;

@DaySolver(3)
public class Day3 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        Pattern p = Pattern.compile("mul\\(\\d+,\\d+\\)");
        Matcher m = p.matcher(input);
        IntStream res = m.results().mapToInt((expression) -> {
            return calculate(expression.group());
        });

        return Optional.of(BigInteger.valueOf(res.sum()));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        Pattern p = Pattern.compile("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)");
        Matcher m = p.matcher(input);
        Boolean active = true;
        int accumulator = 0;
        while (m.find()) {
            String entry = m.group();
            if (entry.equals("do()")) {
                active = true;
                continue;
            }
            if (entry.equals("don't()")) {
                active = false;
                continue;
            }
            if (!active) {
                continue;
            }
            accumulator += calculate(entry);
        }

        return Optional.of(BigInteger.valueOf(accumulator));
    }

    int calculate(String s) {
        String[] ints = s.replaceAll("\\D", " ").trim().split(" ");

        return Integer.parseInt(ints[0]) * Integer.parseInt(ints[1]);
    }
}
