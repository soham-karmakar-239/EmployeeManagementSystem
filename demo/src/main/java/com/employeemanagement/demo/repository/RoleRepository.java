package com.employeemanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
