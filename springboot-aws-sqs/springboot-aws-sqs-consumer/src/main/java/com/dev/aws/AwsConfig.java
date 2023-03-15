package com.dev.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.dev.model.Properties;

@Component
public class AwsConfig {
	@Autowired
	private Properties properties;

	@Bean
	public AmazonSQSAsync amazonSQSAsync() {
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(properties.getAwsAccessKey(), properties.getAwsSecretKey()));

		return AmazonSQSAsyncClientBuilder.standard().withRegion(properties.getAwsRegion())
				.withCredentials(awsCredentialsProvider).build();
	}
}
