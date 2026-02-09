package com.aws_monitor.cpuUsage.domain;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.services.cloudwatch.model.Statistic;

public class CloudWatchStatistic {
    private final Statistic statistic;

    private CloudWatchStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public static CloudWatchStatistic fromString(String value) {
        if (value == null || value.isBlank()) {
            return new CloudWatchStatistic(Statistic.AVERAGE);
        }

        String normalized = value.trim().toUpperCase();

        return switch (normalized) {
            case "AVERAGE" -> new CloudWatchStatistic(Statistic.AVERAGE);
            case "MINIMUM" -> new CloudWatchStatistic(Statistic.MINIMUM);
            case "MAXIMUM" -> new CloudWatchStatistic(Statistic.MAXIMUM);
            default -> new CloudWatchStatistic(Statistic.AVERAGE);
        };
    }

    public Statistic toAwsSdkStatistic() {
        return statistic;
    }

    public String getValue() {
        return statistic.toString();
    }
}
