package org.thermoweb.aoc.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Timer {
    Instant startTime;
    Map<String,Instant> checkpoints;
    public Timer(){
        this.startTime = Instant.now();
        this.checkpoints = new LinkedHashMap<>();
    }
    public void mark(String s){
        this.checkpoints.put(s,Instant.now());
    }

    public void stop(){
        this.mark("End");

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Start\t0\n");
        AtomicInstant last = new AtomicInstant(this.startTime);
        checkpoints.entrySet().stream().forEach((entry)->{
            s.append(
                "%s\t%sms (+%sms)\n".formatted(
                    entry.getKey(),
                    Duration.between(this.startTime, entry.getValue()).toMillis(),
                    Duration.between(last.get(), entry.getValue()).toMillis())
            );
            last.set(entry.getValue());
        });
        return s.toString();

    }

    public  <T> T runAndStop(Supplier<T> s){
        T result = s.get();
        this.stop();
        System.out.println(this);
        return result;
    }
}
