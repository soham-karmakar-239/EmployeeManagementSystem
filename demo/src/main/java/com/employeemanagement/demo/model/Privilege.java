package com.employeemanagement.demo.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Privilege {
	
	@Id
	@Column(name = "privilege_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int privilegeId;
	
	@Column(name = "privilege_name")
	private int privilegeName;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;

}
