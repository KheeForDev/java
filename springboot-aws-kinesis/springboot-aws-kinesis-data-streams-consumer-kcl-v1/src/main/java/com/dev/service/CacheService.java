package com.dev.service;

import java.nio.charset.StandardCharsets;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.kinesis.model.Record;
import com.dev.model.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CacheService {
	private static final Logger log = LogManager.getLogger(CacheService.class);

	private HashMap<String, Record> cacheHashMap = new HashMap<>();

	public String process(List<Record> recordList) {
		String checkpointSequenceNumber = null;
		HttpStatus statusCode = null;
		ObjectMapper mapper = new ObjectMapper();
		List<String> orderedSequenceNumber = new ArrayList<>();
		List<RequestDto> requestDtoList = new ArrayList<>();
		List<RequestDto> consolidatedDataDto = new ArrayList<>();

		orderedSequenceNumber = mergeRecord(recordList);
		Collections.sort(orderedSequenceNumber);

		log.info("##### Sequence number ordering: {}", orderedSequenceNumber);
		checkpointSequenceNumber = orderedSequenceNumber.get(orderedSequenceNumber.size() - 1);
		log.info("##### Highest sequence number: {}", checkpointSequenceNumber);

		try {
			for (String sequenceNumber : orderedSequenceNumber) {
				if (cacheHashMap.containsKey(sequenceNumber)) {
					String data = new String(cacheHashMap.get(sequenceNumber).getData().array(),
							StandardCharsets.UTF_8);

					log.info("Sequence number: {} | Data: {}", sequenceNumber, data);

					RequestDto requestDto = new RequestDto();
					requestDto.setString(data);
					requestDtoList.add(requestDto);
				}
			}

			consolidateRecord(consolidatedDataDto, requestDtoList);

			log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(consolidatedDataDto));
		} catch (JsonProcessingException e) {
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

	private void consolidateRecord(List<RequestDto> consolidatedRequestDto, List<RequestDto> requestDtoList) {
		for (RequestDto record : requestDtoList) {
			consolidatedRequestDto.add(record);
		}
	}

	private HttpStatus callAPI(List<RequestDto> dataDto) {
		HttpStatus statusCode = null;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		String url = "http://localhost:7070/hit";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));

		HttpEntity<List<RequestDto>> entity = new HttpEntity<>(dataDto, headers);

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
