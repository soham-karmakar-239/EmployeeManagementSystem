package com.employeemanagement.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.demo.model.Privilege;
import com.employeemanagement.demo.model.Role;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

	List<Privilege> findAllByRole(Role role);
}
