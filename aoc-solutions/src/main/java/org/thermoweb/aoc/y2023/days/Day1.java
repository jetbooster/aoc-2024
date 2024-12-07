package org.thermoweb.aoc.y2023.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;

enum DigitMapper {
    ONE("one","o1e"),
    TWO("two","t2o"),
    THREE("three","t3e"),
    FOUR("four","f4r"),
    FIVE("five","f5e"),
    SIX("six","s6x"),
    SEVEN("seven","s7n"),
    EIGHT("eight","e8t"),
    NINE("nine","n9n");

    private final String before;
    private final String after;
    DigitMapper(String before, String after){
        this.before = before;
        this.after = after;
    }

    String map(String input){
        return input.replace(this.before, this.after);
    }

}

@DaySolver(1)
public class Day1 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        Predicate<String> p = Pattern.compile("\\d").asPredicate();
        var lines = InputParseUtils.lines(input);
        var result = lines.map(line->{
            var numerical = Arrays.stream(line.split("")).filter(p).collect(Collectors.joining());

            if (numerical.length() == 1){
                numerical = numerical+numerical;
            }
            if (numerical.length() > 2){
                numerical = String.valueOf(numerical.charAt(0)) + String.valueOf(numerical.charAt(numerical.length()-1));
            }
            return Integer.parseInt(numerical);
            
        })
        .collect(Collectors.summingInt(Integer::valueOf));

        return Optional.of(BigInteger.valueOf(result));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        var lines = InputParseUtils.lines(input);
        Predicate<String> p = Pattern.compile("\\d").asPredicate();

        var result = lines.map(line->{
            String newLine = line;
            for (DigitMapper d : DigitMapper.values()){
                newLine = d.map(newLine);
            }
            var numerical = Arrays.stream(newLine.split("")).filter(p).collect(Collectors.joining());
            if (numerical.length() == 1){
                numerical = numerical+numerical;
            }
            if (numerical.length() > 2){
                numerical = String.valueOf(numerical.charAt(0)) + String.valueOf(numerical.charAt(numerical.length()-1));
            }
            return Integer.parseInt(numerical);
            
        })
        .collect(Collectors.summingInt(Integer::valueOf));
        // .collect(Collectors.toList());
        System.out.println(result);
        return Optional.of(BigInteger.valueOf(result));
        // return Optional.empty();
    }
}
