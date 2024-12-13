package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.DayRunner;

class Day11Test {
    private final Day11 day = new Day11();

    @Test
    void test_part_one() throws Exception {
        assertEquals(Optional.of(BigInteger.valueOf(55312)), day.partOne(DayRunner.getExample(11)));
    }

    @Test
    void test_part_two() throws Exception {
        assertEquals(Optional.empty(), day.partTwo(DayRunner.getExample(11)));
    }

    @Test
    void dfStone() {
        long answer = day.dfStoneCount(List.of(0l, 1l, 10l), 1, 0);
        long answer2 = day.dfStoneCount(List.of(0l, 1l, 10l), 2, 0);
        long answer3 = day.dfStoneCount(List.of(0l, 1l, 10l), 3, 0);
        assertEquals(answer, 4);
        assertEquals(answer2, 5);
        assertEquals(answer3, 9);
    }
}
