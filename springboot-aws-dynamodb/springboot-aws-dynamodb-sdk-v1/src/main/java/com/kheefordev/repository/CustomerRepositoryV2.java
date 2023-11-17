package com.kheefordev.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.kheefordev.model.CustomerV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CustomerRepositoryV2 {

	@Autowired
	private DynamoDB dynamoDB;

	public List<CustomerV2> getAllCustomers() {
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerV2> customerList = new ArrayList<>();
		Table table = dynamoDB.getTable("customer");

		ItemCollection<ScanOutcome> items = table.scan(null, "customerId, firstName, lastName, email", null, null);

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			CustomerV2 customer = new CustomerV2();
			try {
				customer = mapper.readValue(iterator.next().toJSON(), CustomerV2.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			customerList.add(customer);
		}

		return customerList;
	}

	public String saveCustomer(CustomerV2 customer) {
		Table table = dynamoDB.getTable("customer");

		Item item = new Item().withPrimaryKey("customerId", customer.getCustomerId())
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

	public void saveCustomers(List<CustomerV2> customerList) {
		System.out.println("Saving number of customer: " + customerList.size());

		List<Item> itemList = new ArrayList<>();

		for (CustomerV2 customer : customerList) {
			Item item = new Item().withPrimaryKey("customerId", customer.getCustomerId())
					.with("firstName", customer.getFirstName()).with("lastName", customer.getLastName())
					.with("email", customer.getEmail());
			itemList.add(item);
		}

		TableWriteItems tableWriteItems = new TableWriteItems("Customer");
		tableWriteItems.withItemsToPut(itemList);
		BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);

		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(outcome));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public CustomerV2 getCustomerById(String customerId) {
		ObjectMapper mapper = new ObjectMapper();
		CustomerV2 customer = null;

		Table table = dynamoDB.getTable("Customer");
		GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", customerId);
		Item item = table.getItem(spec);

		if (item != null) {
			String jsonItem = item.toJSON();

			try {
				customer = mapper.readValue(jsonItem, CustomerV2.class);
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
		}

		return customer;
	}

	public String deleteCustomerById(String customerId) {
		return null;
	}

	public String updateCustomer(String customerId, CustomerV2 customer) {
		return null;
	}
}