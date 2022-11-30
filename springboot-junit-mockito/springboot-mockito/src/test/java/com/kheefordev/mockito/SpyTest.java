package com.kheefordev.mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SpyTest {

	@Test
	public void test() {
		List<String> arrayListSpy = spy(ArrayList.class);
		
		assertEquals(0, arrayListSpy.size());
		arrayListSpy.add("Dummy");
		assertEquals(1, arrayListSpy.size());
		
		verify(arrayListSpy).add("Dummy");
		verify(arrayListSpy, never()).clear();
	}
}
