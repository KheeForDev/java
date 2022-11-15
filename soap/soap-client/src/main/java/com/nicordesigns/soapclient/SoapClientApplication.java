package com.nicordesigns.soapclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nicordesigns.soapclient.client.InsuranceClient;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;

@SpringBootApplication
public class SoapClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapClientApplication.class, args);
	}

	@Bean
	CommandLineRunner lookup(InsuranceClient insuranceClient) {
		return args -> {
			InsuranceResponse insuranceResponse = insuranceClient.getInsurance();
			System.out.println(insuranceResponse.getConfirmationId());
			System.out.println(insuranceResponse.getAmount());
			System.out.println(insuranceResponse.getOrderDate());
			System.out.println(insuranceResponse.getValue());
		};
	}
}
