package com.kheefordev.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

public class ListTest {

	@Test
	public void mockListSize() {
		List listMock = mock(List.class);
		when(listMock.size()).thenReturn(2);

		assertEquals(2, listMock.size());
		assertEquals(2, listMock.size());
		assertEquals(2, listMock.size());
	}

	@Test
	public void mockListSize_returnMultipleValues() {
		List listMock = mock(List.class);
		when(listMock.size()).thenReturn(2).thenReturn(3);

		assertEquals(2, listMock.size());
		assertEquals(3, listMock.size());
	}

	@Test
	public void mockListGet() {
		List listMock = mock(List.class);
		when(listMock.get(0)).thenReturn("Java Junit 4");

		assertEquals("Java Junit 4", listMock.get(0));
		assertEquals(null, listMock.get(1));
	}

	@Test
	public void mockListGet_anyInt() {
		List listMock = mock(List.class);
		// Argument Matcher
		// Will return value for any index in the List
		when(listMock.get(anyInt())).thenReturn("Java Junit 4");

		assertEquals("Java Junit 4", listMock.get(0));
		assertEquals("Java Junit 4", listMock.get(1));
	}

	@Test
	public void mockListGet_anyInt_usingBDD() {
		// Given
		List<String> listMock = mock(List.class);
		given(listMock.get(anyInt())).willReturn("Java Junit 4");

		// When
		String element = listMock.get(0);

		// Then
		assertThat(element, is("Java Junit 4"));
		assertThat(element, is("Java Junit 4"));
	}

	@Test(expected = RuntimeException.class)
	public void mockListGet_throwAnException() {
		List listMock = mock(List.class);
		// Argument Matcher
		// Will return value for any index in the List
		when(listMock.get(anyInt())).thenThrow(new RuntimeException("Encounter runtime exception"));

		listMock.get(0);
	}

	@Test(expected = RuntimeException.class)
	public void mockListGet_subList() {
		List listMock = mock(List.class);

		when(listMock.subList(anyInt(), 5)).thenThrow(new RuntimeException("Encounter runtime exception"));

		listMock.get(0);
	}
}
