package com.employeemanagement.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.employeemanagement.demo.service.EmployeeService;

/**
 * Configuration class for HttpSecurity
 * 
 * @author 2144388
 *
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurer {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private JwtAuthenticationEntryPoint authEntryPoint;

	/**
	 * Creates AuthenticationProvider bean
	 * 
	 * @return DaoAuthenticationProvider object
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(employeeService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

	/**
	 * Creates AuthenticationManager bean
	 * 
	 * @param http - HttpSecurity object
	 * @return AuthenticationManager object
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(authenticationProvider())
				.build();
	}

	/**
	 * Creates SecurityFilterChain bean
	 * 
	 * @param http - HttpSecurity
	 * @return SecurityFilterChain object
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.csrf().disable().authorizeHttpRequests((auth) -> auth.antMatchers(HttpMethod.DELETE, "/employee/*")
				.hasAuthority("DELETE_EMPLOYEE").antMatchers(HttpMethod.DELETE, "/role/*").hasAuthority("DELETE_ROLE")
				.antMatchers(HttpMethod.PUT, "/employee/*").hasAnyAuthority("EDIT_EMPLOYEE", "DELETE_EMPLOYEE")
				.antMatchers(HttpMethod.PUT, "/role/*").hasAnyAuthority("EDIT_ROLE", "DELETE_ROLE")
				.antMatchers(HttpMethod.POST, "/employee*")
				.hasAnyAuthority("WRITE_EMPLOYEE", "EDIT_EMPLOYEE", "DELETE_EMPLOYEE")
				.antMatchers(HttpMethod.POST, "/role*").hasAnyAuthority("WRITE_ROLE", "EDIT_ROLE", "DELETE_ROLE")
				.antMatchers(HttpMethod.GET, "/employee", "/employee/*", "/role*", "/privilege*")
				.hasAnyAuthority("READ", "WRITE_EMPLOYEE", "EDIT_EMPLOYEE", "DELETE_EMPLOYEE", "WRITE_ROLE",
						"EDIT_ROLE", "DELETE_ROLE")
				.antMatchers(HttpMethod.GET, "/user")
				.hasAnyAuthority("READ_SELF", "READ", "WRITE_EMPLOYEE", "EDIT_EMPLOYEE", "DELETE_EMPLOYEE",
						"WRITE_ROLE", "EDIT_ROLE", "DELETE_ROLE")
				.antMatchers("/login", "/docs", "/swagger-ui/**", "/v3/api-docs/**").permitAll().anyRequest()
				.authenticated());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(authEntryPoint).and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
