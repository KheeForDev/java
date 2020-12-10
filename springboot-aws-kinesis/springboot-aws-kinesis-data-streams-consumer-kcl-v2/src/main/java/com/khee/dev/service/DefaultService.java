package com.khee.dev.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DefaultService {
	private static final Logger log = LogManager.getLogger(DefaultService.class);

	public void process(String data) {
		try {
			log.info("Perform business logic on: {} ",
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	}
}