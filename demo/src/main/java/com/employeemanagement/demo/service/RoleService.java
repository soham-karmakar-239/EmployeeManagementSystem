package com.employeemanagement.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public Role getRoleById(int id) throws NoSuchElementException{
		return roleRepository.findById(id).get();
	}

	public Role addNewRole(Role role) {
		return roleRepository.save(role);
	}

	public Role editRole(int id, Role newRole) throws NoSuchElementException {
		Role existingRole = getRoleById(id);
		return roleRepository.save(newRole);
	}

	public void deleteRole(int id) throws NoSuchElementException {
		Role existingRole = getRoleById(id);
		roleRepository.deleteById(id);
	}
	

}
