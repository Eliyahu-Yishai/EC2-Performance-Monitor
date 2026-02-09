package com.aws_monitor.cpuUsage.domain;

import java.time.Instant;

public class CpuPoint {

    private final Instant timestamp;
    private final double value;

    public CpuPoint(Instant timestamp, double value) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp is required");
        }
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("CPU value must be between 0 and 100");
        }
        this.timestamp = timestamp;
        this.value = value;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public double value() {
        return value;
    }
}
