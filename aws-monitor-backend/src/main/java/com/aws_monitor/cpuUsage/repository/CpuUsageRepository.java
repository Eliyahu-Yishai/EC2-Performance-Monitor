package com.aws_monitor.cpuUsage.repository;

import com.aws_monitor.cpuUsage.domain.CloudWatchStatistic;
import com.aws_monitor.cpuUsage.domain.CpuSeries;
import com.aws_monitor.cpuUsage.domain.SampleInterval;
import com.aws_monitor.cpuUsage.domain.TimeRange;
import org.springframework.stereotype.Repository;

@Repository
public class CpuUsageRepository {

    private final CloudWatchCpuRepository cloudWatchCpuRepository;

    public CpuUsageRepository(CloudWatchCpuRepository cloudWatchCpuRepository) {
        this.cloudWatchCpuRepository = cloudWatchCpuRepository;
    }

    public CpuSeries getCpuUsage(
            String instanceId,
            TimeRange range,
            SampleInterval interval,
            CloudWatchStatistic statistic
    ) {
        return cloudWatchCpuRepository.fetchCpuUtilization(
                instanceId,
                range,
                interval,
                statistic
        );
    }
}
