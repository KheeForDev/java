package com.dev.kinesis;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.Record;
import com.dev.model.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Consumer implements CommandLineRunner {
	private static final Logger log = LogManager.getLogger(Consumer.class);

	@Autowired
	private Properties properties;

	@Override
	public void run(String... args) {
		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());

		AmazonKinesisClientBuilder amazonKinesisClientBuilder = AmazonKinesisClientBuilder.standard();
		amazonKinesisClientBuilder.setCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
		amazonKinesisClientBuilder.setRegion(properties.getAwsRegion());
		amazonKinesisClientBuilder.setClientConfiguration(new ClientConfiguration());

		AmazonKinesis amazonKinesis = amazonKinesisClientBuilder.build();

		String shardIterator;
		GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
		getShardIteratorRequest.setStreamName(properties.getAwsKinesisStream());
		getShardIteratorRequest.setShardId("shardId-000000000000");
		getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");

		String ShardIteratorType = properties.getAwsKinesisShardIteratorType();
		switch (ShardIteratorType) {
		case "AT_SEQUENCE_NUMBER":
			getShardIteratorRequest.setShardIteratorType("AT_SEQUENCE_NUMBER");
			getShardIteratorRequest.setStartingSequenceNumber(properties.getAwsKinesisSequenceNumber());
			break;
		case "AFTER_SEQUENCE_NUMBER":
			getShardIteratorRequest.setShardIteratorType("AFTER_SEQUENCE_NUMBER");
			getShardIteratorRequest.setStartingSequenceNumber(properties.getAwsKinesisSequenceNumber());
			break;
		case "AT_TIMESTAMP":
			getShardIteratorRequest.setShardIteratorType("AT_TIMESTAMP");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			Date date = null;
			try {
				date = sdf.parse(properties.getAwsKinesisTimestamp());
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
			getShardIteratorRequest.setTimestamp(date);
			break;
		case "TRIM_HORIZON":
			getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");
			break;
		case "LATEST":
			getShardIteratorRequest.setShardIteratorType("LATEST");
			break;
		}

		GetShardIteratorResult getShardIteratorResult = amazonKinesis.getShardIterator(getShardIteratorRequest);
		shardIterator = getShardIteratorResult.getShardIterator();

		List<Record> records;

		while (true) {
			// Create a new getRecordsRequest with an existing shardIterator
			// Set the maximum records to return to 25
			GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
			getRecordsRequest.setShardIterator(shardIterator);
			getRecordsRequest.setLimit(25);

			GetRecordsResult result = amazonKinesis.getRecords(getRecordsRequest);

			// Put the result into record list. The result can be empty.
			records = result.getRecords();

			for (Record record : records) {
				log.info("=============================================");
				try {
					log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(record));
					log.info(new String(record.getData().array(), "UTF-8"));
				} catch (JsonProcessingException | UnsupportedEncodingException e) {
					log.info(e.getMessage());
				}
				log.info("=============================================");
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}

			shardIterator = result.getNextShardIterator();
		}
	}

}
