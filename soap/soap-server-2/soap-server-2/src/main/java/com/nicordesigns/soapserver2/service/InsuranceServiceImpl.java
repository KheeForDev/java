package com.nicordesigns.soapserver2.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.nicordesigns.spring_ws_insurance.ApplicantType;
import com.nicordesigns.spring_ws_insurance.InsuranceRequest;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;

@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Override
	public void writeInsuranceApplication(InsuranceRequest insuranceRequest) {
		System.out.println("Insurance Application: " + insuranceRequest);

		ApplicantType applicantType = insuranceRequest.getApplicant();
		System.out.println("Insurance Application: Full Name " + applicantType.getFullname());
	}

	@Override
	public InsuranceResponse processInsuranceApplication(InsuranceRequest insuranceRequest) {
		InsuranceResponse insuranceResponse = new InsuranceResponse();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(calendar.getTime());
		System.out.println(formattedDate);

		insuranceResponse.setConfirmationId("1");
		insuranceResponse.setAmount("500.00");
		insuranceResponse.setOrderDate(formattedDate);
		insuranceResponse.setValue("1700.00");

		return insuranceResponse;
	}
}
