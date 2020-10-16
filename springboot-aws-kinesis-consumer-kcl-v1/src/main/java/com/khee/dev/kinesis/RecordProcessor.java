package com.khee.dev.kinesis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khee.dev.service.DefaultService;

@Component
public class RecordProcessor implements IRecordProcessor {
	private static final Logger log = LogManager.getLogger(RecordProcessor.class);

	private String kinesisShardId;

	// Backoff and retry settings
	private static final long BACKOFF_TIME_IN_MILLIS = 3000L;
	private static final int NUM_RETRIES = 10;

	// Checkpoint about once a minute
	private static final long CHECKPOINT_INTERVAL_MILLIS = 60000L;
	private long nextCheckpointTimeInMillis;

	@Autowired
	private DefaultService defaultService;

	@Override
	public void initialize(InitializationInput initializationInput) {
		log.info("Initializing record processor for shard: {}", initializationInput.getShardId());
		this.kinesisShardId = initializationInput.getShardId();
	}

	@Override
	public void processRecords(ProcessRecordsInput processRecordsInput) {
		log.info("Processing {} records from {}", processRecordsInput.getRecords().size(), kinesisShardId);

		// Process records and perform all exception handling.
		processRecordsWithRetries(processRecordsInput.getRecords());

		// Checkpoint once every checkpoint interval.
		if (System.currentTimeMillis() > nextCheckpointTimeInMillis) {
			checkpoint(processRecordsInput.getCheckpointer());
			nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MILLIS;
		}
	}

	@Override
	public void shutdown(ShutdownInput shutdownInput) {
		log.info("Shutting down record processor for shard: {}", kinesisShardId);
		// Important to checkpoint after reaching end of shard, so we can start
		// processing data from child shards.
		if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
			checkpoint(shutdownInput.getCheckpointer());
		}
	}

	private void processRecordsWithRetries(List<Record> records) {
		for (Record record : records) {
			boolean processedSuccessfully = false;
			for (int i = 0; i < NUM_RETRIES; i++) {
				try {
					//
					// logic to process record goes here.
					//
					processSingleRecord(record);

					processedSuccessfully = true;
					break;
				} catch (Throwable t) {
					log.warn("Caught throwable while processing record {}", record, t);
				}

				// backoff if we encounter an exception.
				try {
					Thread.sleep(BACKOFF_TIME_IN_MILLIS);
				} catch (InterruptedException e) {
					log.debug("Interrupted sleep", e);
				}
			}

			if (!processedSuccessfully) {
				log.error("Couldn't process record {}. Skipping the record.", record);
			}
		}
	}

	/**
	 * Process a single record.
	 * 
	 * @param record The record to be processed.
	 */
	private void processSingleRecord(Record record) {
		// TODO Add your own record processing logic here
		try {
			log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(record));
			log.info(new String(record.getData().array(), "UTF-8"));

			defaultService.process(new String(record.getData().array(), "UTF-8"));
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			log.info(e.getMessage());
		}
	}

	/**
	 * Checkpoint with retries.
	 * 
	 * @param checkpointer
	 */
	private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
		log.info("Checkpointing shard {}", kinesisShardId);
		for (int i = 0; i < NUM_RETRIES; i++) {
			try {
				checkpointer.checkpoint();
				break;
			} catch (ShutdownException se) {
				// Ignore checkpoint if the processor instance has been shutdown (fail over).
				log.info("Caught shutdown exception, skipping checkpoint.", se);
				break;
			} catch (ThrottlingException e) {
				// Backoff and re-attempt checkpoint upon transient failures
				if (i >= (NUM_RETRIES - 1)) {
					log.error("Checkpoint failed after " + (i + 1) + "attempts.", e);
					break;
				} else {
					log.info("Transient issue when checkpointing - attempt " + (i + 1) + " of " + NUM_RETRIES, e);
				}
			} catch (InvalidStateException e) {
				// This indicates an issue with the DynamoDB table (check for table, provisioned
				// IOPS).
				log.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e);
				break;
			}
			try {
				Thread.sleep(BACKOFF_TIME_IN_MILLIS);
			} catch (InterruptedException e) {
				log.debug("Interrupted sleep", e);
			}
		}
	}
}
