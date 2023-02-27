package com.dev.exe;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SimpleHandler implements RequestHandler<String, String> {
	@Override
	public String handleRequest(String input, Context context) {
//		LambdaLogger logger = context.getLogger();
		context.getLogger().log("Input: " + input);
		return "Hello World - " + input;
	}

}
