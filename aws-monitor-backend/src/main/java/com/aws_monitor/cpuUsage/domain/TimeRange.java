package com.aws_monitor.cpuUsage.domain;

import java.time.Instant;
import java.time.Duration;

public class TimeRange {
    private final Instant from;
    private final Instant to;

    private TimeRange(Instant from, Instant to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("Time range cannot be null");

        if (!from.isBefore(to))
            throw new IllegalArgumentException("From must be before to");

        if (to.isAfter(Instant.now()))
            throw new IllegalArgumentException("Time range cannot be in the future");

        this.from = from;
        this.to = to;
    }

    public static TimeRange lastMinutes(int minutesBack){
        if(minutesBack <= 0){
            throw new IllegalArgumentException("Minutes back must be positive");
        }

        Instant now = Instant.now();
        Instant from = now.minus(Duration.ofMinutes(minutesBack));
        return new TimeRange(from,now);
    }

    public Instant from(){
            return this.from;
    }

    public Instant to(){
        return this.to;
    }
}
