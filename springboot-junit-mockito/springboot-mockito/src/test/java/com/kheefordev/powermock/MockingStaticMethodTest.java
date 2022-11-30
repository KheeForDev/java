package com.kheefordev.powermock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UtilityClass.class)
public class MockingStaticMethodTest {
	// Specific Runner
	// Initialize UtilityClass.class

	@Mock
	private Dependency dependency;

	@InjectMocks
	private SystemUnderTest systemUnderTest;

	@Test
	public void testRetrieveTodosRelatedToSpring_usingPowerMockito() {
		List<Integer> stats = Arrays.asList(1, 2, 3);
		when(dependency.retrieveAllStats()).thenReturn(stats);

		PowerMockito.mockStatic(UtilityClass.class);
		when(UtilityClass.staticMethod(6)).thenReturn(150);

		int result = systemUnderTest.methodCallingAStaticMethod();
		assertEquals(150, result);

		// To verify if static method is called with the same argument
		PowerMockito.verifyStatic();
		UtilityClass.staticMethod(6);
	}
}
