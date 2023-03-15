package com.dev.aws.sqs;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.dev.model.Properties;
import com.dev.model.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SqsConsumer {
	private static final Logger log = LogManager.getLogger(SqsConsumer.class);

	@Autowired
	private Properties properties;

	private final AmazonSQSAsync amazonSQSAsync;

	public SqsConsumer(AmazonSQSAsync amazonSQSAsync) {
		this.amazonSQSAsync = amazonSQSAsync;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startPolling() {
		PollingTask pollingTask = new PollingTask(amazonSQSAsync, properties.getAwsSqsQueueUrl());
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(pollingTask, 0, 5, TimeUnit.SECONDS);
	}

	private static class PollingTask implements Runnable {
		private final AmazonSQSAsync amazonSQSAsync;
		private final String queueUrl;

		public PollingTask(AmazonSQSAsync amazonSQSAsync, String queueUrl) {
			this.amazonSQSAsync = amazonSQSAsync;
			this.queueUrl = queueUrl;
		}

		@Override
		public void run() {
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl)
					.withMaxNumberOfMessages(10).withWaitTimeSeconds(20);
			List<Message> messages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
			
			log.info("===== BATCH ====");
			if (!messages.isEmpty()) {
				messages.forEach(message -> {
					// process message
					log.info("Message Body: {}", message.getBody());

					RequestDto retrieveReqDto = null;
					try {
						retrieveReqDto = new ObjectMapper().readValue(message.getBody(), RequestDto.class);
						log.info(new ObjectMapper().writeValueAsString(retrieveReqDto));
					} catch (JsonProcessingException e) {
						log.error(e.getMessage());
					}

					amazonSQSAsync.deleteMessage(queueUrl, message.getReceiptHandle());
				});
			}
			log.info("===== BATCH ====");
		}
	}
}
