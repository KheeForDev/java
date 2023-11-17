package com.kheefordev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties
public class Properties {
	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.secret.key}")
	private String awsSecretKey;

	@Value("${aws.dynamodb.table}")
	private String awsDynamodbTable;

	@Value("${aws.dynamodb.timeToLive}")
	private int awsDynamodbTimeToLive;
}
