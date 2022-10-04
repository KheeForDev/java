package com.dev.dynamodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.dev.model.Properties;

@Configuration
public class DynamoDBConfiguration {

	@Autowired
	private Properties properties;

	@Bean
	public DynamoDBMapper dynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());
	}

	private AmazonDynamoDB buildAmazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						properties.getAwsDynamodbEndpoint(), properties.getAwsRegion()))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
						properties.getAwsDynamodbAccessKey(), properties.getAwsDynamodbSecretKey())))
				.build();
	}
}