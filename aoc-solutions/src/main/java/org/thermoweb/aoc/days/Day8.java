package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;
import org.thermoweb.aoc.utils.Position;
import org.thermoweb.aoc.utils.TerminalUtils;
import org.thermoweb.aoc.utils.Timer;

class AntennaElement extends Element {
    public List<String> antinodes = new ArrayList<String>();

    public List<String> getAntinodes() {
        return antinodes;
    }

    public AntennaElement(int x, int y, String content) {
        super(x, y, content);
    }

    public boolean addAntinode(AntennaElement a) {
        return this.antinodes.add(a.getContent());
    }

    public boolean hasAntinodes() {
        return this.antinodes.size() > 0;
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
        t.mark("Global", "init");
        Grid<AntennaElement> g = new Grid<AntennaElement>(input, AntennaElement.class);
        t.mark("Global", "Generate Grid");
        Map<String, List<AntennaElement>> antennaGroups = g.filter(p)
                .collect(Collectors.groupingBy(AntennaElement::getContent));
        t.mark("Global", "Collect Elems");
        antennaGroups.entrySet().forEach((entry) -> {
            this.markAntinodes(entry, g);
        });
        System.out.println(g.toString((elem) -> {
            if (elem.hasAntinodes()) {
                return TerminalUtils.printWithColour(elem.getAntinodes().get(0),
                        TerminalUtils.Colour.ANSI_RED);
            }
            return elem.getContent();
        }));
        var x = g.filter((elem) -> {
            return elem.hasAntinodes();
        }).count();
        return t.runAndStop(() -> Optional.of(BigInteger.valueOf(x)));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();
        // implementation
        t.mark("Global", "init");
        Grid<AntennaElement> g = new Grid<AntennaElement>(input, AntennaElement.class);
        t.mark("Global", "Generate Grid");
        Map<String, List<AntennaElement>> antennaGroups = g.filter(p)
                .collect(Collectors.groupingBy(AntennaElement::getContent));
        t.mark("Global", "Collect Elems");
        antennaGroups.entrySet().forEach((entry) -> {
            this.markAntinodes2(entry, g);
        });
        System.out.println(g.toString((elem) -> {
            if (elem.hasAntinodes()) {
                return TerminalUtils.printWithColour(elem.getAntinodes().get(0),
                        TerminalUtils.Colour.ANSI_RED);
            }
            return elem.getContent();
        }));
        var x = g.filter((elem) -> {
            return elem.hasAntinodes();
        }).count();
        return t.runAndStop(() -> Optional.of(BigInteger.valueOf(x)));
    }

    void markAntinodes(Map.Entry<String, List<AntennaElement>> entry, Grid<AntennaElement> g) {
        List<AntennaElement> values = entry.getValue();
        for (int i = 0; i < values.size(); i++) {
            AntennaElement val1 = values.get(i);
            for (int j = 0; j < values.size(); j++) {
                if (i == j) {
                    continue;
                }
                AntennaElement val2 = values.get(j);
                g.getElementAtOffset(val1, val2.getDirectionTo(val1)).ifPresent((elem) -> elem.addAntinode(val1));
                g.getElementAtOffset(val2, val1.getDirectionTo(val2)).ifPresent((elem) -> elem.addAntinode(val1));

            }
        }
    }

    void markAntinodes2(Map.Entry<String, List<AntennaElement>> entry, Grid<AntennaElement> g) {
        List<AntennaElement> values = entry.getValue();
        for (int i = 0; i < values.size(); i++) {
            AntennaElement val1 = values.get(i);
            for (int j = 0; j < values.size(); j++) {
                if (i == j) {
                    continue;
                }
                AntennaElement val2 = values.get(j);
                Position d1 = val1.getDirectionTo(val2);
                Position d2 = val1.getDirectionTo(val2);
                int left = 0;
                AtomicBoolean leftDone = new AtomicBoolean(false);
                int right = 0;
                AtomicBoolean rightDone = new AtomicBoolean(false);
                while (!leftDone.get()) {
                    g.getElementAtOffset(val1, d1.multiplyBy(left++)).ifPresentOrElse((elem) -> elem.addAntinode(val1),
                            () -> {
                                leftDone.set(true);
                            });

                }
                while (!rightDone.get()) {
                    g.getElementAtOffset(val2, d2.multiplyBy(right++)).ifPresentOrElse((elem) -> elem.addAntinode(val2),
                            () -> {
                                rightDone.set(true);
                            });

                }

            }
        }
    }

}
