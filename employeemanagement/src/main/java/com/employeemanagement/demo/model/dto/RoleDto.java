package com.employeemanagement.demo.model.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer class for Role entity
 * 
 * @author 2144388
 *
 */
@Data
@Schema(description = "Role Data Transfer Object")
public class RoleDto {

	/**
	 * Name of role
	 */
	@Schema(description = "Name of role")
	private String roleName;

	/**
	 * Privilege ID of privileges assigned to the role
	 */
	@ArraySchema(schema = @Schema(description = "Privilege ID of privileges assigned to the role"))
	private List<Integer> privilegeIds;

}
