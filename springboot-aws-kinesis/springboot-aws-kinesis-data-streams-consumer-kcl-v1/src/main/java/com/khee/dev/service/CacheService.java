package com.khee.dev.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheService {
	private static final Logger log = LogManager.getLogger(CacheService.class);

	private HashMap<String, Record> cacheHashMap = new HashMap<>();

	public String process(List<Record> recordList) {
		log.info("##### {}", recordList.size());

		String checkpointSequenceNumber = null;
		HttpStatus statusCode = null;
		ObjectMapper mapper = new ObjectMapper();
		List<String> orderedSequenceNumber = new ArrayList<>();
		List<String> consolidatedDataDto = new ArrayList<>();

		orderedSequenceNumber = mergeRecord(recordList);
		Collections.sort(orderedSequenceNumber);

		log.info("##### seq number ordering {}", orderedSequenceNumber);
		checkpointSequenceNumber = orderedSequenceNumber.get(orderedSequenceNumber.size() - 1);
		log.info("##### latest seq number {}", checkpointSequenceNumber);

		try {
			for (String sequenceNumber : orderedSequenceNumber) {
				if (cacheHashMap.containsKey(sequenceNumber)) {

					String data = new String(cacheHashMap.get(sequenceNumber).getData().array(), "UTF-8");

					log.info("{} | {}", sequenceNumber, data);

					List<String> requestDtoList = mapper.readValue(data, new TypeReference<List<String>>() {
					});

					consolidateRecord(consolidatedDataDto, requestDtoList);

				}
			}

			log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(consolidatedDataDto));
		} catch (UnsupportedEncodingException | JsonProcessingException e) {
			log.error(e.getMessage());
		}

		statusCode = callAPI(consolidatedDataDto);

		if (statusCode.equals(HttpStatus.OK)) {
			cacheHashMap.clear();
		} else {
			checkpointSequenceNumber = null;
		}

		return checkpointSequenceNumber;
	}

	private List<String> mergeRecord(List<Record> recordList) {
		for (Record record : recordList) {
			cacheHashMap.put(record.getSequenceNumber(), record);
		}

		List<String> sequenceNumber = new ArrayList<>(cacheHashMap.keySet());

		return sequenceNumber;
	}

	private void consolidateRecord(List<String> consolidatedRequestDto, List<String> requestDtoList) {
		for (String record : requestDtoList) {
			consolidatedRequestDto.add(record);
		}
	}

	private HttpStatus callAPI(List<String> dataDto) {
		HttpStatus statusCode = null;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		String url = "http://localhost:7070/hit";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));

		HttpEntity<List<String>> entity = new HttpEntity<>(dataDto, headers);

		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (ResourceAccessException e) {
			log.error(e.getMessage());
		}

		if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
			statusCode = HttpStatus.OK;
		} else {
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return statusCode;
	}
}
