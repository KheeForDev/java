package com.kheefordev.springbootjwt.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kheefordev.springbootjwt.model.User;
import com.kheefordev.springbootjwt.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.getUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
}
