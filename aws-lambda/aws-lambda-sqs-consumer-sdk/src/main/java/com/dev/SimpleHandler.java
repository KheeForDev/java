package com.dev;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.dev.model.MessageDto;
import com.google.gson.Gson;

public class SimpleHandler {
	public void handleRequest(SQSEvent input, Context context) {
		LambdaLogger logger = context.getLogger();

		logger.log("Number of record(s) to process: " + String.valueOf(input.getRecords().size()));

		input.getRecords().forEach(record -> {
			String msg = record.getBody();
			logger.log(msg);
		});
	}

	/**
	 * <p>
	 * This method is used to handle batch record(s) with partial successful
	 * message. Using SQSBatchResponse to return messageId that failed to process
	 * and put into a Dead-Letter Queue (DQL) for further processing or
	 * investigation.
	 * </p>
	 */
	public SQSBatchResponse handleRequestWithFailure(SQSEvent input, Context context) {
		LambdaLogger logger = context.getLogger();
		List<SQSBatchResponse.BatchItemFailure> batchItemFailureList = new ArrayList<>();

		logger.log("Number of record(s) to process: " + String.valueOf(input.getRecords().size()));

		input.getRecords().forEach(record -> {
			String msg = record.getBody();
			logger.log(msg);

			MessageDto messageDto = new Gson().fromJson(msg, MessageDto.class);

			if (messageDto.getName().toLowerCase().contains("fail")) {
				batchItemFailureList.add(new SQSBatchResponse.BatchItemFailure(record.getMessageId()));
				throw new RuntimeException("Unable to process message");
			}
		});

		logger.log("Number of failed record(s): " + String.valueOf(batchItemFailureList.size()));

		return new SQSBatchResponse(batchItemFailureList);
	}
}