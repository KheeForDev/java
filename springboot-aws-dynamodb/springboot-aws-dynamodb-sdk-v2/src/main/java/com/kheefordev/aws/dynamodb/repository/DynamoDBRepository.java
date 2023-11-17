package com.kheefordev.aws.dynamodb.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kheefordev.aws.dynamodb.model.DynamoDBModel;
import com.kheefordev.config.Properties;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@Repository
public class DynamoDBRepository {
	private static final Logger LOG = LogManager.getLogger(DynamoDBRepository.class);

	@Autowired
	private Properties properties;

	@Autowired
	private DynamoDbClient dynamoDbClient;

	public void BatchWrite(List<DynamoDBModel> DynamoDBModels) {
		LOG.info("DynamoDB BatchWrite running");
		LOG.info("DynamoDB BatchWrite number of record(s): {}", DynamoDBModels.size());

		Map<String, List<WriteRequest>> requestItems = new HashMap<>();
		List<WriteRequest> writeRequests = new ArrayList<>();

		for (DynamoDBModel dynamoDBModel : DynamoDBModels) {
			UUID uuid = UUID.randomUUID();

			Map<String, AttributeValue> itemHm = new HashMap<>();
			itemHm.put("id", AttributeValue.builder().s(String.valueOf(uuid)).build());
			itemHm.put("data", AttributeValue.builder().s(dynamoDBModel.getData()).build());
			itemHm.put("timeToLive", AttributeValue.builder().n(String.valueOf(dynamoDBModel.getTimeToLive())).build());

			WriteRequest writeRequest = WriteRequest.builder().putRequest(PutRequest.builder().item(itemHm).build())
					.build();

			writeRequests.add(writeRequest);
		}

		// Add WriteRequests to requestItems
		requestItems.put(properties.getAwsDynamodbTable(), writeRequests);

		// Prepare the BatchWriteItemRequest.
		BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder().requestItems(requestItems)
				.build();

		try {
			// BatchWriteItem API limitations:
			// - Up to 25 put/delete operations
			// - Cannot exceed 1MB in HTTP payload
			BatchWriteItemResponse batchWriteItemResponse = dynamoDbClient.batchWriteItem(batchWriteItemRequest);
			LOG.info("DynamoDB BatchWriteItemResponse result: {}", batchWriteItemResponse.toString());
		} catch (DynamoDbException e) {
			LOG.error(e.awsErrorDetails().errorMessage());
		}

		LOG.info("DynamoDB BatchWrite completed");
	}
}
