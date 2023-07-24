package com.employeemanagement.demo.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Role {
	
	@Id
	@Column(name = "role_id")
	private int roleId;
	
	@Column(name = "role_name")
	private String roleName;
	
	@JsonIgnore
	@OneToMany(mappedBy="role", cascade=CascadeType.ALL)
	private Collection<Employee> employees;
	
	@ManyToMany
    @JoinTable(
        name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"), 
        inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "privilegeId"))
	private Collection<Privilege> privileges;

}
