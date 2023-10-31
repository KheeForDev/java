package com.kheefordev.aws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kheefordev.config.Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsClientConfig {
	private static final Logger LOG = LogManager.getLogger(SnsClientConfig.class);

	@Autowired
	private Properties properties;

	@Bean
	public SnsClient snsClient() {
		LOG.info("SnsClient Initializing");
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(properties.getAwsAccessKey(),
				properties.getAwsSecretKey());

		SnsClient snsClient = SnsClient.builder().region(Region.of(properties.getAwsRegion()))
				.credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build();
		LOG.info("SnsClient Initialized");

		return snsClient;
	}
}
