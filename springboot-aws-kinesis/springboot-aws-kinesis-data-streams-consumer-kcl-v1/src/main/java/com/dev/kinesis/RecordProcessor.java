package com.dev.kinesis;

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
import com.dev.service.CacheService;

@Component
public class RecordProcessor implements IRecordProcessor {
	private static final Logger log = LogManager.getLogger(RecordProcessor.class);

	private String kinesisShardId;
	private String checkpointSequenceNumber = null;

	// Backoff and retry settings
	private static final long BACKOFF_TIME_IN_MILLIS = 3000L;
	private static final int NUM_RETRIES = 10;

	// Checkpoint about once a minute
	private static final long CHECKPOINT_INTERVAL_MILLIS = 60000L;
	private long nextCheckpointTimeInMillis;

	@Autowired
	private CacheService cacheService;

	@Override
	public void initialize(InitializationInput initializationInput) {
		log.info("Initializing record processor for shard: {}", initializationInput.getShardId());
		this.kinesisShardId = initializationInput.getShardId();
	}

	@Override
	public void processRecords(ProcessRecordsInput processRecordsInput) {
		log.info("Processing {} records from {}", processRecordsInput.getRecords().size(), kinesisShardId);

		checkpointSequenceNumber = cacheService.process(processRecordsInput.getRecords());

		// Checkpoint once every checkpoint interval.
		if (System.currentTimeMillis() > nextCheckpointTimeInMillis && checkpointSequenceNumber != null) {
			checkpoint(processRecordsInput.getCheckpointer());
			nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MILLIS;
		}
	}

	@Override
	public void shutdown(ShutdownInput shutdownInput) {
		log.info("Shutting down record processor for shard: {}", kinesisShardId);
		// Important to checkpoint after reaching end of shard, so we can start
		// processing data from child shards.
		if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE && checkpointSequenceNumber != null) {
			checkpoint(shutdownInput.getCheckpointer());
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
				checkpointer.checkpoint(checkpointSequenceNumber);
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

		checkpointSequenceNumber = null;
	}
}
