package com.dev.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.model.Customer;
import com.dev.model.Properties;
import com.dev.repository.CustomerRepository;
import com.dev.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CustomerController {
	@Autowired
	private Properties properties;

	@Autowired
	private DateUtil dateUtil;
	
	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/")
	public void getAllCustomers() {
		List<Customer> customerList = customerRepository.getAllCustomers();

		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customerList));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/add/customer")
	public String saveCustomer(@RequestBody Customer customer) {		
		customer.setTimeToLive(dateUtil.addCalendar(dateUtil.getCurrentCalendar(), Calendar.DATE,
				properties.getAwsDynamodbTimeToLive()).getTime() / 1000);
		
		return customerRepository.saveCustomer(customer);
	}

	@PostMapping("/add/customers")
	public String saveCustomer(@RequestBody List<Customer> customerList) {
		List<Customer> saveList = new ArrayList<Customer>();
		
		for (Customer customer : customerList) {
			saveList.add(customer);
			
			if (saveList.size() == 25) {
				customerRepository.saveCustomers(saveList);
				saveList.clear();
			}
		}
		
		// Save remaining
		if (saveList.size() > 0)
			customerRepository.saveCustomers(saveList);
		
		return "Save successful";
	}

//	@GetMapping("/get/customer/{id}")
//	public Customer getCustomerById(@PathVariable("id") String customerId) {
//		return customerRepository.getCustomerById(customerId);
//	}
//
//	@DeleteMapping("/delete/customer/{id}")
//	public String deleteCustomerById(@PathVariable("id") String customerId) {
//		return customerRepository.deleteCustomerById(customerId);
//	}
//
//	@PutMapping("/update/customer/{id}")
//	public String updateCustomer(@PathVariable("id") String customerId, @RequestBody Customer customer) {
//		return customerRepository.updateCustomer(customerId, customer);
//	}
}
