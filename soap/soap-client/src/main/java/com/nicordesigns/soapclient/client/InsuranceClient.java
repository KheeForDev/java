package com.nicordesigns.soapclient.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.nicordesigns.spring_ws_insurance.ApplicantType;
import com.nicordesigns.spring_ws_insurance.InsuranceInfoType;
import com.nicordesigns.spring_ws_insurance.InsuranceRequest;
import com.nicordesigns.spring_ws_insurance.InsuranceResponse;
import com.nicordesigns.spring_ws_insurance.WidgetInfoType;

public class InsuranceClient extends WebServiceGatewaySupport {
	public InsuranceResponse getInsurance() {
		InsuranceRequest insuranceRequest = new InsuranceRequest();

		ApplicantType applicantType = new ApplicantType();
		applicantType.setSSN("12-34-5522");
		applicantType.setFirstName("Khee");
		applicantType.setMiddleName("Houy");
		applicantType.setLastName("Tan");

		insuranceRequest.setApplicant(applicantType);

		InsuranceInfoType insuranceInfoType = new InsuranceInfoType();
		insuranceInfoType.setProduct("Widget Protector");
		insuranceInfoType.setCoverageOption("Gold");
		insuranceInfoType.setConverageType("Full Life");

		insuranceRequest.setInsuranceInfo(insuranceInfoType);

		WidgetInfoType widgetInfoType = new WidgetInfoType();
		widgetInfoType.setWgtAmount("560.00");
		widgetInfoType.setWgtContractNumber("76.00");

		insuranceRequest.setWidgetInfo(widgetInfoType);

		System.out.println("Requesting insurance for: " + insuranceRequest);

		InsuranceResponse insuranceResponse = (InsuranceResponse) getWebServiceTemplate().marshalSendAndReceive(
				"http://localhost:8080/spring-ws-insurance", insuranceRequest,
				new SoapActionCallback("http://localhost:8080/spring-ws-insurance/insuranceService"));

		return insuranceResponse;
	}
}
