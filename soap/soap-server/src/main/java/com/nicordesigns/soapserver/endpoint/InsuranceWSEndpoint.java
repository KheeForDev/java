package com.nicordesigns.soapserver.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.nicordesigns.soapserver.service.InsuranceService;
import com.nicordesigns.spring_ws_insurance.InsuranceRequest;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;

@Endpoint
public class InsuranceWSEndpoint {
	private static final String NAMESPACE_URI = "http://www.nicordesigns.com/spring-ws-insurance";
	
	@Autowired
	private InsuranceService insuranceService;
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="InsuranceRequest")
	@ResponsePayload
	public InsuranceResponse InsuranceApplication(@RequestPayload InsuranceRequest insuranceRequest) {
		if (insuranceRequest != null) {
			insuranceService.writeInsuranceApplication(insuranceRequest);
		}
		
		return insuranceService.processInsuranceApplication(insuranceRequest);
	}
}
