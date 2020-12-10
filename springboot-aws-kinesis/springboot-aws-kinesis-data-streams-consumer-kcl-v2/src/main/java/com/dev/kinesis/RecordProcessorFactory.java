package com.dev.kinesis;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

@Component
public class RecordProcessorFactory implements ShardRecordProcessorFactory {

	@Lookup
	public ShardRecordProcessor shardRecordProcessor() {
		return null;
	}
}
