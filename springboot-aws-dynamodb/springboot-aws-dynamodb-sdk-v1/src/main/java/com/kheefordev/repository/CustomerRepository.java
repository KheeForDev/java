package com.kheefordev.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kheefordev.model.Customer;

@Repository
public class CustomerRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Customer saveCustomer(Customer customer) {
		dynamoDBMapper.save(customer);
		return customer;
	}

	public Customer getCustomerById(String customerId) {
		return dynamoDBMapper.load(Customer.class, customerId);
	}

	public String deleteCustomerById(String customerId) {
		dynamoDBMapper.delete(dynamoDBMapper.load(Customer.class, customerId));
		return "Customer Id : " + customerId + " Deleted!";
	}

	public String updateCustomer(String customerId, Customer customer) {
		dynamoDBMapper.save(customer, new DynamoDBSaveExpression().withExpectedEntry("customerId",
				new ExpectedAttributeValue(new AttributeValue().withS(customerId))));
		return customerId;
	}
}