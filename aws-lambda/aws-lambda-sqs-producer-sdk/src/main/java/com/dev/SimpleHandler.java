package com.dev;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.dev.model.RequestDto;
import com.google.gson.Gson;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public class SimpleHandler {
	private SqsClient sqsClient = SqsClient.builder().region(Region.of("ap-southeast-1")).build();

	public String handleRequest(Map<String, Object> requestMap, Context context) {
		LambdaLogger logger = context.getLogger();
		RequestDto requestDto = null;

		String body = requestMap.get("body").toString();
		logger.log("[Request]: " + body);
		requestDto = new Gson().fromJson(body, RequestDto.class);

		String sqsResponse = sendSQSMessage(body, requestDto.getMessageId());
		logger.log("[SQS Response]: " + sqsResponse);

		return sqsResponse;
	}

	/**
	 * <p>
	 * messageDeduplicationId - Message deduplication ID is the token used for
	 * deduplication of sent messages. If a message with a particular message
	 * deduplication ID is sent successfully, any messages sent with the same
	 * message deduplication ID are accepted successfully but aren't delivered
	 * during the 5-minute deduplication interval
	 * </p>
	 * 
	 * <p>
	 * messageGroupId - MessageGroupId is the tag that specifies that a message
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
	private String sendSQSMessage(String data, String messageId) {
		SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
				.queueUrl("https://sqs.ap-southeast-1.amazonaws.com/334927287776/sqs-test.fifo").messageBody(data)
				.messageGroupId("default-group").messageDeduplicationId(messageId).build());
		return response.toString();
	}
}