package com.khee.dev.kinesis;

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
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.khee.dev.model.Properties;

@Component
public class Consumer implements CommandLineRunner {
	private static final Logger log = LogManager.getLogger(Consumer.class);

	@Autowired
	private Properties properties;

	@Override
	public void run(String... args) throws Exception {
		BasicAWSCredentials credentials = new BasicAWSCredentials(properties.getAwsKinesisAccessKey(),
				properties.getAwsKinesisSecretKey());

		AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

		String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
		KinesisClientLibConfiguration kinesisClientLibConfiguration = new KinesisClientLibConfiguration(
				properties.getApplicationName(), properties.getAwsKinesisStream(), credentialsProvider, workerId);
		kinesisClientLibConfiguration.withRegionName(properties.getAwsKinesisRegion());
		kinesisClientLibConfiguration.withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);

		IRecordProcessorFactory recordProcessorFactory = new RecordProcessorFactory();
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
