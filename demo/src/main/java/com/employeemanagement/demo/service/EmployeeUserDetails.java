package com.employeemanagement.demo.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.model.Privilege;
import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.repository.PrivilegeRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUserDetails implements UserDetails {
	
	private Employee employee;
	
	@Autowired
	private PrivilegeRepository privilegeRepository;
			
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getPrivileges(employee.getRole());
	}

	private Collection<? extends GrantedAuthority> getPrivileges(Role role) {
		List<Privilege> privileges = privilegeRepository.findAllByRole(role);
		
		return null;
	}

	@Override
	public String getPassword() {
		return employee.getPassword();
	}

	@Override
	public String getUsername() {
		return employee.getEmailId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return employee.isEnabled();
	}
	
	public EmployeeUserDetails(Employee employee){
		this.employee=employee;
	}

}
