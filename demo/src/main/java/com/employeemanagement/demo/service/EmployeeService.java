package com.employeemanagement.demo.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		Employee employee = employeeRepository.findByEmailId();
		if(employee == null) throw new UsernameNotFoundException("No account found with email id!");
//		if(!employee.isEnabled()) throw new DisabledException("User account is disabled.");
		return new EmployeeUserDetails(employee);
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	public Employee getEmployeeById(long id) throws NoSuchElementException {
		return employeeRepository.findById(id).get();
	}

	public Employee addEmployee(Employee employee) {
		employee.setAccountCreationDate(new Date());
		return employeeRepository.save(employee);
	}

	public Employee updateEmployee(long id, Employee newEmployee) throws NoSuchElementException {
		Employee existingEmployee = getEmployeeById(id);
		return employeeRepository.save(newEmployee);
	}
	
	public void deleteEmployee(long id) throws NoSuchElementException {
		Employee employee = getEmployeeById(id);
		employeeRepository.deleteById(id);
	}
		
}
