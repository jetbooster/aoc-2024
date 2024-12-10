package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Timer;

record Element(Integer id, int postion) {
    Element(Optional<Integer> id, int position) {
        this(id.isEmpty() ? null : id.get(), position);
    }
}

record GroupElement(Integer id, int postion, int length) {
    GroupElement(Optional<Integer> id, int position, int length) {
        this(id.isEmpty() ? null : id.get(), position, length);
    }

    boolean isHole() {
        return id == null;
    }
}

@DaySolver(9)
public class Day9 implements Day {
    Timer t = new Timer();
    Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public Optional<BigInteger> partOne(String input) {
        t.restart();
        // implementation

        return t.runAndStop(() -> {
            ArrayList<Element> elems = new ArrayList<>();
            AtomicInteger id = new AtomicInteger(0);
            AtomicInteger memoryLocation = new AtomicInteger(0);
            AtomicBoolean isMemory = new AtomicBoolean(true);
            Arrays.stream(input.split("")).mapToInt(Integer::valueOf).forEach(elem -> {
                if (!isMemory.get()) {
                    for (int i = 0; i < elem; i++) {
                        elems.add(new Element(Optional.empty(), memoryLocation.getAndIncrement()));
                    }
                    isMemory.set(true);
                    return;
                }
                int myId = id.getAndIncrement();
                for (int i = 0; i < elem; i++) {
                    elems.add(new Element(Optional.of(myId), memoryLocation.getAndIncrement()));
                }
                isMemory.set(false);

            });
            System.out.println(elems.size());
            boolean print = false;
            for (int i = 0; i < elems.size() - 1; i++) {
                Element el = elems.get(i);
                if (el.id() == null) {
                    Element pulledElem = elems.removeLast();
                    while (pulledElem.id() == null) {
                        pulledElem = elems.removeLast();
                    }
                    elems.set(i, new Element(pulledElem.id(), i));
                }
                if (i == 99999) {
                    print = true;
                }
                if (print) {
                    String str = print(elems);
                    System.out.println(str);
                }
            }
            long checksum = elems.stream()
                    .filter((elem) -> {
                        return elem.id() == null ? false : true;
                    })
                    .mapToLong((elem) -> elem.id() * elem.postion())
                    .reduce(0, (a, b) -> a + b);
            return Optional.of(BigInteger.valueOf(checksum));
        });
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        t.restart();

        // implementation
        return t.runAndStop(() -> {
            ArrayList<GroupElement> elems = new ArrayList<>();
            AtomicInteger id = new AtomicInteger(0);
            AtomicInteger memoryLocation = new AtomicInteger(0);
            AtomicBoolean isMemory = new AtomicBoolean(true);
            Arrays.stream(input.split("")).mapToInt(Integer::valueOf).forEach(elem -> {
                if (elem == 0) {
                    if (isMemory.get()) {
                        id.getAndIncrement();
                    }
                    isMemory.set(!isMemory.get());
                    return;
                }
                if (!isMemory.get()) {
                    elems.add(new GroupElement(Optional.empty(), memoryLocation.get(), elem));
                    memoryLocation.addAndGet(elem);
                    isMemory.set(true);
                    return;
                }
                int myId = id.getAndIncrement();
                elems.add(new GroupElement(Optional.of(myId), memoryLocation.get(), elem));
                memoryLocation.addAndGet(elem);
                isMemory.set(false);

            });

            for (int i = 0; i < elems.size() - 1; i++) {
                GroupElement curr = elems.get(i);
                GroupElement next = elems.get(i + 1);
                if (curr.isHole() && next.isHole()) {
                    elems.set(i, new GroupElement(Optional.empty(), curr.postion(), curr.length() + next.length()));
                    elems.remove(i + 1);
                }
            }

            defrag(elems);
            var x = printGroup(elems);
            return Optional.of(BigInteger.valueOf(checksumAll(elems)));

        });
    }

    String print(List<Element> elems) {
        StringBuilder s = new StringBuilder();
        elems.stream().forEach((elem) -> {
            if (elem.id() != null) {
                s.append(elem.id());
                s.append(",");
            } else {
                s.append(".,");
            }
        });
        return s.toString();
    }

    String printGroup(List<GroupElement> elems) {
        StringBuilder s = new StringBuilder();
        elems.stream().forEach((elem) -> {
            if (elem.id() != null) {
                IntStream.range(0, elem.length()).forEachOrdered((i) -> {
                    s.append(elem.id());
                    s.append(",");
                });
            } else {
                IntStream.range(0, elem.length()).forEachOrdered((i) -> {
                    s.append(".,");
                });
            }
        });
        return s.toString();
    }

    long checksumAll(List<GroupElement> elems) {
        var sums = elems.stream().filter((elem) -> !elem.isHole()).mapToLong(this::checksum).boxed()
                .collect(Collectors.toList());

        return sums.stream().mapToLong(Long::valueOf).sum();
    }

    public long checksum(GroupElement elem) {
        long accumulator = 0;

        for (int i = elem.postion(); i < elem.postion() + elem.length(); i++) {
            accumulator += i * elem.id();
        }
        return accumulator;
    }

    void defrag(ArrayList<GroupElement> elems) {
        for (int i = elems.size() - 1; i >= 0; i--) {
            GroupElement testElem = elems.get(i);
            if (testElem.isHole()) {
                continue;
            }

            if (testElem.id().equals(Integer.valueOf(91))) {
                var x = printGroup(elems);
                System.out.println(x);
            }

            for (int j = 0; (j < elems.size() - 1) && (j <= i); j++) {
                GroupElement potentialHole = elems.get(j);
                if (!potentialHole.isHole() || potentialHole.length() < testElem.length()) {
                    continue;
                }
                elems.remove(i);
                if (potentialHole.length() == testElem.length()) {
                    elems.set(j, new GroupElement(testElem.id(), potentialHole.postion(), testElem.length()));
                    elems.add(i, new GroupElement(Optional.empty(), testElem.postion(), testElem.length()));
                } else {
                    elems.set(j, new GroupElement(testElem.id(), potentialHole.postion(), testElem.length()));
                    elems.add(j + 1, new GroupElement(Optional.empty(), potentialHole.postion() + testElem.length(),
                            potentialHole.length() - testElem.length()));
                    elems.add(i + 1, new GroupElement(Optional.empty(), testElem.postion(), testElem.length()));
                    i++;
                }
                break;
            }
        }
        var x = printGroup(elems);
        System.out.println(x);
    }
}
