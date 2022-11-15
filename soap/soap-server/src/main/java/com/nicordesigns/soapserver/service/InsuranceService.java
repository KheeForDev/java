package com.nicordesigns.soapserver.service;

import org.springframework.stereotype.Service;

import com.nicordesigns.spring_ws_insurance.InsuranceRequest;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;

@Service
public interface InsuranceService {
	public void writeInsuranceApplication(InsuranceRequest insuranceRequest);
	public InsuranceResponse processInsuranceApplication(InsuranceRequest insuranceRequest);
}
