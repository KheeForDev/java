package com.nicordesigns.soapserver.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.nicordesigns.spring_ws_insurance.ApplicantType;
import com.nicordesigns.spring_ws_insurance.InsuranceInfoType;
import com.nicordesigns.spring_ws_insurance.InsuranceRequest;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;
import com.nicordesigns.spring_ws_insurance.WidgetInfoType;

@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Override
	public void writeInsuranceApplication(InsuranceRequest insuranceRequest) {
		System.out.println("Insurance Application: " + insuranceRequest);

		ApplicantType applicantType = insuranceRequest.getApplicant();
		System.out.println("Insurance Application: First Name " + applicantType.getFirstName());
		System.out.println("Insurance Application: Last Name " + applicantType.getLastName());
		System.out.println("Insurance Application: SSN " + applicantType.getSSN());

		WidgetInfoType widgetInfo = insuranceRequest.getWidgetInfo();
		System.out.println("Widget Information: Contract Number " + widgetInfo.getWgtContractNumber());
		System.out.println("Widget Information: Amount " + widgetInfo.getWgtAmount());

		InsuranceInfoType insuranceInfo = insuranceRequest.getInsuranceInfo();
		System.out.println("Insurance Information: Coverage Option " + insuranceInfo.getCoverageOption());
		System.out.println("Insurance Information: Coverage Type " + insuranceInfo.getConverageType());
		System.out.println("Insurance Information: Product " + insuranceInfo.getProduct());
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
