package com.employeemanagement.demo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.employeemanagement.demo.logs.LoggerUtil;
import com.employeemanagement.demo.model.Employee;
import com.employeemanagement.demo.model.Privilege;
import com.employeemanagement.demo.model.Role;
import com.employeemanagement.demo.model.dto.RoleDto;
import com.employeemanagement.demo.repository.PrivilegeRepository;
import com.employeemanagement.demo.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private LoggerUtil loggerUtil;

	Logger logger = LoggerFactory.getLogger(RoleService.class);

	/**
	 * Gets list of requested roles
	 * 
	 * @param pageNo   - requested page number
	 * @param pageSize - number of records to be fetched per page
	 * @param sortBy   - field by which the response is sorted
	 * @param desc     - sorting order
	 * @return List of requested roles
	 */
	public List<Role> getAllRolesPaged(Integer pageNo, Integer pageSize, String sortBy, boolean desc) {

		final String primaryProcessName = "Get list of roles";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));

		Pageable pageable;
		Page<Role> pagedResult;
		try {
			if (desc == true)
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
			else
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		} catch (PropertyReferenceException ex) {
			pageable = PageRequest.of(pageNo, pageSize);
		}
		pagedResult = roleRepository.findAll(pageable);
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		if (pagedResult.hasContent())
			return pagedResult.getContent();
		return new ArrayList<Role>();
	}

	/**
	 * Gets details of role with particular role ID
	 * 
	 * @param id - role ID to be searched
	 * @return - role record with particular role ID
	 * @throws NoSuchElementException - if role is not found with requested role ID
	 */
	public Role getRoleById(int id) throws NoSuchElementException {
		final String primaryProcessName = "Get role by role ID";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		Role role = new Role();
		try {
			role = roleRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("No role found by ID : " + id);
			throw new NoSuchElementException("No role found with ID");
		} finally {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		}

		return role;
	}

	/**
	 * Creates new Role record
	 * 
	 * @param roleDto  - details of role to be created
	 * @param username - email ID of user who created the request
	 * @return - created role record
	 * @throws NoSuchElementException   - if privilege is not found with requested
	 *                                  privilege ID
	 * @throws IllegalArgumentException - if required field is missing from request,
	 *                                  or unique values are required
	 */
	public Role addNewRole(RoleDto roleDto, String username) throws NoSuchElementException, IllegalArgumentException {

		final String primaryProcessName = "Create Role";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));

		final String secondaryProcessName = "Validate Request";
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName));

		if (roleDto.getRoleName() == null || roleDto.getRoleName().isBlank()) {
			logger.error("Role name is empty");
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new IllegalArgumentException("Role name cannot be empty");
		}

		if (roleDto.getPrivilegeIds() == null || roleDto.getPrivilegeIds().isEmpty()) {
			logger.error("Privilege Ids is empty");
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new IllegalArgumentException("Privilege Ids cannot be empty");
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));

		Role role = new Role();
		role.setPrivileges(new HashSet<Privilege>());
		try {
			createRoleFromDto(role, roleDto);
			role = roleRepository.save(role);
			logger.info("Role " + role.getRoleName() + " created by " + username);
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (NoSuchElementException ex) {
			throw ex;
		} finally {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		}

		return role;
	}

	/**
	 * Updates role record with particular role ID
	 * 
	 * @param id       - role ID of record to be updated
	 * @param roleDto  - details to be updated
	 * @param username - email ID of user who created the request
	 * @return - role record of updated role
	 * @throws IllegalArgumentException - if unique values are required
	 * @throws NoSuchElementException   - if role is not found with requested role
	 *                                  ID ,or privilege is not found with requested
	 *                                  privilege ID
	 */
	public Role editRole(int id, RoleDto roleDto, String username)
			throws IllegalArgumentException, NoSuchElementException {

		final String primaryProcessName = "Update Role";

		final String secondaryProcessName = "Validate Role ID";

		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName));

		Role role;

		try {
			role = roleRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("No role found with ID : " + id);
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw ex;
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName));

		if (roleDto.getPrivilegeIds() != null && roleDto.getPrivilegeIds().size() > 0)
			role.setPrivileges(new HashSet<Privilege>());

		try {
			createRoleFromDto(role, roleDto);
			role = roleRepository.save(role);
			logger.info("Role with ID " + id + " updated by " + username);
		} catch (Exception ex) {
			throw ex;
		} finally {
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		}

		return role;
	}

	/**
	 * Utility function to create role record from role data transfer object
	 * 
	 * @param role    - role record
	 * @param roleDto - role data transfer object
	 */
	private void createRoleFromDto(Role role, RoleDto roleDto) {

		final String processName1 = "Validate unique role Name";

		logger.info(loggerUtil.buildBeginExecuteSecondary(processName1));
		Role roleNameCheck;
		if (roleDto.getRoleName() != null) {
			String roleName = roleDto.getRoleName();
			if (role.getRoleName() == null || !roleName.equals(role.getRoleName())) {
				roleNameCheck = roleRepository.findByRoleNameIgnoreCase(roleName);
				if (roleNameCheck != null) {
					logger.error("Role with same name already exists");
					logger.info(loggerUtil.buildEndExecuteSecondary(processName1));
					throw new IllegalArgumentException("Role with same name already exists");
				}
			}

		}
		role.setRoleName(roleDto.getRoleName());
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(processName1));

		final String processName2 = "Assign privileges to role";
		logger.info(loggerUtil.buildBeginExecuteSecondary(processName2));

		if (roleDto.getPrivilegeIds() != null && roleDto.getPrivilegeIds().size() > 0) {
			for (Integer id : roleDto.getPrivilegeIds()) {
				try {
					Privilege privilege = privilegeRepository.findById(id).get();
					role.getPrivileges().add(privilege);
					logger.info("Privilege with ID " + id + " assigned to role");
				} catch (NoSuchElementException ex) {
					logger.error("No privilege found with ID : " + id);
					logger.info(loggerUtil.buildEndExecuteSecondary(processName2));
					throw new NoSuchElementException("No privilege found with ID " + id);
				}
			}
		}
		logger.info(loggerUtil.buildEndExecuteSecondary(processName2));
	}

	/**
	 * Deletes role record with particular role ID
	 * 
	 * @param id       - role ID of role record to be deleted
	 * @param username - email ID of user who created the request
	 */
	public void deleteRole(int id, String username) {
		final String primaryProcessName = "Delete Role";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));

		final String secondaryProcessName1 = "Validate Role ID";
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName1));

		Role role;
		try {
			role = roleRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("No Role found with ID : " + id);
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("No role found with ID " + id);
		}
		logger.info(loggerUtil.getValidationSuccess());
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName1));

		final String secondaryProcessName2 = "Remove Role from Employee accounts";
		logger.info(loggerUtil.buildBeginExecuteSecondary(secondaryProcessName2));

		try {
			for (Employee employee : role.getEmployees()) {
				Set<Role> employeeRoles = employee.getRoles();
				employeeRoles.remove(role);
				if (CollectionUtils.isEmpty(employeeRoles)) {
					employeeRoles.add(roleRepository.findByRoleNameIgnoreCase("Basic"));
				}
				employee.setRoles(employeeRoles);
			}
		} catch (NoSuchElementException ex) {
			logger.error("Basic Role not found");
			logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));
			logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
			throw new NoSuchElementException("Basic Role not found");
		}
		logger.info(loggerUtil.buildEndExecuteSecondary(secondaryProcessName2));

		roleRepository.deleteById(id);
		logger.info("Role " + role.getRoleName() + " deleted by " + username);
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
	}

	/**
	 * Gets a list of all privileges
	 * 
	 * @return list of privileges
	 */
	public List<Privilege> getAllPrivileges() {
		final String primaryProcessName = "Get list of privileges";
		logger.info(loggerUtil.buildBeginExecutePrimary(primaryProcessName));
		logger.info(loggerUtil.buildEndExecutePrimary(primaryProcessName));
		return privilegeRepository.findAll();
	}

}
