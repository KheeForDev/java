package com.sia.csl.service.oeotocmtdeliveryservice.aws.dynamodb.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.csl.service.oeotocmtdeliveryservice.aws.dynamodb.model.JourneyCampaign;
import com.sia.csl.service.oeotocmtdeliveryservice.config.Properties;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@Repository
public class JourneyCampaignRepository {
	private static final Logger LOG = LogManager.getLogger(JourneyCampaignRepository.class);

	@Autowired
	private Properties properties;

	@Autowired
	private DynamoDbClient dynamoDbClient;

	public void saveJourneyCampaigns(Map<String, JourneyCampaign> journeyCampaignHm) {
//		LOG.info("Number of record(s) inserting into DynamoDB: {}", journeyCampaignHm.size());
//
//		Map<String, List<WriteRequest>> requestItems = new HashMap<>();
//		List<WriteRequest> writeRequests = new ArrayList<>();
//
//		for (JourneyCampaign journeyCampaign : journeyCampaignHm.values()) {
//			StringBuilder sb = new StringBuilder();
//
//			sb.append(journeyCampaign.getIid());
//			sb.append("|");
//			sb.append(journeyCampaign.getCampaignName());
//
//			String primaryKey = sb.toString();
//
//			Map<String, AttributeValue> itemHm = new HashMap<>();
//			itemHm.put("id", AttributeValue.builder().s(primaryKey).build());
//			itemHm.put("campaignId", AttributeValue.builder().s(journeyCampaign.getCampaignId()).build());
//			itemHm.put("campaignName", AttributeValue.builder().s(journeyCampaign.getCampaignName()).build());
//			itemHm.put("iid", AttributeValue.builder().s(journeyCampaign.getIid()).build());
//			itemHm.put("journeyId", AttributeValue.builder().s(journeyCampaign.getJourneyId()).build());
//			itemHm.put("timeToLive",
//					AttributeValue.builder().n(String.valueOf(journeyCampaign.getTimeToLive())).build());
//
//			WriteRequest writeRequest = WriteRequest.builder().putRequest(PutRequest.builder().item(itemHm).build())
//					.build();
//
//			writeRequests.add(writeRequest);
//		}
//
//		// Add WriteRequests to requestItems
//		requestItems.put(properties.getAwsDynamodbTableName(), writeRequests);
//
//		// Prepare the BatchWriteItemRequest.
//		BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder().requestItems(requestItems)
//				.build();
//
//		// BatchWriteItem API limitations:
//		// - Up to 25 put/delete operations
//		// - Cannot exceed 1MB in HTTP payload
//		BatchWriteItemResponse batchWriteItemResponse = dynamoDbClient.batchWriteItem(batchWriteItemRequest);
//
//		try {
//			LOG.info(new ObjectMapper().writeValueAsString(batchWriteItemResponse));
//		} catch (JsonProcessingException e) {
//			LOG.error(e.getMessage());
//		}
	}
}
