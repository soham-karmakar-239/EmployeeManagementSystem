package com.employeemanagement.demo.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Model class for Role entity
 * 
 * @author 2144388
 *
 */
@Data
@Entity
@Schema(description = "Role Model Information")
public class Role {

	/**
	 * Unique auto-generated role ID
	 */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Autogenerated Role Id")
	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;

	/**
	 * Unique Name of role
	 */
	@Schema(description = "Name of role")
	@Column(name = "role_name", nullable = false, unique = true)
	private String roleName;

	/**
	 * Employees with the role assigned
	 */
	@Schema(description = "Employees with the role assigned")
	@JsonIgnore
	@ManyToMany(mappedBy = "roles", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Collection<Employee> employees;

	/**
	 * Privileges associated with the role
	 */
	@Schema(description = "Privileges associated with the role")
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
	private Collection<Privilege> privileges;

}
