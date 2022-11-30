package com.kheefordev.junit.helper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class QuickBeforeAfterTest {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before Class");
	}

	@Before
	public void setup() {
		System.out.println("Before Test");
	}

	@Test
	public void test1() {
		System.out.println("test 1 executed");
	}

	@Test
	public void test2() {
		System.out.println("test 2 executed");
	}

	@After
	public void teardown() {
		System.out.println("After Test");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("After Class");
	}
}
