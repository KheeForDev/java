package com.kheefordev.springbootjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kheefordev.springbootjwt.filter.CustomAuthenticationFilter;
import com.kheefordev.springbootjwt.filter.CustomAuthorizationFilter;
import com.kheefordev.springbootjwt.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserServiceImpl userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomAuthorizationFilter customAuthorizationFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// override default login path in UsernamePasswordAuthenticationFilter
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
				authenticationManagerBean());
		customAuthenticationFilter.setFilterProcessesUrl("/api/login");
		http.addFilter(customAuthenticationFilter);

		// .cors()
		// tells spring security to enable cors
		// server is ready to whitelist/filter the origins.		
		http.csrf().disable().cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/login").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("ROLE_SUPER_ADMIN",
				"ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save").permitAll();
		
		// httpBasic()
		// tells spring security that clients are going to authenticate the requests through basic authentication method
		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}