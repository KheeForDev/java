package com.dev.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.dev.model.Properties;

@Configuration
@EnableSqs
public class AwsConfig {
	@Autowired
	private Properties properties;

	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync() {
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(properties.getAwsAccessKey(), properties.getAwsSecretKey()));

		return AmazonSQSAsyncClientBuilder.standard().withRegion(properties.getAwsRegion())
				.withCredentials(awsCredentialsProvider).build();
	}

	@Bean
	public QueueMessageHandler queueMessageHandler() {
		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setAmazonSqs(amazonSQSAsync());
		return factory.createQueueMessageHandler();
	}

	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
			QueueMessageHandler queueMessageHandler) {
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		factory.setAmazonSqs(amazonSQSAsync());
		factory.setAutoStartup(true);
		factory.setMaxNumberOfMessages(20);
		factory.setWaitTimeOut(20); // Set the wait timeout to 10 minutes (in seconds)
		factory.setVisibilityTimeout(60);
		factory.setQueueMessageHandler(queueMessageHandler);
		return factory;
	}
}
