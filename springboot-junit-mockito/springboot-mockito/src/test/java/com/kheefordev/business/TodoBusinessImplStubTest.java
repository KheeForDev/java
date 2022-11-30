package com.kheefordev.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.kheefordev.data.api.TodoService;
import com.kheefordev.data.api.TodoServiceStub;

public class TodoBusinessImplStubTest {

	@Test
	public void testRetrieveTodosRelatedToSpring_usingStub_assetEquals() {
		TodoService todoServiceStub = new TodoServiceStub();
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceStub);
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertEquals(2, filteredTodos.size());
	}

	@Test
	public void testRetrieveTodosRelatedToSpring_usingStub_assertArrayEquals() {
		String[] expected = { "Learn Spring MVC", "Learn Spring" };

		TodoService todoServiceStub = new TodoServiceStub();
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceStub);
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertArrayEquals(expected, filteredTodos.toArray());
	}
}
