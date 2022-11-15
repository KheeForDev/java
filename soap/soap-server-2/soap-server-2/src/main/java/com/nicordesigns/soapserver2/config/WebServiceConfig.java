package com.nicordesigns.soapserver2.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/spring-ws-insurance/*");
	}

	@Bean(name = "getInsurance")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema insuranceServiceSchema) {
		DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
		defaultWsdl11Definition.setPortTypeName("InsuranceServicePort");
		defaultWsdl11Definition.setLocationUri("/spring-ws-insurance");
		defaultWsdl11Definition.setTargetNamespace("http://www.nicordesigns.com/spring-ws-insurance");
		defaultWsdl11Definition.setSchema(insuranceServiceSchema);

		return defaultWsdl11Definition;
	}

	@Bean
	public XsdSchema insuranceServiceSchema() {
		return new SimpleXsdSchema(new ClassPathResource("InsuranceService.xsd"));
	}
}
