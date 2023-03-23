package com.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.ComponentScan;

@EnableSqs
@SpringBootApplication
@ComponentScan(basePackages = "com.dev")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
