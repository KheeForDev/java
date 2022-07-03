package com.kheefordev.springbootjwt.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger log = LogManager.getLogger(CustomAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username = "";
		String password = "";

		// for request using application/json
		try {
			Map<?, ?> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
			username = (String) requestMap.get("username");
			password = (String) requestMap.get("password");
		} catch (IOException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}

		// for request using application/x-www-form-urlencoded
		// username = request.getParameter("username");
		// password = request.getParameter("password");

		log.info("username: {}", username);
		log.info("password: {}", password);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password, new ArrayList<SimpleGrantedAuthority>());

		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURI())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURI()).sign(algorithm);

//		response.setHeader("accessToken", accessToken);
//		response.setHeader("refreshToken", refreshToken);

		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
			roles.add(grantedAuthority.getAuthority());
		}
		
		Map<String, Object> tokens = new HashMap<String, Object>();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		tokens.put("roles", roles);

		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
}
