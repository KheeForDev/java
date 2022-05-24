package com.kheefordev.springbootjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kheefordev.springbootjwt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
