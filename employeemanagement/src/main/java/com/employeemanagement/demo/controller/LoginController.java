package com.employeemanagement.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.demo.security.dto.AuthenticationRequest;
import com.employeemanagement.demo.security.dto.AuthenticationResponse;
import com.employeemanagement.demo.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for authentication management APIs
 * 
 * @author 2144388
 *
 */
@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * Accepts authentication request with username and password
	 * 
	 * @param authenticationRequest
	 * @return ResponseEntity with JWT against authentication request
	 */
	@Operation(summary = "Authenticates username and password and provides JWT")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User authenticated", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Bad Credentials", content = @Content) })
	@PostMapping(value = "/login")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		String jwt = loginService.authenticateUser(authenticationRequest);
		if (jwt.isBlank()) {
			return new ResponseEntity<String>("Bad credentials", HttpStatus.UNAUTHORIZED);
		}
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

//	/**
//	 * Clears security context
//	 * 
//	 * @return ResponseEntity with HttpStatus 200
//	 */
//	@Operation(summary = "Clears Security Context")
//	@ApiResponse(responseCode = "200", description = "Security Context cleared.")
//	@GetMapping(value = "/logout")
//	public ResponseEntity<?> logout() {
//		request.removeAttribute("Authorization");
//		SecurityContextHolder.clearContext();
//		return ResponseEntity.ok("Logout Successful");
//	}

}
