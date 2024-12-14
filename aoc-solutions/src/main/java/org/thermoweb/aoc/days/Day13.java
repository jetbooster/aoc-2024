package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.ejml.simple.SimpleMatrix;
import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Pair;
import org.thermoweb.aoc.utils.Timer;

class EquationSet {
    final static Pattern p = Pattern.compile("Button .: X\\+(\\d+), Y\\+(\\d+)");
    final static Pattern p2 = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
    long a_x, a_y, b_x, b_y, c_x, c_y;

    final static int A_COST = 3;
    final static int B_COST = 1;

    EquationSet(String s, boolean part2) {
        String[] lines = s.split(System.lineSeparator());
        var line1Result = p.matcher(lines[0]).results().findFirst().get();
        var line2Result = p.matcher(lines[1]).results().findFirst().get();
        var line3Result = p2.matcher(lines[2]).results().findFirst().get();
        this.a_x = Long.parseLong(line1Result.group(1));
        this.a_y = Long.parseLong(line1Result.group(2));
        this.b_x = Long.parseLong(line2Result.group(1));
        this.b_y = Long.parseLong(line2Result.group(2));
        this.c_x = Long.parseLong(part2 ? "10000000000000" + line3Result.group(1) : line3Result.group(1));
        this.c_y = Long.parseLong(part2 ? "10000000000000" + line3Result.group(2) : line3Result.group(2));
    }

    Optional<Pair<Long, Long>> solve() {
        var x = new SimpleMatrix(new double[][] { new double[] { a_x, b_x }, new double[] { a_y, b_y } });
        var c = new SimpleMatrix(new double[][] { new double[] { c_x }, new double[] { c_y } });
        var resultMatrix = x.solve(c);
        var result_x = resultMatrix.get(0);
        var result_y = resultMatrix.get(1);
        if ((result_y - Math.floor(result_y) > 0.000001) || (result_x - Math.floor(result_x) > 0.000001)
                || Math.signum(result_x) == -1 || Math.signum(result_y) == -1) {

            return Optional.empty();
        }
        return Optional.of(new Pair<Long, Long>((long) Math.floor(result_x),
                (long) Math.floor(result_y)));
    }

}

@DaySolver(13)
public class Day13 implements Day {
    Timer t = new Timer();

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            var sum = Arrays.stream(input.split("\n\n"))
                    .map((in) -> new EquationSet(in, false))
                    .map(eq -> eq.solve())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map((pair) -> EquationSet.A_COST * pair.a() + EquationSet.B_COST * pair.b())
                    .collect(Collectors.toList());

            return Optional.of(BigInteger.valueOf(sum.stream().mapToLong(Long::valueOf).sum()));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> {
            var sum = Arrays.stream(input.split("\n\n"))
                    .map((in) -> new EquationSet(in, true))
                    .map(eq -> eq.solve())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map((pair) -> EquationSet.A_COST * pair.a() + EquationSet.B_COST * pair.b())
                    .collect(Collectors.toList());

            return Optional.of(BigInteger.valueOf(sum.stream().mapToLong(Long::valueOf).sum()));
        });
    }

}
