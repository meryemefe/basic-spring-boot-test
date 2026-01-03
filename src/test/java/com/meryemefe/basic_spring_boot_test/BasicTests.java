package com.meryemefe.basic_spring_boot_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Basic Tests")
@DisplayNameGeneration(DisplayNameGenerator.class)
public class BasicTests {

	@Test
	public void testAddition() {
		int sum = 2 + 2;
		assert sum == 4 : "2 + 2 should equal 4";
	}

	@Test
	public void testWrongAddition() {
		int sum = 2 + 2;
		assert sum == 5 : "2 + 2 should equal 5";
	}

	// Give a "Test > Dummy Test" name to this test
	@Test
	@DisplayName("Test > Dummy")
	public void testDummy() {
		boolean val = false;
		assert val : "Dummy val should be true";
	}
}
