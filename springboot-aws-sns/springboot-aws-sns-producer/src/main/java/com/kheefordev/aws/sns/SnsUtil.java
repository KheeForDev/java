package com.kheefordev.aws.sns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kheefordev.config.Properties;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
public class SnsUtil {
	private static final Logger LOG = LogManager.getLogger(SnsUtil.class);

	@Autowired
	private Properties properties;

	@Autowired
	private SnsClient snsClient;

	public void sendSNSMessage(String message) {
		PublishResponse publishResponse = snsClient
				.publish(PublishRequest.builder().topicArn(properties.getAwsSnsTopicArn()).message(message).build());
		
		LOG.info("Message published: {} with status code: {}", publishResponse.messageId(), publishResponse.sdkHttpResponse().statusCode());
	}
}
