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

class Scratchcard {
    public int id;
    List<Integer> winningNumbers;
    List<Integer> myNumbers;
    int matches;
    int copies = 1;
    int score = 0;

    public Scratchcard(String s) {
        String[] id_values = s.split(": ");
        this.id = Integer.parseInt(id_values[0].replace("Card ", "").trim());
        String[] winNumsString_guessNumsString = id_values[1].split(" \\| ");
        this.winningNumbers = Arrays.stream(winNumsString_guessNumsString[0].trim().split("\\s+"))
                .map(Integer::parseInt).collect(Collectors.toList());
        this.myNumbers = Arrays.stream(winNumsString_guessNumsString[1].trim().split("\\s+")).map(Integer::parseInt)
                .collect(Collectors.toList());
        this.matches = getMatches(winningNumbers, myNumbers);
        if (this.matches != 0) {
            this.score = (int) Math.round(Math.pow(2, matches - 1));
        }
    }

    private int getMatches(List<Integer> winning, List<Integer> numbers) {
        List<Integer> winningNums = new ArrayList<Integer>(numbers);
        winningNums.retainAll(winning);
        return winningNums.size();

    }

    @Override
    public String toString() {
        return String.format("id:%s, hits:%s, score:%s\n", this.id, this.matches, this.score);
    }

    public int getId() {
        return id;
    }

    public int getMatches() {
        return matches;
    }

    public int getCopies() {
        return copies;
    }

    public int getScore() {
        return score;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

}

@DaySolver(4)
public class Day4 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        Integer scratches = InputParseUtils.lines(input).map(Scratchcard::new)
                .collect(Collectors.summingInt(Scratchcard::getScore));
        return Optional.of((BigInteger.valueOf(scratches)));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        List<Scratchcard> scratches = InputParseUtils.lines(input).map(Scratchcard::new)
                .collect(Collectors.toList());
        for (int i = 0; i < scratches.size(); i++) {
            int matches = scratches.get(i).matches;
            int copies = scratches.get(i).copies;
            if (matches == 0) {
                continue;
            }
            for (int j = 1; j <= matches; j++) {
                Scratchcard scratch = scratches.get(i + j);
                scratch.setCopies(scratch.copies + copies);
            }
        }
        int totalCards = scratches.stream().collect(Collectors.summingInt(Scratchcard::getCopies));
        return Optional.of(BigInteger.valueOf(totalCards));
    }
}
