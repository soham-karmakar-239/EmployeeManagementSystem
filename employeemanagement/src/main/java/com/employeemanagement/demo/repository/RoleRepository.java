package com.employeemanagement.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.employeemanagement.demo.model.Role;

/**
 * Repository interface for Role entity
 * 
 * @author 2144388
 *
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

	/**
	 * Gets a list of requested roles
	 * 
	 * @param pageable - pagination information
	 * @return Page containing list of requested roles
	 */
	public Page<Role> findAll(Pageable pageable);

	/**
	 * Gets role with particular role name
	 * 
	 * @param roleName - role name to be searched
	 * @return role with particular role name
	 */
	public Role findByRoleNameIgnoreCase(String roleName);

}
