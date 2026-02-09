package com.aws_monitor.cpuUsage.repository;
import com.aws_monitor.common.errors.NotFoundException;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ec2.model.Filter;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Ec2InstanceResolver {
    private final Ec2Client ec2;

    public Ec2InstanceResolver(Ec2Client ec2) {
        this.ec2 = ec2;
    }

    public String resolveByIp(String ip) {

        if (ip == null || ip.isBlank())
            throw new IllegalArgumentException("IP cannot be empty");

        // 1. private ip
        List<Instance> instances = findInstances("private-ip-address", ip);

        // 2. public ip
        if (instances.isEmpty())
            instances = findInstances("ip-address", ip);

        if (instances.isEmpty())
            throw new NotFoundException("No EC2 instance found for ip=" + ip);

        return instances.get(0).instanceId();
    }


    /**
     * Queries AWS EC2 for instances matching the given IP filter.
     * Flattens the AWS reservations structure into a simple list of instances.
     *
     * @param filterName EC2 filter name (e.g. "private-ip-address" or "ip-address")
     * @param ip         IP address to search for
     * @return list of matching EC2 instances (if exists)
     */
    private List<Instance> findInstances(String filterName, String ip) {

        // Build EC2 filter - find instances where 'filterName' == ip
        Filter filter = Filter.builder().name(filterName).values(ip).build();

        // Create DescribeInstances request using the filter
        DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .filters(filter)
                .build();

        // Send request to AWS EC2 and receive matching instances grouped by reservations
        DescribeInstancesResponse response = ec2.describeInstances(request);

        List<Instance> result = new ArrayList<>();

        // Extract all instances from the AWS response into a single list
        for (Reservation res : response.reservations()) {
            result.addAll(res.instances());
        }

        return result;
    }
}
