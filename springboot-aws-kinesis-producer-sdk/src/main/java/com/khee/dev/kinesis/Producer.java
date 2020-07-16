package com.khee.dev.kinesis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.kinesis.model.PutRecordsResultEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khee.dev.model.Properties;

@Component
public class Producer {

	@Autowired
	private Properties properties;

	public void putIntoKinesis(String payload) {
		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();

		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());

		clientBuilder.setCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
		clientBuilder.setRegion(properties.getAwsKinesisRegion());
		clientBuilder.setClientConfiguration(new ClientConfiguration());

		AmazonKinesis kinesisClient = clientBuilder.build();

		PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
		putRecordsRequest.setStreamName(properties.getAwsKinesisStream());
		List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();

		PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
		putRecordsRequestEntry.setData(ByteBuffer.wrap(String.valueOf(payload).getBytes()));
		putRecordsRequestEntry.setPartitionKey("partitionKey");
		putRecordsRequestEntryList.add(putRecordsRequestEntry);

		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRecordsRequest);

		try {
			System.out.println("Put Result "
					+ new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(putRecordsResult));
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}

		while (putRecordsResult.getFailedRecordCount() > 0) {
			final List<PutRecordsRequestEntry> failedRecordsList = new ArrayList<>();
			final List<PutRecordsResultEntry> putRecordsResultEntryList = putRecordsResult.getRecords();
			for (int i = 0; i < putRecordsResultEntryList.size(); i++) {
				final PutRecordsRequestEntry putRecordRequestEntry = putRecordsRequestEntryList.get(i);
				final PutRecordsResultEntry putRecordsResultEntry = putRecordsResultEntryList.get(i);
				if (putRecordsResultEntry.getErrorCode() != null) {
					failedRecordsList.add(putRecordRequestEntry);
				}
			}
			putRecordsRequestEntryList = failedRecordsList;
			putRecordsRequest.setRecords(putRecordsRequestEntryList);
			putRecordsResult = kinesisClient.putRecords(putRecordsRequest);

			try {
				System.out.println("Put Result "
						+ new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(putRecordsResult));
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
