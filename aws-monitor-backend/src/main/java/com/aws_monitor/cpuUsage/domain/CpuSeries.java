package com.aws_monitor.cpuUsage.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CpuSeries {

    private final String instanceId;
    private final List<CpuPoint> points;

    public CpuSeries(String instanceId, List<CpuPoint> points) {
        if (instanceId == null || instanceId.isBlank()) {
            throw new IllegalArgumentException("instanceId is required");
        }
        if (points == null) {
            throw new IllegalArgumentException("points is required");
        }

        List<CpuPoint> copy = new ArrayList<>(points);
        if (copy.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("points cannot contain null");
        }

        copy.sort(Comparator.comparing(CpuPoint::timestamp));

        this.instanceId = instanceId;
        this.points = List.copyOf(copy);
    }

    public String instanceId() {
        return instanceId;
    }

    public List<CpuPoint> points() {
        return points;
    }
}
