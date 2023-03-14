package com.dev.sqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.dev.model.Properties;

import jakarta.annotation.PostConstruct;

@Component
public class SqsUtil {
	private static final Logger log = LogManager.getLogger(SqsUtil.class);

	@Autowired
	private Properties properties;

	private AmazonSQS amazonSQS;

	@PostConstruct
	private void postConstructor() {
		log.info("SQS URL: " + properties.getAwsSqsQueueUrl());

		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(properties.getAwsAccessKey(), properties.getAwsSecretKey()));

		this.amazonSQS = AmazonSQSClientBuilder.standard().withRegion(properties.getAwsRegion())
				.withCredentials(awsCredentialsProvider).build();
	}

	public void sendSQSMessage(String message) {
		log.info("Sending SQS message: " + message);
		SendMessageResult result = this.amazonSQS.sendMessage(properties.getAwsSqsQueueUrl(), message);
		log.info("SQS Message ID: " + result.getMessageId());
	}
	
	/**
	 * <p>
	 * withMessageGroupId - Message deduplication ID is the token used for
	 * deduplication of sent messages. If a message with a particular message
	 * deduplication ID is sent successfully, any messages sent with the same
	 * message deduplication ID are accepted successfully but aren't delivered
	 * during the 5-minute deduplication interval
	 * </p>
	 * 
	 * <p>
	 * withMessageDeduplicationId - MessageGroupId is the tag that specifies that a message
	 * belongs to a specific message group. Messages that belong to the same message
	 * group are always processed one by one, in a strict order relative to the
	 * message group (however, messages that belong to different message groups
	 * 
	 * If you need all messages in a FIFO queue to be delivered in strict order,
	 * just use the same MessageGroupId for all messages. You can name it
	 * "default-group". Having that will work just fine, but remember that a FIFO
	 * queue like that will not give out the next available message until the
	 * previously received message is deleted -> this is the way such queue
	 * guarantees processing in order. So you maximum throughput of a pure FIFO
	 * queue like that will be limited.
	 * </p>
	 * 
	 * <p>
	 * Reference:
	 * https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/using-messagededuplicationid-property.html
	 * https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/using-messagegroupid-property.html
	 * https://repost.aws/questions/QUidOETC17R2Cf5vTawtgBpA/can-someone-explain-the-point-of-message-group-id
	 * </p>
	 */
	public void sendSQSMessageFifo(String message) {
		log.info("Sending SQS message: " + message);
		SendMessageRequest smr = new SendMessageRequest(properties.getAwsSqsQueueUrl(), message).withMessageGroupId("default-group").withMessageDeduplicationId("1");
		SendMessageResult result = this.amazonSQS.sendMessage(smr);
		log.info("SQS Message ID: " + result.getMessageId());
	}
}
