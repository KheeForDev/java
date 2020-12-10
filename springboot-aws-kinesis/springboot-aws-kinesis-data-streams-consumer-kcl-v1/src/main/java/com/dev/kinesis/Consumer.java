package com.dev.kinesis;

import java.net.InetAddress;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.dev.model.Properties;

@Component
public class Consumer implements CommandLineRunner {
	private static final Logger log = LogManager.getLogger(Consumer.class);

	@Autowired
	private Properties properties;

	@Autowired
	private RecordProcessorFactory recordProcessorFactory;

	@Override
	public void run(String... args) throws Exception {
		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());

		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);

		String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
		KinesisClientLibConfiguration kinesisClientLibConfiguration = new KinesisClientLibConfiguration(
				properties.getApplicationName(), properties.getAwsKinesisStream(), awsCredentialsProvider, workerId);
		kinesisClientLibConfiguration.withRegionName(properties.getAwsRegion());

		// Maximum number of records to fetch in a Kinesis getRecords() call
		// Minimum value of 1
		// Maximum value of 10000
		// Default value is 10000 without defining
		kinesisClientLibConfiguration.withMaxRecords(10);
		kinesisClientLibConfiguration.withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);

		Worker worker = new Worker.Builder().recordProcessorFactory(recordProcessorFactory)
				.config(kinesisClientLibConfiguration).build();

		log.info("Running {} to process stream {} as worker {}...", properties.getApplicationName(),
				properties.getAwsKinesisStream(), workerId);

		try {
			worker.run();
		} catch (Throwable t) {
			log.error("Caught throwable while processing data.");
			log.error(t.getMessage());
		}
	}
}
