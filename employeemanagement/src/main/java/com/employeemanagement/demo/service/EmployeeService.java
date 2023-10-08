package com.employeemanagement.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.employeemanagement.demo.logs.LoggerUtil;
import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.model.dto.EmployeeDto;
import com.employeemanagement.demo.repository.EmployeeRepository;
import com.employeemanagement.demo.repository.RoleRepository;

/**
 * Service class for management of Employee entities
 * 
 * @author 2144388
 *
 */
@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private LoggerUtil loggerUtil;

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	/**
	 * Creates password encoder bean
	 * 
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Gets UserDetails for user authentication
	 * 
	 * @param username - email ID of user trying to authenticate
	 * @return EmployeeUserDetails of associated employee record
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {
		Employee employee = employeeRepository.findByEmailId(username);
		if (employee == null)
			throw new UsernameNotFoundException("No account found with email id!");
		if (!employee.getEnabled())
			throw new DisabledException("User account is disabled.");
		return new EmployeeUserDetails(employee);
	}

	/**
	 * Gets requested list of employee records
	 * 
	 * @param pageNo
	 * @param pageSize - number of employee records to be fetched
	 * @param sortBy   - field by which to sort result
	 * @param desc     - sorting order
	 * @return List of employee records
	 */
	public List<Employee> getAllEmployeesPaged(Integer pageNo, Integer pageSize, String sortBy, boolean desc) {

		final String primaryProcessName = "Get list of employee accounts";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));

		Pageable pageable;
		Page<Employee> pagedResult;
		try {
			if (desc == true)
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
			else
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		} catch (PropertyReferenceException ex) {
			pageable = PageRequest.of(pageNo, pageSize);
		}
		pagedResult = employeeRepository.findAll(pageable);
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		}
		return new ArrayList<Employee>();
	}

	/**
	 * Gets employee record with particular employee ID
	 * 
	 * @param id - Employee ID to be searched
	 * @return Employee record with particular ID
	 * @throws NoSuchElementException - if no employee record is found with
	 *                                requested employee ID
	 */
	public Employee getEmployeeById(long id) throws NoSuchElementException {

		final String primaryProcessName = "Get employee account by employee ID";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));

		Employee employee = new Employee();
		try {
			employee = employeeRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("No employee account found by ID : " + id);
			throw new NoSuchElementException("No employee found with ID");
		} finally {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		}
		return employee;
	}

	/**
	 * Creates new employee record
	 * 
	 * @param employeeDto       - details of new employee record
	 * @param createdByUsername - email ID of user who created the request
	 * @return - employee record of created employee account
	 */
	public Employee addEmployee(EmployeeDto employeeDto, String createdByUsername) {

		final String primaryProcessName = "Create Employee Account";

		final String secondaryProcessName1 = "Validate request";

		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName1));

		if (employeeDto.getEmailId() == null || employeeDto.getPassword() == null) {
			logger.error("Required fields missing from request");
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new IllegalArgumentException("Required fields missing");
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));

		final String secondaryProcessName2 = "Validate unique emailID";
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName2));
		Employee employeeCheckEmail = employeeRepository.findByEmailIdIgnoreCase(employeeDto.getEmailId());
		if (employeeCheckEmail != null) {
			logger.error("Account with same email ID already exists : " + employeeDto.getEmailId());
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new IllegalArgumentException("Account exists with same Email ID.");
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));

		Employee employee = new Employee();
		try {
			createEmployeeFromDto(employee, employeeDto);
		} catch (NoSuchElementException ex) {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("Role not found");
		}
		employee.setAccountCreatedBY(createdByUsername);
		employee.setAccountCreationDate(new Date());
		employee = employeeRepository.save(employee);
		logger.info(
				"Employee account with employee ID " + employee.getEmployeeId() + " created by " + createdByUsername);
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		return employee;
	}

	/**
	 * Updates existing employee record
	 * 
	 * @param id          - employee ID of account to be updated
	 * @param employeeDto - details to be updated
	 * @param username    - email ID of user who created the request
	 * @return employee record of updated employee account
	 */
	public Employee updateEmployee(long id, EmployeeDto employeeDto, String username) {

		final String primaryProcessName = "Update Employee Account";

		final String secondaryProcessName1 = "Validate request";

		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName1));

		Employee employee;
		try {
			employee = employeeRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("Employee not found by ID " + id);
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("Employee not found by ID " + id);
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));

		final String secondaryProcessName2 = "Validate unique emailID";
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName2));

		if (employeeDto.getEmailId() != null && !employeeDto.getEmailId().equals(employee.getEmailId())) {
			Employee employeeCheckEmail = employeeRepository.findByEmailIdIgnoreCase(employeeDto.getEmailId());
			if (employeeCheckEmail != null) {
				logger.error("Account with same email ID already exists : " + employeeDto.getEmailId());
				logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));
				logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
				throw new IllegalArgumentException("Account exists with same Email ID.");
			}
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));

		try {
			createEmployeeFromDto(employee, employeeDto);
		} catch (NoSuchElementException ex) {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("Role not found");
		}
		employee.setAccountLastModifiedBy(username);
		employee.setAccountLastModifiedDate(new Date());
		employee = employeeRepository.save(employee);
		logger.info("Employee account with ID " + id + " modified by " + username);
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		return employee;
	}

	/**
	 * Utility method to create employee record from employee data transfer object
	 * 
	 * @param employee    - employee record
	 * @param employeeDto - employee data transfer object
	 * @throws NoSuchElementException - if role is not found with requested role ID
	 */
	private void createEmployeeFromDto(Employee employee, EmployeeDto employeeDto) throws NoSuchElementException {

		if (employeeDto.getEmailId() != null)
			employee.setEmailId(employeeDto.getEmailId());
		if (employeeDto.getFirstName() != null)
			employee.setFirstName(employeeDto.getFirstName());
		if (employeeDto.getLastName() != null)
			employee.setLastName(employeeDto.getLastName());
		if (employeeDto.getPassword() != null)
			employee.setPassword(passwordEncoder().encode(employeeDto.getPassword()));
		if (employeeDto.getDescription() != null)
			employee.setDescription(employeeDto.getDescription());
		if (employeeDto.getPhoneNumber() != null)
			employee.setPhoneNumber(employeeDto.getPhoneNumber());
		if (employeeDto.getEnabled() != null)
			employee.setEnabled(employeeDto.getEnabled());
		else if (employeeDto.getEnabled() == null && employee.getEnabled() == null)
			employee.setEnabled(true);

		final String processName = "Assign Roles to Employee Account";

		logger.info(loggerUtil.buildBeginExecuteSecondary(processName));

		List<Integer> dtoRoleIds = employeeDto.getRoleIds();
		if ((dtoRoleIds == null || dtoRoleIds.isEmpty()) && (employee.getRoles() == null)) {
			try {
				Role basicRole = roleRepository.findByRoleNameIgnoreCase("Basic");
				employee.setRoles(Collections.singleton(basicRole));
				logger.info("Basic role assigned to employee account");
			} catch (NoSuchElementException ex) {
				logger.error("Basic role not found");
				logger.info(loggerUtil.buildEndExecuteSecondary(processName));
				throw new NoSuchElementException("Basic role not found");
			}

		} else if (dtoRoleIds != null) {
			try {
				Set<Role> roles = new HashSet<>();
				for (Integer id : dtoRoleIds) {
					Role role = roleRepository.findById(id).get();
					roles.add(role);
					logger.info(role.getRoleName() + " role assigned to employee account");
				}
				employee.setRoles(roles);
			} catch (NoSuchElementException ex) {
				logger.error("Role not found by role ID");
				logger.info(loggerUtil.buildEndExecuteSecondary(processName));
				throw new NoSuchElementException("Role not found by ID");
			}
		}
		logger.info(loggerUtil.buildEndExecuteSecondary(processName));
	}

	/**
	 * Deletes employee record with particular employee ID
	 * 
	 * @param id       - Employee ID of account to be deleted
	 * @param username - email ID of user who created the request
	 */
	public void deleteEmployee(long id, String username) {

		final String primaryProcessName = "Delete Employee Account";

		final String secondaryProcessName = "Validate request";

		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName));
		try {
			Employee employee = employeeRepository.findById(id).get();
			logger.info(loggerUtil.getValidationSuccess());
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));
			employeeRepository.deleteById(id);
			logger.info("Employee account with employee ID " + id + " deleted by " + username);
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		} catch (NoSuchElementException ex) {
			logger.error("Employee account not found with ID " + id);
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("Employee not found by id " + id);
		}
	}

	/**
	 * Gets an employee record by particular email ID
	 * 
	 * @param username - email ID of account to be searched
	 * @return employee with requested email ID
	 */
	public Employee getEmployeeByUsername(String username) {
		try {
			return employeeRepository.findByEmailId(username);
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("Account not found by email id.");
		}

	}

}
