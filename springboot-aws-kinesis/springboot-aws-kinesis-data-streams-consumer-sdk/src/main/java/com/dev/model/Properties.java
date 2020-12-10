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

	@Value("${aws.kinesis.shard.iterator.type}")
	private String awsKinesisShardIteratorType;

	@Value("${aws.kinesis.sequence.number}")
	private String awsKinesisSequenceNumber;

	@Value("${aws.kinesis.timestamp}")
	private String awsKinesisTimestamp;

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

	public String getAwsKinesisShardIteratorType() {
		return awsKinesisShardIteratorType;
	}

	public void setAwsKinesisShardIteratorType(String awsKinesisShardIteratorType) {
		this.awsKinesisShardIteratorType = awsKinesisShardIteratorType;
	}

	public String getAwsKinesisSequenceNumber() {
		return awsKinesisSequenceNumber;
	}

	public void setAwsKinesisSequenceNumber(String awsKinesisSequenceNumber) {
		this.awsKinesisSequenceNumber = awsKinesisSequenceNumber;
	}

	public String getAwsKinesisTimestamp() {
		return awsKinesisTimestamp;
	}

	public void setAwsKinesisTimestamp(String awsKinesisTimestamp) {
		this.awsKinesisTimestamp = awsKinesisTimestamp;
	}
}
