package com.aws_monitor.cpuUsage.repository;

import org.springframework.stereotype.Repository;
import com.aws_monitor.cpuUsage.domain.CloudWatchStatistic;
import com.aws_monitor.cpuUsage.domain.CpuPoint;
import com.aws_monitor.cpuUsage.domain.CpuSeries;
import com.aws_monitor.cpuUsage.domain.SampleInterval;
import com.aws_monitor.cpuUsage.domain.TimeRange;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CloudWatchCpuRepository {

    private final CloudWatchClient cloudWatch;

    public CloudWatchCpuRepository(CloudWatchClient cloudWatch) {
        this.cloudWatch = cloudWatch;
    }

    public CpuSeries fetchCpuUtilization(
            String instanceId,
            TimeRange range,
            SampleInterval interval,
            CloudWatchStatistic statistic
    ) {

        GetMetricStatisticsRequest request =
                GetMetricStatisticsRequest.builder()
                        .namespace("AWS/EC2")
                        .metricName("CPUUtilization")
                        .dimensions(
                                Dimension.builder()
                                        .name("InstanceId")
                                        .value(instanceId)
                                        .build()
                        )
                        .startTime(range.from())
                        .endTime(range.to())
                        .period(interval.seconds())
                        .statistics(statistic.toAwsSdkStatistic())
                        .build();

        GetMetricStatisticsResponse response =
                cloudWatch.getMetricStatistics(request);

        Statistic awsStatistic = statistic.toAwsSdkStatistic();
        List<CpuPoint> points = response.datapoints().stream()
                .map(dp -> new CpuPoint(dp.timestamp(), extractValue(dp, awsStatistic)))
                .collect(Collectors.toList());

        return new CpuSeries(instanceId, points);
    }

    private Double extractValue(Datapoint datapoint, Statistic statistic) {
        return switch (statistic) {
            case AVERAGE -> datapoint.average();
            case MINIMUM -> datapoint.minimum();
            case MAXIMUM -> datapoint.maximum();
            default -> datapoint.average();
        };
    }
}