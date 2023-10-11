package com.kheefordev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class Properties {
	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.secret.key}")
	private String awsSecretKey;

	@Value("${aws.sqs.queue.url}")
	private String awsSqsQueueUrl;

	@Value("${aws.sqs.message.visibility.second}")
	private Integer awsSqsMessageVisibilitySecond;
}