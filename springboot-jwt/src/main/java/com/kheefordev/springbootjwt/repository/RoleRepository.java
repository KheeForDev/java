package com.kheefordev.springbootjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kheefordev.springbootjwt.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
