package com.kheefordev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.kheefordev.config.Properties;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
@ComponentScan(basePackages = "com.kheefordev")
public class App {
	public static void main(String[] args) {
//		SpringApplication.run(App.class, args);

		ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
		ctx.close();
	}
}
