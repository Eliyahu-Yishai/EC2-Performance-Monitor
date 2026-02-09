package com.aws_monitor.cpuUsage.api.dto;

public class CpuQueryRequest {
    public String ip;
    public int minutesBack;
    public int intervalSeconds;
    public String statistic;

    public CpuQueryRequest(String ip, int minutesBack, int intervalSeconds, String statistic){
        this.ip = ip;
        this.minutesBack = minutesBack;
        this.intervalSeconds = intervalSeconds;
        this.statistic = statistic;
    }
}
