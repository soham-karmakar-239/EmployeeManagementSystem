package com.employeemanagement.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.employeemanagement.demo.model.Employee;

/**
 * Repository interface for Employee entity
 * 
 * @author 2144388
 *
 */
@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

	/**
	 * Gets list of requested employees
	 * 
	 * @param pageable - pagination information
	 * @return Page with list of requested employee records
	 */
	public Page<Employee> findAll(Pageable pageable);

	/**
	 * Gets employee with particular email ID
	 * 
	 * @param emailId - email ID to be searched
	 * @return employee record with particular email ID
	 */
	public Employee findByEmailIdIgnoreCase(String emailId);

	public Employee findByEmailId(String emailId);

}
