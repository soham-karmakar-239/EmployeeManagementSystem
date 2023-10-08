package com.employeemanagement.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.model.Privilege;
import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.repository.PrivilegeRepository;

import lombok.Data;

/**
 * Custom UserDetails implementation class
 * 
 * @author 2144388
 *
 */
@Data
public class EmployeeUserDetails implements UserDetails {

	private Employee employee;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	/**
	 * Gets authorities of user from associated employee record
	 * 
	 * @return Collection of SimpleGrantedAuthorities
	 *
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getPrivileges(employee.getRoles());
	}

	/**
	 * Gets authorities from list of roles of authenticated user
	 * 
	 * @param roles - list of roles of user
	 * @return Collection of SimpleGrantedAuthorities
	 */
	private Collection<? extends GrantedAuthority> getPrivileges(Collection<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Set<String> privileges = new HashSet<>();
		for (Role role : roles) {
			for (Privilege privilege : role.getPrivileges()) {
				privileges.add(privilege.getPrivilegeName());
			}
		}
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
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

	/**
	 * Gets account status of authenticated user
	 *
	 */
	@Override
	public boolean isEnabled() {
		return employee.getEnabled();
	}

	/**
	 * Parameterized constructor to created UserDetails from employee record
	 * 
	 * @param employee - employee record
	 */
	public EmployeeUserDetails(Employee employee) {
		this.employee = employee;
	}

}
