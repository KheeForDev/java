package com.dev.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	@Value("${aws.kinesis.region}")
	private String awsKinesisRegion;

	@Value("${aws.kinesis.stream}")
	private String awsKinesisStream;

	@Value("${aws.kinesis.access.key}")
	private String awsKinesisAccessKey;

	@Value("${aws.kinesis.secret.key}")
	private String awsKinesisSecretKey;

	public String getAwsKinesisRegion() {
		return awsKinesisRegion;
	}

	public void setAwsKinesisRegion(String awsKinesisRegion) {
		this.awsKinesisRegion = awsKinesisRegion;
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
