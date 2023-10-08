package com.employeemanagement.demo.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Custom AuthenticationEntryPoint class
 *  
 * @author 2144388
 *
 */
@Component
public class JwtAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	
	/**
	 * Responds to authentication exception
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(authException.getMessage());
	}
	
	/**
	 * Sets realmName
	 */
	@Override
	public void afterPropertiesSet() {
		setRealmName("JWT Authentication");
		super.afterPropertiesSet();
	}
	
	
}
