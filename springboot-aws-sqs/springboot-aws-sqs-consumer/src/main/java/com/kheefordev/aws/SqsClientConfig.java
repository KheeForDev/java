package com.kheefordev.aws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kheefordev.config.Properties;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsClientConfig {
	private static final Logger LOG = LogManager.getLogger(SqsClientConfig.class);

	@Autowired
	private Properties properties;

	@Bean
	public SqsAsyncClient sqsAsyncClient() {
		LOG.info("SqsAsyncClient Initializing");
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(properties.getAwsAccessKey(),
				properties.getAwsSecretKey());

		SqsAsyncClient sqsAsyncClient = SqsAsyncClient.builder().region(Region.of(properties.getAwsRegion()))
				.credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build();
		LOG.info("SqsAsyncClient Initialized");

		return sqsAsyncClient;
	}

	@Bean
	public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
		LOG.info("SqsMessageListenerContainerFactory Initializing");
		SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory = SqsMessageListenerContainerFactory
				.builder().configure(options -> options.acknowledgementMode(AcknowledgementMode.MANUAL))
				.sqsAsyncClient(sqsAsyncClient()).build();
		LOG.info("SqsMessageListenerContainerFactory Initialized");

		return defaultSqsListenerContainerFactory;
	}
}