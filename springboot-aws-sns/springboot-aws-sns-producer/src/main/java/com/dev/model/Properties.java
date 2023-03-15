package com.dev.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Properties {
	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.secret.key}")
	private String awsSecretKey;

	@Value("${aws.sns.topic.arn}")
	private String awsSnsTopicArn;
}