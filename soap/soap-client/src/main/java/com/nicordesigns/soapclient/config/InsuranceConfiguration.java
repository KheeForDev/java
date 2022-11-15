package com.nicordesigns.soapclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.nicordesigns.soapclient.client.InsuranceClient;

@Configuration
public class InsuranceConfiguration {
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.nicordesigns.spring_ws_insurance");
		return marshaller;
	}

	@Bean
	public InsuranceClient insuranceClient(Jaxb2Marshaller marshaller) {
		InsuranceClient insuranceClient = new InsuranceClient();

		insuranceClient.setDefaultUri("http://localhost:8080/spring-ws-insurance/insuranceService");
		insuranceClient.setMarshaller(marshaller);
		insuranceClient.setUnmarshaller(marshaller);

		return insuranceClient;
	}
}
