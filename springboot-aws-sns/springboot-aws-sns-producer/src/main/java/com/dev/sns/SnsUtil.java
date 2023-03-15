package com.dev.sns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.dev.model.Properties;

import jakarta.annotation.PostConstruct;

@Component
public class SnsUtil {
	private static final Logger log = LogManager.getLogger(SnsUtil.class);

	@Autowired
	private Properties properties;

	private AmazonSNS amazonSNS;

	@PostConstruct
	private void postConstructor() {
		log.info("SNS Topic ARN: " + properties.getAwsSnsTopicArn());

		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(properties.getAwsAccessKey(), properties.getAwsSecretKey()));

		this.amazonSNS = AmazonSNSClientBuilder.standard().withRegion(properties.getAwsRegion())
				.withCredentials(awsCredentialsProvider).build();
	}

	public void sendSNSMessage(String message) {
		PublishRequest publishRequest = new PublishRequest().withMessage(message)
				.withTopicArn(properties.getAwsSnsTopicArn());
		PublishResult result = amazonSNS.publish(publishRequest);

		log.info("Message sent: " + result.getMessageId());
	}
}
