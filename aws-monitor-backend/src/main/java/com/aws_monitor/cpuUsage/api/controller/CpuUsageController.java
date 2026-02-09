package com.aws_monitor.cpuUsage.api.controller;

import com.aws_monitor.cpuUsage.api.dto.CpuPointDto;
import com.aws_monitor.cpuUsage.api.dto.CpuQueryRequest;
import com.aws_monitor.cpuUsage.api.dto.CpuSeriesResponse;
import com.aws_monitor.cpuUsage.domain.CpuSeries;
import com.aws_monitor.cpuUsage.domain.SampleInterval;
import com.aws_monitor.cpuUsage.domain.TimeRange;
import com.aws_monitor.cpuUsage.service.CpuUsageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cpu")
public class CpuUsageController {

    private final CpuUsageService service;

    public CpuUsageController(CpuUsageService service) {
        this.service = service;
    }

    @PostMapping("/usage")
    public CpuSeriesResponse cpuUsage(@RequestBody CpuQueryRequest request) {
        TimeRange range = TimeRange.lastMinutes(request.minutesBack);
        SampleInterval interval = SampleInterval.ofSeconds(request.intervalSeconds);
        String statistic = request.statistic != null ? request.statistic : "Average";

        CpuSeries series = service.getCpuUsage(request.ip, range, interval, statistic);

        List<CpuPointDto> points = series.points().stream()
                .map(p -> new CpuPointDto(p.timestamp().toString(), p.value()))
                .toList();

        return new CpuSeriesResponse(series.instanceId(), points);
    }
}
