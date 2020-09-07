package com.khee.dev.kinesis;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.amazon.kinesis.exceptions.InvalidStateException;
import software.amazon.kinesis.exceptions.ShutdownException;
import software.amazon.kinesis.lifecycle.events.InitializationInput;
import software.amazon.kinesis.lifecycle.events.LeaseLostInput;
import software.amazon.kinesis.lifecycle.events.ProcessRecordsInput;
import software.amazon.kinesis.lifecycle.events.ShardEndedInput;
import software.amazon.kinesis.lifecycle.events.ShutdownRequestedInput;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.retrieval.KinesisClientRecord;

public class RecordProcessor implements ShardRecordProcessor {
	private static final Logger log = LogManager.getLogger(RecordProcessor.class);

	private static final String SHARD_KEY = "ShardId";

	private HashMap<String, String> shardMap;
	private String shardId;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecordProcessor() {
		this.shardMap = new HashMap();
	}

	@Override
	public void initialize(InitializationInput initializationInput) {
		shardId = initializationInput.shardId();
		shardMap.put(SHARD_KEY, shardId);
		try {
			log.info("Initializing record processor for shard: {} ({} Byte)", initializationInput.shardId(),
					initializationInput.shardId().getBytes());
		} finally {
			shardMap.remove(SHARD_KEY);
		}
	}

	@Override
	public void processRecords(ProcessRecordsInput processRecordsInput) {
		shardMap.put(SHARD_KEY, shardId);
		try {
			log.info("Thread Name:" + Thread.currentThread().getName() + " Processing {} record(s)",
					processRecordsInput.records().size());

			for (KinesisClientRecord record : processRecordsInput.records()) {
				CharBuffer charBuffer = StandardCharsets.US_ASCII.decode(record.data());
				String text = charBuffer.toString();

				log.info("Processing record pk: {} -- Seq: {} -- Data: {}", record.partitionKey(),
						record.sequenceNumber(), text);
			}
		} catch (Throwable t) {
			log.error("Caught throwable while processing records.  Aborting");
			Runtime.getRuntime().halt(1);
		} finally {
			shardMap.remove(SHARD_KEY);
		}

		log.info("Processing {} records from {}", processRecordsInput.records().size(), shardId);

		shardMap.remove(SHARD_KEY);
	}

	@Override
	public void leaseLost(LeaseLostInput leaseLostInput) {
		log.error("leaseLostInput ", leaseLostInput.toString());
	}

	@Override
	public void shardEnded(ShardEndedInput shardEndedInput) {
		shardMap.put(SHARD_KEY, shardId);
		try {
			log.info("Reached shard end checkpointing.");
			shardEndedInput.checkpointer().checkpoint();
		} catch (ShutdownException | InvalidStateException e) {
			log.error("Exception while checkpointing at shard end. Giving up", e.getMessage());
		} finally {
			shardMap.remove(SHARD_KEY);
		}

	}

	@Override
	public void shutdownRequested(ShutdownRequestedInput shutdownRequestedInput) {
		shardMap.put(SHARD_KEY, shardId);
		try {
			log.info("Scheduler is shutting down, checkpointing.");
			shutdownRequestedInput.checkpointer().checkpoint();
		} catch (ShutdownException | InvalidStateException e) {
			log.error("Exception while checkpointing at requested shutdown. Giving up", e.getMessage());
		} finally {
			shardMap.remove(SHARD_KEY);
		}
	}
}
