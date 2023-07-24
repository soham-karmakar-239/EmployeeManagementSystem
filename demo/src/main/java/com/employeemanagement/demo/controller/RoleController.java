package com.employeemanagement.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.service.RoleService;

@RestController(value = "/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@GetMapping(value = "/")
	public ResponseEntity<List<Role>> viewRoles(){
		return new ResponseEntity<List<Role>>(roleService.getAllRoles(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Role> viewRole(@PathVariable int id) throws NoSuchElementException {
		return new ResponseEntity<Role>(roleService.getRoleById(id), HttpStatus.OK);
	}
	
	@PostMapping(value = "/")
	public ResponseEntity<Role> addRole(@ModelAttribute Role role){
		return new ResponseEntity<Role>(roleService.addNewRole(role), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Role> editRole(@PathVariable int id, @ModelAttribute Role role) throws NoSuchElementException {
		return new ResponseEntity<Role>(roleService.editRole(id, role), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<HttpStatus> deleteRole(@PathVariable int id) throws NoSuchElementException {
		roleService.deleteRole(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	
}
