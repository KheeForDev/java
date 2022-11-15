package com.kheefordev.soapclientrt.controller;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kheefordev.soapclientrt.dto.ApplicantType;
import com.kheefordev.soapclientrt.dto.InsuranceRequest;
import com.kheefordev.soapclientrt.interceptor.RequestResponseLoggingInterceptor;

@RestController
@RequestMapping("/api")
public class InsuranceController {
	
	@GetMapping("/insurance")
	public ResponseEntity<String> insurance() throws JsonProcessingException {
//		String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spr=\"http://www.nicordesigns.com/spring-ws-insurance\"><soapenv:Header/><soapenv:Body><spr:InsuranceRequest><spr:Applicant><spr:fullname>123</spr:fullname></spr:Applicant></spr:InsuranceRequest></soapenv:Body></soapenv:Envelope>";
		
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		ApplicantType applicantType = new ApplicantType();
		applicantType.setFullname("kheefordev");
		
		insuranceRequest.setApplicant(applicantType);

	    RestTemplate restTemplate =  new RestTemplate();
	    restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
//	    //Create a list for the message converters
//	    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//	    //Add the String Message converter
//	    messageConverters.add(new StringHttpMessageConverter());
//	    //Add the message converters to the restTemplate
//	    restTemplate.setMessageConverters(messageConverters);


	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_XML);
	    HttpEntity<InsuranceRequest> request = new HttpEntity<InsuranceRequest>(insuranceRequest, headers);

	    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/spring-ws-insurance", request, String.class);
	    
	    System.out.println(response);
	    System.out.println(response.getBody());
		
		return ResponseEntity.status(HttpStatus.OK).body("insurance");
	}
}
