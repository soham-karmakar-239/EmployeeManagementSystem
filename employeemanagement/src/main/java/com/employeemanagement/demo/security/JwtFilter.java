package com.employeemanagement.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.employeemanagement.demo.service.EmployeeService;

/**
 * Custom Filter class for JWT authentication
 * 
 * @author 2144388
 *
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtility jwtUtil;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Validates JWT received with request and sets authentication information in
	 * SecurityContextHolder
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = jwtUtil.resolveToken(request);

		if (token != null && jwtUtil.validateToken(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails userDetails = employeeService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);

	}

}
