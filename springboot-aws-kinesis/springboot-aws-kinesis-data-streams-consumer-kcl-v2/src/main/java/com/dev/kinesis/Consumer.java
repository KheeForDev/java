package com.dev.kinesis;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dev.model.Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

@Component
public class Consumer implements CommandLineRunner {
	private KinesisAsyncClient kinesisClient;
	private DynamoDbAsyncClient dynamoClient;
	private CloudWatchAsyncClient cloudWatchClient;
//	private Region region = Region.AP_SOUTHEAST_1;
	private ConfigsBuilder configsBuilder;

	@Autowired
	private Properties properties;

	@Autowired
	private RecordProcessorFactory recordProcessorFactory;

	@Override
	public void run(String... args) throws Exception {
		Region region = Region.of(properties.getAwsRegion());

		AwsCredentials awsCredentials = AwsBasicCredentials.create(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());

		AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);

		this.kinesisClient = KinesisAsyncClient.builder().credentialsProvider(awsCredentialsProvider).region(region)
				.build();
		this.dynamoClient = DynamoDbAsyncClient.builder().credentialsProvider(awsCredentialsProvider).region(region)
				.build();
		this.cloudWatchClient = CloudWatchAsyncClient.builder().credentialsProvider(awsCredentialsProvider)
				.region(region).build();
		configsBuilder = new ConfigsBuilder(properties.getAwsKinesisStream(), properties.getApplicationName(),
				kinesisClient, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(), recordProcessorFactory);

		Scheduler scheduler = new Scheduler(configsBuilder.checkpointConfig(), configsBuilder.coordinatorConfig(),
				configsBuilder.leaseManagementConfig(), configsBuilder.lifecycleConfig(),
				configsBuilder.metricsConfig(), configsBuilder.processorConfig(), configsBuilder.retrievalConfig());

		Thread schedulerThread = new Thread(scheduler);
		schedulerThread.setDaemon(true);
		schedulerThread.start();
	}
}
