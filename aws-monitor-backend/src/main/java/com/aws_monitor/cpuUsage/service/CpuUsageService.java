package com.aws_monitor.cpuUsage.service;

import com.aws_monitor.common.errors.NotFoundException;
import com.aws_monitor.common.errors.ValidationException;
import com.aws_monitor.cpuUsage.domain.CloudWatchStatistic;
import com.aws_monitor.cpuUsage.domain.CpuSeries;
import com.aws_monitor.cpuUsage.domain.SampleInterval;
import com.aws_monitor.cpuUsage.domain.TimeRange;
import com.aws_monitor.cpuUsage.repository.CloudWatchCpuRepository;
import com.aws_monitor.cpuUsage.repository.Ec2InstanceResolver;
import org.springframework.stereotype.Service;
import com.aws_monitor.safety.Ec2SafetyGuard;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CpuUsageService {
    private static final long MAX_POINTS = 5_000;

    private final Ec2InstanceResolver instanceResolver;
    private final CloudWatchCpuRepository cpuRepository;
    // private final Ec2SafetyGuard safetyGuard; // for future

    public CpuUsageService(
            Ec2InstanceResolver instanceResolver,
            CloudWatchCpuRepository cpuRepository
//            Ec2SafetyGuard safetyGuard
    ) {
        this.instanceResolver = instanceResolver;
        this.cpuRepository = cpuRepository;
//        this.safetyGuard = safetyGuard;
    }

    public CpuSeries getCpuUsage(String ip, TimeRange range, SampleInterval interval, String statisticStr) {
        validateInputs(ip, range, interval);

        CloudWatchStatistic statistic = CloudWatchStatistic.fromString(statisticStr);
        String instanceId = resolveInstanceId(ip);

        return fetchCpuSeries(instanceId, range, interval, statistic);
    }

    private void validateInputs(String ip, TimeRange range, SampleInterval interval) {
        if (ip == null || ip.isBlank()) {
            throw new ValidationException("ip is required");
        }
        String trimmed = ip.trim();
        if (!isValidIp(trimmed)) {
            throw new ValidationException("invalid ip format: " + trimmed);
        }

        if (range == null) {
            throw new ValidationException("range is required");
        }
        if (interval == null) {
            throw new ValidationException("interval is required");
        }

        Instant start = range.from();
        Instant end = range.to();

        if (start == null || end == null) {
            throw new ValidationException("range must include start and end");
        }
        if (!end.isAfter(start)) {
            throw new ValidationException("range end must be after start");
        }

        long seconds = end.getEpochSecond() - start.getEpochSecond();
        long approxPoints = Math.max(1, seconds / interval.seconds());

        if (approxPoints > MAX_POINTS) {
            throw new ValidationException(
                    "too many samples requested (" + approxPoints + "). Increase interval or reduce time range."
            );
        }
    }

    private String resolveInstanceId(String ip) {
        String instanceId = instanceResolver.resolveByIp(ip);
        if (instanceId == null || instanceId.isBlank()) {
            throw new NotFoundException("No EC2 instance found for IP: " + ip);
        }
        return instanceId;
    }

    private CpuSeries fetchCpuSeries(String instanceId, TimeRange range, SampleInterval interval, CloudWatchStatistic statistic) {
        CpuSeries series = cpuRepository.fetchCpuUtilization(instanceId, range, interval, statistic);

        if (series == null) {
            throw new NotFoundException("No CPU metrics returned for instance: " + instanceId);
        }
        return series;
    }

    private boolean isValidIp(String ip) {
            String regex =  "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
    }
}
