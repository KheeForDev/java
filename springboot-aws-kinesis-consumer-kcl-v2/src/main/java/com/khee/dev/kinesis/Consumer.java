package com.khee.dev.kinesis;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.khee.dev.model.Properties;

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

	@Autowired
	private Properties properties;

	private KinesisAsyncClient kinesisClient;
	private DynamoDbAsyncClient dynamoClient;
	private CloudWatchAsyncClient cloudWatchClient;
	private Region region = Region.AP_SOUTHEAST_1;
	private ConfigsBuilder configsBuilder;

	@Override
	public void run(String... args) throws Exception {
//		ExecutorService executor = Executors.newFixedThreadPool(1);
//		CallableKinesisConsumer callableKinesisConsumer = new CallableKinesisConsumer();
//		executor.submit(callableKinesisConsumer);

		AwsCredentials credentials = AwsBasicCredentials.create(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());
		
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

		this.kinesisClient = KinesisAsyncClient.builder().credentialsProvider(credentialsProvider).region(region)
				.build();
		this.dynamoClient = DynamoDbAsyncClient.builder().credentialsProvider(credentialsProvider).region(region).build();
		this.cloudWatchClient = CloudWatchAsyncClient.builder().credentialsProvider(credentialsProvider).region(region).build();
		configsBuilder = new ConfigsBuilder(properties.getAwsKinesisStream(), properties.getApplicationName(),
				kinesisClient, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(),
				new RecordProcessorFactory());

		Scheduler scheduler = new Scheduler(configsBuilder.checkpointConfig(), configsBuilder.coordinatorConfig(),
				configsBuilder.leaseManagementConfig(), configsBuilder.lifecycleConfig(),
				configsBuilder.metricsConfig(), configsBuilder.processorConfig(), configsBuilder.retrievalConfig());

		Thread schedulerThread = new Thread(scheduler);
		schedulerThread.setDaemon(true);
		schedulerThread.start();
	}
}
