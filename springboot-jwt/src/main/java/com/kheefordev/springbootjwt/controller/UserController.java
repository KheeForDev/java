package com.kheefordev.springbootjwt.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@PostMapping("/refreshtoken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(token);
				String username = decodedJWT.getSubject();

				User user = userService.getUser(username);

//				Token with 10 minutes validity
				String refreshToken = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURI())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);

				Map<String, Object> tokens = new HashMap<String, Object>();
				tokens.put("refreshToken", refreshToken);

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
				response.setHeader("error", e.getMessage());
				response.setStatus(403);

				Map<String, String> error = new HashMap<String, String>();
				error.put("errorMessage", e.getMessage());

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}
}
