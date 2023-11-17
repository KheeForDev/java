package com.kheefordev.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.dynamodb.endpoint}")
	private String awsDynamodbEndpoint;

	@Value("${aws.dynamodb.access.key}")
	private String awsDynamodbAccessKey;

	@Value("${aws.dynamodb.secret.key}")
	private String awsDynamodbSecretKey;

	@Value("${aws.dynamodb.timeToLive}")
	private int awsDynamodbTimeToLive;

	public String getAwsRegion() {
		return awsRegion;
	}

	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	public String getAwsDynamodbEndpoint() {
		return awsDynamodbEndpoint;
	}

	public void setAwsDynamodbEndpoint(String awsDynamodbEndpoint) {
		this.awsDynamodbEndpoint = awsDynamodbEndpoint;
	}

	public String getAwsDynamodbAccessKey() {
		return awsDynamodbAccessKey;
	}

	public void setAwsDynamodbAccessKey(String awsDynamodbAccessKey) {
		this.awsDynamodbAccessKey = awsDynamodbAccessKey;
	}

	public String getAwsDynamodbSecretKey() {
		return awsDynamodbSecretKey;
	}

	public void setAwsDynamodbSecretKey(String awsDynamodbSecretKey) {
		this.awsDynamodbSecretKey = awsDynamodbSecretKey;
	}

	public int getAwsDynamodbTimeToLive() {
		return awsDynamodbTimeToLive;
	}

	public void setAwsDynamodbTimeToLive(int awsDynamodbTimeToLive) {
		this.awsDynamodbTimeToLive = awsDynamodbTimeToLive;
	}
}