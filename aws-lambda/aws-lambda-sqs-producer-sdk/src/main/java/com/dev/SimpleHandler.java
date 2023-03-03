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

		String body = requestMap.get("body").toString();
		logger.log("[Request]: " + body);
//		RequestDto requestDto = new Gson().fromJson(body, RequestDto.class);

		String sqsResponse = sendSQSMessage(body);
		logger.log("[SQS Response]: " + sqsResponse);

		return sqsResponse;
	}

	private String sendSQSMessage(String data) {
		SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
				.queueUrl("https://sqs.ap-southeast-1.amazonaws.com/334927287776/oeo-cmt").messageBody(data).build());
		return response.toString();
	}
}