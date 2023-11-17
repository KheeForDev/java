package com.kheefordev.aws.dynamodb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamoDBModel {
	private String data;
	private long timeToLive;
}