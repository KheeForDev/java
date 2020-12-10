package com.dev.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	@Value("${application.name}")
	private String applicationName;

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.kinesis.stream}")
	private String awsKinesisStream;

	@Value("${aws.kinesis.access.key}")
	private String awsKinesisAccessKey;

	@Value("${aws.kinesis.secret.key}")
	private String awsKinesisSecretKey;

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getAwsRegion() {
		return awsRegion;
	}

	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	public String getAwsKinesisStream() {
		return awsKinesisStream;
	}

	public void setAwsKinesisStream(String awsKinesisStream) {
		this.awsKinesisStream = awsKinesisStream;
	}

	public String getAwsKinesisAccessKey() {
		return awsKinesisAccessKey;
	}

	public void setAwsKinesisAccessKey(String awsKinesisAccessKey) {
		this.awsKinesisAccessKey = awsKinesisAccessKey;
	}

	public String getAwsKinesisSecretKey() {
		return awsKinesisSecretKey;
	}

	public void setAwsKinesisSecretKey(String awsKinesisSecretKey) {
		this.awsKinesisSecretKey = awsKinesisSecretKey;
	}
}
