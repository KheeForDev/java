package com.kheefordev.data.api;

import java.util.List;

// Create TodoServiceStub
// Test TodoBusinessImpl using TodoServiceStub
public interface TodoService {
	public List<String> retrieveTodos(String user);
	public void deleteTodo(String todo);
}