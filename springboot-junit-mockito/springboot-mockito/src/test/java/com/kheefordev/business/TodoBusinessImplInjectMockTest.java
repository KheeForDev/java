package com.kheefordev.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import com.kheefordev.data.api.TodoService;

public class TodoBusinessImplInjectMockTest {
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private TodoService todoServiceMock;

	@InjectMocks
	private TodoBusinessImpl todoBusinessImpl;

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	@Test
	public void testRetrieveTodosRelatedToSpring_usingMock_assetEquals() {
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		when(todoServiceMock.retrieveTodos("Dummy")).thenReturn(todos);

		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertEquals(2, filteredTodos.size());
	}

	@Test
	public void testRetrieveTodosRelatedToSpring_usingMock_assertArrayEquals() {
		String[] expected = { "Learn Spring MVC", "Learn Spring" };

		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		when(todoServiceMock.retrieveTodos("Dummy")).thenReturn(todos);

		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		assertArrayEquals(expected, filteredTodos.toArray());
	}

	@Test
	public void testRetrieveTodosRelatedToSpring_usingBDD() {
		// Given
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);

		// When
		List<String> filteredTodos = todoBusinessImpl.retrieveTodosRelatedToSpring("Dummy");

		// Then
		assertThat(filteredTodos.size(), is(2));
	}

	@Test
	public void testDeleteTodosNotRelatedToSpring_usingBDD() {
		// Given
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);

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
		// Given
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);

		// When
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Dummy");

		// Then
		then(todoServiceMock).should().deleteTodo(stringArgumentCaptor.capture());
		assertThat(stringArgumentCaptor.getValue(), is("Learn to Dance"));
	}

	@Test
	public void testDeleteTodosNotRelatedToSpring_usingBDD_argumentCaptureMultiple() {
		// Given
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn to Rock and Roll", "Learn to Dance");
		given(todoServiceMock.retrieveTodos("Dummy")).willReturn(todos);

		// When
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Dummy");

		// Then
		then(todoServiceMock).should(times(2)).deleteTodo(stringArgumentCaptor.capture());
		assertThat(stringArgumentCaptor.getAllValues().size(), is(2));
	}
}
