package com.aws_monitor.cpuUsage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.ec2.Ec2Client;

@Configuration
public class AwsClientsConfig {

    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.builder()
                .region(region())
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public Ec2Client ec2Client() {
        return Ec2Client.builder()
                .region(region())
                .credentialsProvider(credentialsProvider())
                .build();
    }

    private StaticCredentialsProvider credentialsProvider() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("Missing AWS credentials");
        }

        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    private Region region() {
        String region = System.getenv("AWS_REGION");
        if (region == null) {
            throw new IllegalStateException("Missing AWS_REGION");
        }
        return Region.of(region);
    }
}
