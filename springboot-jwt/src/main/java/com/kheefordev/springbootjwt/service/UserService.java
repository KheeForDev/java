package com.kheefordev.springbootjwt.service;

import java.util.List;

import com.kheefordev.springbootjwt.model.Role;
import com.kheefordev.springbootjwt.model.User;

public interface UserService {
	public User saveUser(User user);

	public Role saveRole(Role role);

	public void addRoleToUser(String username, String roleName);

	public User getUser(String username);

	public List<User> getUsers();
}
