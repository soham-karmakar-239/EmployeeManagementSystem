package com.employeemanagement.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.demo.model.Privilege;
import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.model.dto.RoleDto;
import com.employeemanagement.demo.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for roles management APIs
 * 
 * @author 2144388
 *
 */
@Tag(name = "Role", description = "Roles management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * Accepts request to get list of roles and their details
	 * 
	 * @param pageNo   - requested page number
	 * @param pageSize - number of records to be fetched per page
	 * @param sortBy   - field by which the response is sorted
	 * @param desc     - sorting order
	 * @return ResponseEntity with list of requested roles
	 */
	@Operation(summary = "Get list of all roles")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Fetched details of all roles", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class))) }),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@GetMapping(value = "/role")
	public ResponseEntity<List<Role>> viewRoles(
			@Parameter(required = false, description = "Page number") @RequestParam(defaultValue = "0") Integer pageNo,
			@Parameter(required = false, description = "Number of results per page") @RequestParam(defaultValue = "10") Integer pageSize,
			@Parameter(required = false, description = "Name of field by which to sort results") @RequestParam(defaultValue = "roleId") String sortBy,
			@RequestParam(required = false) boolean desc) {
		return new ResponseEntity<List<Role>>(roleService.getAllRolesPaged(pageNo, pageSize, sortBy, desc),
				HttpStatus.OK);
	}

	/**
	 * Accepts request to get role by role ID
	 * 
	 * @param id - role ID to be searched
	 * @return ResponseEntity with details of searched role
	 * @throws NoSuchElementException - If no role is found with requested role ID
	 */
	@Operation(summary = "Get details of role by role ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched details of role by role ID", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)) }),
			@ApiResponse(responseCode = "404", description = "Role not found by role ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@GetMapping(value = "/role/{id}")
	public ResponseEntity<Role> viewRole(@Parameter(description = "Role ID to be searched") @PathVariable int id)
			throws NoSuchElementException {
		return new ResponseEntity<Role>(roleService.getRoleById(id), HttpStatus.OK);
	}

	/**
	 * Accepts request to get list of all privileges
	 * 
	 * @return ResponseEntity with list of all privileges
	 */
	@Operation(summary = "Get list of all privileges")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched list of all privileges", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Privilege.class))) }),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@GetMapping(value = "/privilege")
	public ResponseEntity<List<Privilege>> viewPrivileges() {
		return new ResponseEntity<List<Privilege>>(roleService.getAllPrivileges(), HttpStatus.OK);
	}

	/**
	 * Accepts request to create new role
	 * 
	 * @param roleDto - details of new role to be created
	 * @return ResponseEntity with details of created role
	 * @throws NoSuchElementException   - if privilege is not found with requested
	 *                                  privilege ID
	 * @throws IllegalArgumentException - if request does not contain a required
	 *                                  field or unique values are required
	 */
	@Operation(summary = "Create new role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Role created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)) }),
			@ApiResponse(responseCode = "404", description = "Privilege not found by ID", content = @Content),
			@ApiResponse(responseCode = "409", description = "Role with same name already exists. Role Name must be unique.", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@PostMapping(value = "/role")
	public ResponseEntity<Role> addRole(@RequestBody RoleDto roleDto, Principal principal)
			throws NoSuchElementException, IllegalArgumentException {
		return new ResponseEntity<Role>(roleService.addNewRole(roleDto, principal.getName()), HttpStatus.CREATED);
	}

	/**
	 * Accepts request to update details of role with particular role ID
	 * 
	 * @param id      - role ID of role to be updated
	 * @param roleDto - details to be updated
	 * @return ResponseEntity with details of updated role
	 * @throws NoSuchElementException   - if privilege is not found with requested
	 *                                  privilege ID
	 * @throws IllegalArgumentException - if request does not contain a required
	 *                                  field or unique values are required
	 */
	@Operation(summary = "Update role with particular role ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Role updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)) }),
			@ApiResponse(responseCode = "404", description = "Role or privilege not found by ID", content = @Content),
			@ApiResponse(responseCode = "409", description = "Role with same name already exists. Role Name must be unique.", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@PutMapping(value = "/role/{id}")
	public ResponseEntity<Role> editRole(@Parameter(description = "Role ID to be updated") @PathVariable int id,
			@RequestBody RoleDto roleDto, Principal principal) throws NoSuchElementException, IllegalArgumentException {
		return new ResponseEntity<Role>(roleService.editRole(id, roleDto, principal.getName()), HttpStatus.OK);
	}

	/**
	 * Accepts request to delete role with particular role ID
	 * 
	 * @param id - role ID of role to be deleted
	 * @return ResponseEntity with no content
	 * @throws NoSuchElementException - if role is not found with requested role ID
	 */
	@Operation(summary = "Delete role with particular role ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Role deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Role not found by ID", content = @Content),
			@ApiResponse(responseCode = "403", description = "Not permitted to use this resource", content = @Content) })
	@DeleteMapping(value = "/role/{id}")
	public ResponseEntity<HttpStatus> deleteRole(@Parameter(description = "Role ID to be deleted") @PathVariable int id,
			Principal principal) throws NoSuchElementException {
		roleService.deleteRole(id, principal.getName());
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

}
