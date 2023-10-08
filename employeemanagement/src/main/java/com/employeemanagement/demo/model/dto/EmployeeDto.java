package com.employeemanagement.demo.model.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer class for Employee entity
 * 
 * @author 2144388
 *
 */
@Data
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDto {

	/**
	 * Email ID of employee
	 */
	@Schema(description = "Email ID of employee")
	private String emailId;

	/**
	 * First name of employee
	 */
	@Schema(description = "First name of employee")
	private String firstName;

	/**
	 * Last name of employee
	 */
	@Schema(description = "Last name of employee")
	private String lastName;

	/**
	 * Password
	 */
	@Schema(description = "Password")
	private String password;

	/**
	 * Phone number of employee
	 */
	@Schema(description = "Phone number of employee")
	private Long phoneNumber;

	/**
	 * State of the account
	 */
	@Schema(description = "State of the account")
	private Boolean enabled;

	/**
	 * Role ID of the roles for the account
	 */
	@ArraySchema(schema = @Schema(description = "Role ID of the roles for the account"))
	private List<Integer> roleIds;

	/**
	 * Comments/description on the account
	 */
	@Schema(description = "Comments/description on the account")
	private String description;

}
