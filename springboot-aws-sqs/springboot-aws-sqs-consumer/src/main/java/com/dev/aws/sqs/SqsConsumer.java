package com.dev.aws.sqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import com.dev.model.Properties;

@Service
public class SqsConsumer {
	private static final Logger log = LogManager.getLogger(SqsConsumer.class);

	@Autowired
	private Properties properties;

	@SqsListener(value = "https://sqs.ap-southeast-1.amazonaws.com/334927287776/khee-sqs-1")
	public void handleMessages(String message) {
		log.info("Message Body: {}", message);
	}

//	private final AmazonSQSAsync amazonSQSAsync;
//
//	public SqsConsumer(AmazonSQSAsync amazonSQSAsync) {
//		this.amazonSQSAsync = amazonSQSAsync;
//	}
//
//	@EventListener(ApplicationReadyEvent.class)
//	public void startPolling() {
//		PollingTask pollingTask = new PollingTask(amazonSQSAsync, properties.getAwsSqsQueueUrl());
//		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(pollingTask, 0, 5, TimeUnit.SECONDS);
//	}
//
//	private static class PollingTask implements Runnable {
//		private final AmazonSQSAsync amazonSQSAsync;
//		private final String queueUrl;
//
//		public PollingTask(AmazonSQSAsync amazonSQSAsync, String queueUrl) {
//			this.amazonSQSAsync = amazonSQSAsync;
//			this.queueUrl = queueUrl;
//		}
//
//		@Override
//		public void run() {
//			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl)
//					.withMaxNumberOfMessages(10).withWaitTimeSeconds(20);
//			List<Message> messages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
//			
//			log.info("===== BATCH ====");
//			if (!messages.isEmpty()) {
//				messages.forEach(message -> {
//					// process message
//					log.info("Message Body: {}", message.getBody());
//
//					RequestDto retrieveReqDto = null;
//					try {
//						retrieveReqDto = new ObjectMapper().readValue(message.getBody(), RequestDto.class);
//						log.info(new ObjectMapper().writeValueAsString(retrieveReqDto));
//					} catch (JsonProcessingException e) {
//						log.error(e.getMessage());
//					}
//
//					amazonSQSAsync.deleteMessage(queueUrl, message.getReceiptHandle());
//				});
//			}
//			log.info("===== BATCH ====");
//		}
//	}
}
