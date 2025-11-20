package com.meryemefe.basic_spring_boot_test;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
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
}
