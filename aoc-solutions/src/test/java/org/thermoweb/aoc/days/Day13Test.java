package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.Exception;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DayRunner;

class Day13Test {
    private final Day day = new Day13();

    @Test
    void test_part_one() throws Exception {
        assertEquals(Optional.empty(), day.partOne(DayRunner.getExample(13)));
    }

    @Test
    void test_part_two() throws Exception {
        assertEquals(Optional.empty(), day.partTwo(DayRunner.getExample(13)));
    }
}
