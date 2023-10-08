package com.employeemanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.demo.model.Privilege;

/**
 * Repository interface for Privilege entity
 * 
 * @author 2144388
 *
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

}
