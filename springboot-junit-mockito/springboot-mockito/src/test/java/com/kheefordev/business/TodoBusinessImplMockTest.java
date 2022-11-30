package com.kheefordev.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.kheefordev.data.api.TodoService;

public class TodoBusinessImplMockTest {
	// What is mocking?
	// mocking is creating objects that simulate the behavior of real objects.
	// Unlike stubs, mocks can be dynamically created from code - at runtime.
	// Mocks offer more functionality than stubbing.
	// You can verify method calls and a lot of other things.

	@Test
	public void testRetrieveTodosRelatedToSpring_usingMock_assetEquals() {
		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		when(todoServiceMock.retrieveTodos("Dummy")).thenReturn(todos);

		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertEquals(2, filteredTodos.size());
	}

	@Test
	public void testRetrieveTodosRelatedToSpring_usingMock_assertArrayEquals() {
		String[] expected = { "Learn Spring MVC", "Learn Spring" };

		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		when(todoServiceMock.retrieveTodos("Dummy")).thenReturn(todos);

		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertArrayEquals(expected, filteredTodos.toArray());
	}

	@Test
	public void testRetrieveTodosRelatedToSpring_usingBDD() {
		// Given
		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);

		// When
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		// Then
		assertThat(filteredTodos.size(), is(2));
	}

	@Test
	public void testDeleteTodosNotRelatedToSpring_usingBDD() {
		// Given
		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);

		// When
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Dummy");

		// Then
		verify(todoServiceMock).deleteTodo("Learn to Dance");

		// Then - times(n) to verify that a method is run n times
		verify(todoServiceMock, times(1)).deleteTodo("Learn to Dance");

		// Then - atLeast(n) to verify that a method is run >= n times
		verify(todoServiceMock, atLeast(1)).deleteTodo("Learn to Dance");

		// Then - never() to verify that a method is not called
		verify(todoServiceMock, never()).deleteTodo("Learn Spring MVC");
		verify(todoServiceMock, never()).deleteTodo("Learn Spring");

		// Then - Alternative
		then(todoServiceMock).should().deleteTodo("Learn to Dance");
		then(todoServiceMock).should(never()).deleteTodo("Learn Spring MVC");
		then(todoServiceMock).should(never()).deleteTodo("Learn Spring");
	}

	@Test
	public void testDeleteTodosNotRelatedToSpring_usingBDD_argumentCapture() {
		// Declare Argument Captor
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

		// Given
		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);

		// When
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Dummy");

		// Then
		then(todoServiceMock).should().deleteTodo(stringArgumentCaptor.capture());
		assertThat(stringArgumentCaptor.getValue(), is("Learn to Dance"));
	}

	@Test
	public void testDeleteTodosNotRelatedToSpring_usingBDD_argumentCaptureMultiple() {
		// Declare Argument Captor
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

		// Given
		TodoService todoServiceMock = mock(TodoService.class);
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn to Rock and Roll", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);
		TodoBusinessImpl todoBusinessImpl = new TodoBusinessImpl(todoServiceMock);

		// When
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Dummy");

		// Then
		then(todoServiceMock).should(times(2)).deleteTodo(stringArgumentCaptor.capture());
		assertThat(stringArgumentCaptor.getAllValues().size(), is(2));
	}
}
