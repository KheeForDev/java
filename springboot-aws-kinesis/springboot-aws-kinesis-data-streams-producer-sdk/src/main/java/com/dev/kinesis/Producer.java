package com.dev.kinesis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.dev.model.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Producer {
	private static final Logger log = LogManager.getLogger(Producer.class);

	private AmazonKinesisClientBuilder amazonKinesisClientBuilder = null;

	@Autowired
	private Properties properties;

	public void putIntoKinesis(String payload) {
		AmazonKinesis kinesisClient = getAmazonKinesisClientBuilder().build();

		PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
		putRecordsRequest.setStreamName(properties.getAwsKinesisStream());
		List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();

		PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
		putRecordsRequestEntry.setData(ByteBuffer.wrap(String.valueOf(payload).getBytes()));
		putRecordsRequestEntry.setPartitionKey(String.valueOf(UUID.randomUUID()));
		putRecordsRequestEntryList.add(putRecordsRequestEntry);

		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRecordsRequest);

		try {
			log.info("Put Result {}",
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(putRecordsResult));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	}

	private AmazonKinesisClientBuilder getAmazonKinesisClientBuilder() {
		if (amazonKinesisClientBuilder == null) {
			log.info("Initialize AWS Kinesis Client Builder");

			BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(properties.getAwsKinesisAccessKey(),
					properties.getAwsKinesisSecretKey());

			amazonKinesisClientBuilder = AmazonKinesisClientBuilder.standard();
			amazonKinesisClientBuilder.setCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
			amazonKinesisClientBuilder.setRegion(properties.getAwsRegion());
			amazonKinesisClientBuilder.setClientConfiguration(new ClientConfiguration());
		}

		return amazonKinesisClientBuilder;
	}
}
