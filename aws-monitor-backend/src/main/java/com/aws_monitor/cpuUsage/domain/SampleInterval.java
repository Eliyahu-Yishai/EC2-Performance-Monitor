package com.aws_monitor.cpuUsage.domain;

public class SampleInterval {
    private final int seconds;

    private static final int MIN_SECONDS = 10;

    private SampleInterval(int seconds) {
        if (seconds <= 0)
            throw new IllegalArgumentException("Interval must be positive");

        if (seconds < MIN_SECONDS)
            throw new IllegalArgumentException("Interval too small");

        this.seconds = seconds;
    }

    public static SampleInterval ofSeconds(int seconds) {
        return new SampleInterval(seconds);
    }

    public int seconds() {
        return seconds;
    }
}
