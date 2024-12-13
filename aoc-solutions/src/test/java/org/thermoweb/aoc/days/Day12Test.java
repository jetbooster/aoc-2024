package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.Exception;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DayRunner;

class Day12Test {
    private final Day day = new Day12();

    @Test
    void test_part_one() throws Exception {
        assertEquals(Optional.empty(), day.partOne(DayRunner.getExample(12)));
    }

    @Test
    void test_part_two() throws Exception {
        assertEquals(Optional.empty(), day.partTwo(DayRunner.getExample(12)));
    }
}
