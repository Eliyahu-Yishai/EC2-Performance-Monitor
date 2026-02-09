package com.aws_monitor.cpuUsage.api.dto;
import java.util.List;

public class CpuSeriesResponse {
    public String instanceId;
    public List<CpuPointDto> points;


    public CpuSeriesResponse(String instanceId, List<CpuPointDto> points) {
        this.instanceId = instanceId;
        this.points = points;
    }
}