package com.kheefordev.springbootjwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kheefordev.springbootjwt.model.Role;
import com.kheefordev.springbootjwt.model.RoleToUser;
import com.kheefordev.springbootjwt.model.User;
import com.kheefordev.springbootjwt.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.getUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@PostMapping("/user/save")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		User existUser = userService.getUser(user.getUsername());

		if (existUser != null)
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username taken");

		User newUser = userService.saveUser(user);

		if (newUser != null) {
			userService.addRoleToUser(newUser.getUsername(), "ROLE_USER");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create user");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("You have signed up successfully");
	}

	@PostMapping("/role/save")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveRole(role));
	}

	@PostMapping("/role/addtouser")
	public ResponseEntity<String> saveRole(@RequestBody RoleToUser roleToUser) {
		userService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
