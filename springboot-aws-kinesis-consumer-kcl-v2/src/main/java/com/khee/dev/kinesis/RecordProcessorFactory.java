package com.khee.dev.kinesis;

import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

public class RecordProcessorFactory implements ShardRecordProcessorFactory {

	@Override
	public ShardRecordProcessor shardRecordProcessor() {
		return new RecordProcessor();
	}
}
