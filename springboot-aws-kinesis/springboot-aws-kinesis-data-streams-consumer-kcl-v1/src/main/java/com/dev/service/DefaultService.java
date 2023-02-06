package com.dev.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DefaultService {
	private static final Logger log = LogManager.getLogger(DefaultService.class);

	public String process(List<Record> recordList) {
		String checkpointSequenceNumber = null;
		ObjectMapper mapper = new ObjectMapper();

		for (Record record : recordList) {
			String data = new String(record.getData().array(), StandardCharsets.UTF_8);
			try {
				log.info("data: {}", mapper.writeValueAsString(data));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
			}

			checkpointSequenceNumber = record.getSequenceNumber();
		}

		return checkpointSequenceNumber;
	}
}
