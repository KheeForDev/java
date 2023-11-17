package com.kheefordev.aws.dynamodb;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfiguration {
//
//	@Autowired
//	private Properties properties;
//
//	@Bean
//	public DynamoDB dynamoDB() {
//		return new DynamoDB(buildAmazonDynamoDB());
//	}
//
//	private AmazonDynamoDB buildAmazonDynamoDB() {
//		return AmazonDynamoDBClientBuilder.standard()
//				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
//						properties.getAwsDynamodbEndpoint(), properties.getAwsRegion()))
//				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
//						properties.getAwsDynamodbAccessKey(), properties.getAwsDynamodbSecretKey())))
//				.build();
//	}
}