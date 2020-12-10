package com.dev.kinesis;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;

@Component
public class RecordProcessorFactory implements IRecordProcessorFactory {

	@Lookup
	public IRecordProcessor createProcessor() {
		return null;
	}
}
