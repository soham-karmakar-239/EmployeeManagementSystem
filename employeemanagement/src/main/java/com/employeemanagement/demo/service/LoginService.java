package com.employeemanagement.demo.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.repository.EmployeeRepository;
import com.employeemanagement.demo.security.JwtUtility;
import com.employeemanagement.demo.security.dto.AuthenticationRequest;

@Service
public class LoginService {

	Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtility jwtUtility;

	/**
	 * Gets JWT against authentication request
	 * 
	 * @param authenticationRequest - authentication request
	 * @return - generated JWT
	 */
	public String authenticateUser(AuthenticationRequest authenticationRequest) {
		
		String jwt = new String();
		
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			jwt = jwtUtility.createToken(authentication);
			Employee employee = employeeRepository.findByEmailId(authenticationRequest.getUsername());
			employee.setLastLoginDate(new Date());
			employeeRepository.save(employee);
			logger.info("Authentication token generated for " + authenticationRequest.getUsername());
		} 
		catch (AuthenticationException ex) {
			logger.info("Failed login attempt with username " + authenticationRequest.getUsername());
			throw ex;
		}
		
		return jwt;
	}

}
