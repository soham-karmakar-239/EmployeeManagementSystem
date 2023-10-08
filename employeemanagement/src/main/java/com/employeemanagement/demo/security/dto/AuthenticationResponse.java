package com.employeemanagement.demo.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer class for authentication token
 * 
 * @author 2144388
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication Response Object")
public class AuthenticationResponse {

	/**
	 * JWT generated against authentication
	 */
	@Schema(description = "Generated JWT against authentication request")
	private String accessToken;
}
