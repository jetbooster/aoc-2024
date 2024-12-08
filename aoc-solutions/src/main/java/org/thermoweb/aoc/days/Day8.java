package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;
import org.thermoweb.aoc.utils.Timer;

class AntennaElement extends Element {
    public List<String> antinodes = new ArrayList<String>();

    public AntennaElement(int x, int y, String content) {
        super(x, y, content);
    }

}

@DaySolver(8)
public class Day8 implements Day {
    Timer t = new Timer();
    Predicate<String> patternPredicate = Pattern.compile("\\.").asMatchPredicate();
    Predicate<AntennaElement> p = (elem) -> {
        return !patternPredicate.test(elem.getContent());
    };

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation
        Grid<AntennaElement> g = new Grid<AntennaElement>(input, AntennaElement.class);
        Map<String, List<AntennaElement>> antennaGroups = g.filter(p)
                .collect(Collectors.groupingBy(AntennaElement::getContent));
        antennaGroups.entrySet().forEach((entry) -> {
            this.markAntinodes(entry, g);
        });
        return t.runAndStop(() -> Optional.empty());
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        return t.runAndStop(() -> Optional.empty());
    }

    <T extends Element> void markAntinodes(Map.Entry<String, List<T>> entry, Grid<T> g) {
        List<T> values = entry.getValue();
        for (int i = 0; i > values.size(); i++) {
            T val1 = values.get(i);
            for (int j = 0; j > values.size(); j++) {
                if (i == j) {
                    continue;
                }
                T val2 = values.get(j);

            }
        }
    }

}
