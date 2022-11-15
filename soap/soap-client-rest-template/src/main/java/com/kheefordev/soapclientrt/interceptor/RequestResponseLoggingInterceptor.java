package com.kheefordev.soapclientrt.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		logResponse(response);
		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
//        if (log.isDebugEnabled()) {
		System.out.println("===========================request begin================================================");
		System.out.println("URI         : " + request.getURI());
		System.out.println("Method      : " + request.getMethod());
		System.out.println("Headers     : " + request.getHeaders());
		System.out.println("Request body: " + new String(body, "UTF-8"));
		System.out.println("==========================request end================================================");
//        }
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
//        if (log.isDebugEnabled()) {
		System.out.println("============================response begin==========================================");
		System.out.println("Status code  : " + response.getStatusCode());
		System.out.println("Status text  : " + response.getStatusText());
		System.out.println("Headers      : " + response.getHeaders());
		System.out.println("Response body: " + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		System.out.println("=======================response end=================================================");
//        }
	}
}