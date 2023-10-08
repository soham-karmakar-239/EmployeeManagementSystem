package com.employeemanagement.demo.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer class for authentication request
 * 
 * @author 2144388
 *
 */
@Data
@Schema(description = "Authentication Request Object")
public class AuthenticationRequest {

	/**
	 * Email ID of employee
	 */
	@Schema(description = "Email ID of employee")
	private String username;

	/**
	 * Password
	 */
	@Schema(description = "Password")
	private String password;

}
