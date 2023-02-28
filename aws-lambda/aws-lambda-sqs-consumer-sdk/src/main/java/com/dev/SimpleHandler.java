package com.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class SimpleHandler {
	public void handleRequest(SQSEvent input, Context context) {
		LambdaLogger logger = context.getLogger();

		logger.log("Number of record(s) to process: " + String.valueOf(input.getRecords().size()));

		input.getRecords().forEach(record -> {
			logger.log(record.getBody());
		});
	}
}