package com.kheefordev.aws.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kheefordev.config.Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {
	private static final Logger LOG = LogManager.getLogger(DynamoDBConfig.class);

	@Autowired
	private Properties properties;

	@Bean
	public DynamoDbClient dynamoDbClient() {
		LOG.info("DynamoDbClient Initializing");
		AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider
				.create(AwsBasicCredentials.create(properties.getAwsAccessKey(), properties.getAwsSecretKey()));

		DynamoDbClient amazonDynamoDB = DynamoDbClient.builder().region(Region.of(properties.getAwsRegion()))
				.credentialsProvider(awsCredentialsProvider).build();
		LOG.info("DynamoDbClient Initialized");

		return amazonDynamoDB;
	}
}