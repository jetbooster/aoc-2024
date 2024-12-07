package org.thermoweb.aoc.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
        s.append("====\n").append("Start\t0\n");
        checkpointGroups.entrySet().stream().forEach((group) -> {
            if (group.getKey().equals("Global")) {
                return;
            }
            AtomicInstant last = new AtomicInstant(this.startTime);
            s.append("Group %s\s\n".formatted(group.getKey()));
            group.getValue().stream().forEach((Item i) -> {
                s.append(
                        "\t%s\t%sms (+%sms)\n".formatted(
                                i.name(),
                                Duration.between(this.startTime, i.instant()).toMillis(),
                                Duration.between(last.get(), i.instant()).toMillis()));

                last.set(i.instant());
            });
        });
        s.append("Global\n");
        checkpointGroups.get("Global").stream().forEach(i -> {
            s.append("\t%s\t%sms\n".formatted(
                    i.name(),
                    Duration.between(this.startTime, i.instant()).toMillis()));
        });

        return s.append("====\n").toString();

    }

    public <T> T runAndStop(Supplier<T> s) {
        T result = s.get();
        this.stop();
        System.out.println(this);
        return result;
    }
}
