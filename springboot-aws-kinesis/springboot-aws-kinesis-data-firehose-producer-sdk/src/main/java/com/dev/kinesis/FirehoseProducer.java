package com.dev.kinesis;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import com.dev.model.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FirehoseProducer {
	private static final Logger log = LogManager.getLogger(FirehoseProducer.class);

	@Autowired
	private Properties properties;

	public void putIntoFirehose(String payload) {
		AmazonKinesisFirehoseClientBuilder clientBuilder = AmazonKinesisFirehoseClientBuilder.standard();

		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(properties.getAwsFirehoseAccessKey(),
				properties.getAwsFirehoseSecretKey());

		clientBuilder.setCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
		clientBuilder.setRegion(properties.getAwsFirehoseRegion());
		clientBuilder.setClientConfiguration(new ClientConfiguration());

		AmazonKinesisFirehose firehoseClient = clientBuilder.build();

		PutRecordRequest putRecordRequest = new PutRecordRequest();
		putRecordRequest.setDeliveryStreamName(properties.getAwsFirehoseStream());
		putRecordRequest.setRecord(new Record().withData(ByteBuffer.wrap(String.valueOf(payload).getBytes())));

		PutRecordResult putRecordResult = firehoseClient.putRecord(putRecordRequest);

		try {
			log.info("Put Result {}",
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(putRecordResult));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	}
}
