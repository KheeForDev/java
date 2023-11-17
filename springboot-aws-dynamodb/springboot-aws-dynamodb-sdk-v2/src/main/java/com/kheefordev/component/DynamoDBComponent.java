package com.kheefordev.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kheefordev.aws.dynamodb.model.DynamoDBModel;
import com.kheefordev.aws.dynamodb.repository.DynamoDBRepository;
import com.kheefordev.config.Properties;
import com.kheefordev.util.DateUtil;

@Component
public class DynamoDBComponent implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(DynamoDBComponent.class);

	@Autowired
	private DynamoDBRepository dynamoDBRepository;

	@Autowired
	private Properties properties;

	@Autowired
	private DateUtil dateUtil;

	@Override
	public void run(String... args) throws Exception {
		LOG.info("DynamoDB SDK Version 2 running");

		List<DynamoDBModel> dynamoDBModels = new ArrayList<>();

		DynamoDBModel dynamoDBModel1 = new DynamoDBModel();
		dynamoDBModel1.setData("dynamoDBModel1 data");
		dynamoDBModel1.setTimeToLive(
				dateUtil.addCalendar(Calendar.DATE, properties.getAwsDynamodbTimeToLive()).getTime() / 1000);
		dynamoDBModels.add(dynamoDBModel1);

		DynamoDBModel dynamoDBModel2 = new DynamoDBModel();
		dynamoDBModel2.setData("dynamoDBModel2 data");
		dynamoDBModel2.setTimeToLive(
				dateUtil.addCalendar(Calendar.DATE, properties.getAwsDynamodbTimeToLive()).getTime() / 1000);
		dynamoDBModels.add(dynamoDBModel2);

		dynamoDBRepository.BatchWrite(dynamoDBModels);

		LOG.info("DynamoDB SDK Version 2 completed");
	}
}
