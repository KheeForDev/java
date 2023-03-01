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
	public SQSBatchResponse handleRequest(SQSEvent input, Context context) {
		LambdaLogger logger = context.getLogger();
		List<SQSBatchResponse.BatchItemFailure> batchItemFailureList = new ArrayList<>();

		logger.log("Number of record(s) to process: " + String.valueOf(input.getRecords().size()));

		input.getRecords().forEach(record -> {
			try {

				String msg = record.getBody();
				logger.log(msg);

				MessageDto messageDto = new Gson().fromJson(msg, MessageDto.class);

				if (messageDto.getName().toLowerCase().contains("fail")) {
					throw new RuntimeException("Unable to process message");
				}
			} catch (RuntimeException rte) {
				logger.log("[ERROR]: " + rte.getMessage());
				batchItemFailureList.add(new SQSBatchResponse.BatchItemFailure(record.getMessageId()));
			}
		});

		return new SQSBatchResponse(batchItemFailureList);
	}
}