package com.aws_monitor.cpuUsage.api.dto;

public class CpuPointDto {
    public String timestamp;
    public double value;

    public CpuPointDto(String timestamp, double value ){
        this.timestamp = timestamp;
        this.value = value;
    }
}