package org.thermoweb.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.thermoweb.aoc.DayRunner;

class Day9Test {
    private final Day9 day = new Day9();

    @Test
    void test_part_one() throws Exception {
        assertEquals(Optional.of(BigInteger.valueOf(1928)), day.partOne(DayRunner.getExample(9)));
    }

    @Test
    void test_part_two() throws Exception {
        assertEquals(Optional.of(BigInteger.valueOf(2858)), day.partTwo(DayRunner.getExample(9)));
    }

    @Test
    void checksum() {
        assertEquals(day.checksum(new GroupElement(Optional.of(8), 8, 3)), 216);
    }

    @Test
    void checksumAll() {
        ArrayList<GroupElement> arr = new ArrayList<>();
        arr.add(new GroupElement(Optional.of(8), 8, 3)); // 216
        arr.add(new GroupElement(Optional.of(9), 11, 3)); // 324

        assertEquals(day.checksumAll(arr), 540);
    }
}
