package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;

record Instruction(int before, int after) {
    Instruction(String s) {
        this(splitAndParse(s));
    }

    private static int[] splitAndParse(String s) {
        String[] b_a = s.split("\\|");
        return new int[] { Integer.parseInt(b_a[0]), Integer.parseInt(b_a[1]) };
    }

    private Instruction(int[] parsed) {
        this(parsed[0], parsed[1]);
    }
}

@DaySolver(5)
public class Day5 implements Day {

    Comparator<Instruction> InstructionComparator = new Comparator<Instruction>() {
        public int compare(Instruction a, Instruction b) {
            if (a.after() == b.before()) {
                return -1;
            }
            if (a.before() == b.after()) {
                return -1;
            }
            return 0;
        };
    };

    @Override
    public Optional<BigInteger> partOne(String input) {
        String[] instructions_lists = input.split("\\n\\n");
        List<Instruction> instructionList = InputParseUtils.lines(instructions_lists[0]).map((s) -> new Instruction(s))
                .toList();

        var x = InputParseUtils.lines(instructions_lists[1])
                .map((line) -> {
                    var result = line.split(",");
                    return result;
                })
                .map((m) -> {
                    var y = Arrays.asList(m);
                    return y;
                })
                .filter((list) -> passesInstructions(list, instructionList))
                .mapToInt(this::grabMiddleElement)
                .sum();

        return Optional.of(BigInteger.valueOf(x));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        String[] instructions_lists = input.split("\\n\\n");

        List<Instruction> instructionList = InputParseUtils.lines(instructions_lists[0]).map((s) -> new Instruction(s))
                .toList();

        var x = InputParseUtils.lines(instructions_lists[1])
                .map(line -> line.split(","))
                .map(m -> Arrays.stream(m).collect(Collectors.toList()))
                .filter(list -> !passesInstructions(list, instructionList))
                .map(list -> this.sortList(list, instructionList))
                .mapToInt(this::grabMiddleElement)
                .sum();

        return Optional.of(BigInteger.valueOf(x));
    }

    boolean passesInstructions(List<String> list, List<Instruction> instructions) {
        return instructions.stream().allMatch((instruction) -> passesInstruction(list, instruction));
    }

    List<Instruction> subsetInstructions(List<String> list, List<Instruction> instructions) {
        return instructions.stream().filter(instruction -> list.contains(Integer.toString(instruction.before()))
                && list.contains(Integer.toString(instruction.after()))).collect(Collectors.toList());
    }

    boolean passesInstruction(List<String> list, Instruction instruction) {
        String beforeString = Integer.toString(instruction.before());
        String afterString = Integer.toString(instruction.after());
        if (!list.contains(beforeString) || !list.contains(afterString)) {
            // can't break the rule if one or the other number never appears
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(beforeString)) {
                continue;
            }
            int afterLocation = list.indexOf(afterString);
            if (afterLocation < i) {
                return false;
            }
        }
        return true;
    }

    int grabMiddleElement(List<String> list) {
        return Integer.parseInt(list.get(Math.floorDiv(list.size(), 2)));
    }

    String findRoot(List<String> list, List<Instruction> instructions) {
        List<String> afters = instructions.stream().map(i -> Integer.toString(i.after())).toList();
        Set<String> result = instructions.stream()
                .filter(instruction -> !afters.contains(Integer.toString(instruction.before())))
                .mapToInt(i -> i.before()).mapToObj(Integer::toString).collect(Collectors.toSet());
        if (result.size() != 1) {
            System.exit(0);
        }
        return result.iterator().next();
    }

    List<String> recursiveSortList(List<String> list, List<Instruction> instructions, ArrayList<String> accumulator) {
        if (list.size() == 1) {
            accumulator.add(list.get(0));
            return accumulator;
        }
        List<Instruction> subset = subsetInstructions(list, instructions);
        String nextRoot = findRoot(list, subset);
        accumulator.add(nextRoot);
        list.remove(nextRoot);
        return recursiveSortList(list, subset, accumulator);
    };

    List<String> sortList(List<String> list, List<Instruction> instructions) {
        ArrayList<String> accumulator = new ArrayList<>();
        return recursiveSortList(list, instructions, accumulator);
    }

}
