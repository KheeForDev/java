package com.kheefordev.springbootjwt;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kheefordev.springbootjwt.model.Role;
import com.kheefordev.springbootjwt.model.User;
import com.kheefordev.springbootjwt.service.UserService;

@SpringBootApplication
public class SpringbootJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(null, "John", "john", "1234", new ArrayList<Role>()));
			userService.saveUser(new User(null, "Mary", "mary", "1234", new ArrayList<Role>()));
			userService.saveUser(new User(null, "Jane", "jane", "1234", new ArrayList<Role>()));
			userService.saveUser(new User(null, "Kate", "kate", "1234", new ArrayList<Role>()));

			userService.addRoleToUser("john", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("john", "ROLE_ADMIN");
			userService.addRoleToUser("john", "ROLE_MANAGER");
			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("mary", "ROLE_ADMIN");
			userService.addRoleToUser("mary", "ROLE_MANAGER");
			userService.addRoleToUser("mary", "ROLE_USER");
			userService.addRoleToUser("Jane", "ROLE_MANAGER");
			userService.addRoleToUser("Jane", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("Jane", "ROLE_USER");
			userService.addRoleToUser("kate", "ROLE_USER");
		};
	}
}
