package com.dev.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.dev.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CustomerRepository {

	@Autowired
	private DynamoDB dynamoDB;

	public List<Customer> getAllCustomers() {
		ObjectMapper mapper = new ObjectMapper();
		List<Customer> customerList = new ArrayList<>();
		Table table = dynamoDB.getTable("customer");

		ItemCollection<ScanOutcome> items = table.scan(null, "customerId, firstName, lastName, email", null, null);

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Customer customer = new Customer();
			try {
				customer = mapper.readValue(iterator.next().toJSON(), Customer.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			customerList.add(customer);
		}

		return customerList;
	}

	public String saveCustomer(Customer customer) {
		Table table = dynamoDB.getTable("customer");

		Item item = new Item().withPrimaryKey("customerId", String.valueOf(UUID.randomUUID()))
				.with("firstName", customer.getFirstName()).with("lastName", customer.getLastName())
				.with("email", customer.getEmail()).with("timeToLive", customer.getTimeToLive());

		PutItemOutcome putItemOutcome = table.putItem(item);

		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(putItemOutcome));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (!Objects.nonNull(putItemOutcome))
			return "Save failed";

		return "Save successful";
	}

	public void saveCustomers(List<Customer> customerList) {
		System.out.println("Saving number of customer: " + customerList.size());

		List<Item> itemList = new ArrayList<>();

		for (Customer customer : customerList) {
			Item item = new Item().withPrimaryKey("customerId", String.valueOf(UUID.randomUUID()))
					.with("firstName", customer.getFirstName()).with("lastName", customer.getLastName())
					.with("email", customer.getEmail());
			itemList.add(item);
		}

		TableWriteItems tableWriteItems = new TableWriteItems("customer");
		tableWriteItems.withItemsToPut(itemList);
		BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);

		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(outcome));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public Customer getCustomerById(String customerId) {
		return null;
	}

	public String deleteCustomerById(String customerId) {
		return null;
	}

	public String updateCustomer(String customerId, Customer customer) {
		return null;
	}
}