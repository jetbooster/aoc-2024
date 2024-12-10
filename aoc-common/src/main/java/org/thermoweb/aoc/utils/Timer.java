package org.thermoweb.aoc.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

record Item(String name, Instant instant) {
}

public class Timer {
    Instant startTime;
    Map<String, List<Item>> checkpointGroups;

    public Timer() {
        this.startTime = Instant.now();
        this.checkpointGroups = new HashMap<>();
        this.mark("Global", "Start");
    }

    public void restart() {
        this.startTime = Instant.now();
        this.checkpointGroups = new HashMap<>();
        this.mark("Global", "Start");
    }

    public List<Item> startGroup(String name) {
        List<Item> list = new ArrayList<Item>();
        this.checkpointGroups.put(name, list);
        return list;
    }

    public void mark(String group, String s) {
        if (!this.checkpointGroups.containsKey(group)) {
            this.startGroup(group);
        }
        this.checkpointGroups.get(group).add(new Item(s, Instant.now()));
    }

    public void stop() {
        this.mark("Global", "End");

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("====\n");
        List<List<String>> lists = new ArrayList<>();
        checkpointGroups.entrySet().stream().forEach((group) -> {
            if (group.getKey().equals("Global")) {
                return;
            }
            AtomicInstant last = new AtomicInstant(this.startTime);
            lists.add(Arrays.asList(group.getKey(), "", "", ""));
            group.getValue().stream().forEach((Item i) -> {
                lists.add(Arrays.asList("", i.name(), Duration.between(this.startTime, i.instant()).toMillis() + "ms",
                        "(+" + Duration.between(last.get(), i.instant()).toMillis() + "ms)"));
                last.set(i.instant());
            });
        });
        lists.add(Arrays.asList("Global", "", "", ""));
        AtomicInstant last = new AtomicInstant(this.startTime);

        checkpointGroups.get("Global").stream().forEach(i -> {
            lists.add(Arrays.asList("", i.name(), Duration.between(this.startTime, i.instant()).toMillis() + "ms",
                    "(+" + Duration.between(last.get(), i.instant()).toMillis() + "ms)"));
            last.set(i.instant());
        });
        s.append(formatAsTable(lists));

        return s.append("====\n").toString();

    }

    public <T> T runAndStop(Supplier<T> s) {
        T result = s.get();
        this.stop();
        System.out.println(this);
        return result;
    }

    public static String formatAsTable(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }
}
