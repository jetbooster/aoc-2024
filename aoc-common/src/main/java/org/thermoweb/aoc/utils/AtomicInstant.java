package org.thermoweb.aoc.utils;

import java.time.Instant;

public class AtomicInstant {
    Instant instant;

    public Instant get() {
        return instant;
    }

    public void set(Instant instant) {
        this.instant = instant;
    }
    AtomicInstant(Instant i){
        this.instant = i;
    }
    AtomicInstant(){
        this.instant = Instant.now();
    }
}
