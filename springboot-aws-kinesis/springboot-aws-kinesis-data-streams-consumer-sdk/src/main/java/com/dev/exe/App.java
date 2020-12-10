package com.dev.exe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.khee.dev")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
