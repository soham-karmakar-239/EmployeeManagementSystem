package com.employeemanagement.demo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private long employeeId;
	
	@Column(name = "email_id", nullable = false)
	private String emailId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "phone_number")
	private long phoneNumber;
	
	@Column(name = "account_creation_date")
	private Date accountCreationDate;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="role_id", referencedColumnName="roleId")
	private Role role;

}
