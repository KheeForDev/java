package com.dev.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.firehose.stream}")
	private String awsFirehoseStream;

	@Value("${aws.firehose.access.key}")
	private String awsFirehoseAccessKey;

	@Value("${aws.firehose.secret.key}")
	private String awsFirehoseSecretKey;

	public String getAwsRegion() {
		return awsRegion;
	}

	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	public String getAwsFirehoseStream() {
		return awsFirehoseStream;
	}

	public void setAwsFirehoseStream(String awsFirehoseStream) {
		this.awsFirehoseStream = awsFirehoseStream;
	}

	public String getAwsFirehoseAccessKey() {
		return awsFirehoseAccessKey;
	}

	public void setAwsFirehoseAccessKey(String awsFirehoseAccessKey) {
		this.awsFirehoseAccessKey = awsFirehoseAccessKey;
	}

	public String getAwsFirehoseSecretKey() {
		return awsFirehoseSecretKey;
	}

	public void setAwsFirehoseSecretKey(String awsFirehoseSecretKey) {
		this.awsFirehoseSecretKey = awsFirehoseSecretKey;
	}
}
