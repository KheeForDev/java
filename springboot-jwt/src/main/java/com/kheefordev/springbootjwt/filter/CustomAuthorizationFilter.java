package com.kheefordev.springbootjwt.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kheefordev.springbootjwt.service.UserServiceImpl;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	private static final Logger log = LogManager.getLogger(CustomAuthorizationFilter.class);

	@Autowired
	private UserServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				log.info("token: {}", token);
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(token);
				String username = decodedJWT.getSubject();
				log.info("username: {}", username);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				for (GrantedAuthority auth : userDetails.getAuthorities()) {
					log.info(auth.getAuthority());
				}

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				log.error("error message: {}", e.getMessage());
				response.setHeader("error", e.getMessage());
				response.setStatus(403);

				Map<String, String> error = new HashMap<String, String>();
				error.put("errorMessage", e.getMessage());

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			log.info("authorization header is null or does not starts with Bearer");
			filterChain.doFilter(request, response);
		}
	}
}
