package com.employeemanagement.demo.advice;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller Advice Class
 * 
 * @author 2144388
 *
 */
@RestControllerAdvice
public class EmployeeControllerAdvice {

	/**
	 * Handles NoSuchElemenetException
	 * 
	 * @param ex
	 * @return ResponseEntity with message from exception
	 */
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles IllegalArgumentsException
	 * 
	 * @param ex
	 * @return ResponseEntity with message from exception
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	/**
	 * Handles AuthenticationException
	 * 
	 * @param ex
	 * @return ResponseEntity with message from exception
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles any other Exception
	 * 
	 * @param ex
	 * @return ResponseEntity with message from exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
