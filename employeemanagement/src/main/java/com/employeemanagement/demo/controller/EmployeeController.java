package com.employeemanagement.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.model.dto.EmployeeDto;
import com.employeemanagement.demo.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for employee accounts management APIs
 * 
 * @author 2144388
 *
 */
@Tag(name = "Employee", description = "Employee accounts management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Accepts request to get account details of authenticated user
	 * 
	 * @param principal - UserDetails of authenticated user
	 * @return ResponseEntity with account details of authenticated employee
	 */
	@Operation(summary = "Get details of authenticated user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched details of authenticated user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
			@ApiResponse(responseCode = "404", description = "User details not found", content = @Content) })
	@GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> viewSelf(Principal principal) {
		return new ResponseEntity<Employee>(employeeService.getEmployeeByUsername(principal.getName()), HttpStatus.OK);
	}

	/**
	 * Accepts request to get list of all employee accounts and their details
	 * 
	 * @param pageNo
	 * @param pageSize - number of accounts to be fetched at a time
	 * @param sortBy   - field by which the response is sorted
	 * @param desc     - sorting order
	 * @return ResponseEntity with list of requested employees
	 */
	@Operation(summary = "Get list of all employee accounts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched details of all employees", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class))) }),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@GetMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Employee>> viewEmployees(
			@Parameter(required = false, description = "Page number") @RequestParam(defaultValue = "0") Integer pageNo,
			@Parameter(required = false, description = "Number of results per page") @RequestParam(defaultValue = "10") Integer pageSize,
			@Parameter(required = false, description = "Name of field by which to sort results") @RequestParam(defaultValue = "employeeId") String sortBy,
			@RequestParam(required = false) boolean desc) {
		return new ResponseEntity<List<Employee>>(employeeService.getAllEmployeesPaged(pageNo, pageSize, sortBy, desc),
				HttpStatus.OK);
	}

	/**
	 * Accepts request to get account details of employee with particular employee
	 * ID
	 * 
	 * @param id - employee ID to be searched
	 * @return ResponseEntity with account details of requested employee
	 * @throws NoSuchElementException - if no employee record is found with
	 *                                requested employee ID
	 */
	@Operation(summary = "Get details of employee by employee ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched details of employee by employee ID", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
			@ApiResponse(responseCode = "404", description = "Employee not found by employee ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content)})
	@GetMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ResponseEntity<Employee> viewEmployee(
			@Parameter(description = "Employee ID of the account") @PathVariable long id)
			throws NoSuchElementException {
		return new ResponseEntity<Employee>(employeeService.getEmployeeById(id), HttpStatus.OK);
	}

	/**
	 * Accepts request to add new employee account
	 * 
	 * @param employeeDto - details of new account
	 * @param principal   - authenticated user
	 * @return ResponseEntity with details of created employee account
	 * @throws NoSuchElementException   - if no role is found with requested role ID
	 * @throws IllegalArgumentException - if required fields are missing, or unique
	 *                                  values are required
	 */
	@Operation(summary = "Create new employee account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Employee account created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
			@ApiResponse(responseCode = "404", description = "Role not found by ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content)})
	@PostMapping(value = "/employee")
	public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDto employeeDto, Principal principal)
			throws NoSuchElementException, IllegalArgumentException {
		return new ResponseEntity<Employee>(employeeService.addEmployee(employeeDto, principal.getName()),
				HttpStatus.CREATED);
	}

	/**
	 * Accepts request to update an employee account details by employee ID
	 * 
	 * @param id          - employee ID
	 * @param employeeDto - new values for the account
	 * @param principal   - authenticated user
	 * @return ResponseEntity with account details after update
	 * @throws NoSuchElementException   - if no employee record is found with
	 *                                  requested employee ID
	 * @throws IllegalArgumentException - if unique values are required
	 */
	@Operation(summary = "Update employee account with particular employee ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Employee account updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
			@ApiResponse(responseCode = "404", description = "Employee or role not found by ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content)})
	@PutMapping(value = "/employee/{id}")
	public ResponseEntity<Employee> updateEmployee(
			@Parameter(description = "Employee ID of account to be updated") @PathVariable long id,
			@RequestBody EmployeeDto employeeDto, Principal principal)
			throws NoSuchElementException, IllegalArgumentException {
		return new ResponseEntity<Employee>(employeeService.updateEmployee(id, employeeDto, principal.getName()),
				HttpStatus.OK);
	}

	/**
	 * Accepts request to delete an employee account by employee ID
	 * 
	 * @param id        - employee ID of account to be deleted
	 * @param principal - authenticated user
	 * @return ResponseEntity with no content
	 * @throws NoSuchElementException - if no employee record is found with
	 *                                requested employee ID
	 */
	@Operation(summary = "Delete employee account with particular employee ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Employee account deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Employee not found by ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content)})
	@DeleteMapping(value = "/employee/{id}")
	public ResponseEntity<HttpStatus> deleteEmployee(
			@Parameter(description = "Employee ID of account to be deleted") @PathVariable long id, Principal principal)
			throws NoSuchElementException {
		employeeService.deleteEmployee(id, principal.getName());
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
}
