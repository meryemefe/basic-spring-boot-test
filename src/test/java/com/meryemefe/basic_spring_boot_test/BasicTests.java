package com.meryemefe.basic_spring_boot_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"logging.level.root=INFO",
		"logging.level.com.meryemefe.basic_spring_boot_test=TRACE"
})
@Slf4j
public class BasicTests {

	@Test
	public void testAddition() {
		log.trace("Test logs for TRACE level");
		log.debug("Test logs for DEBUG level");
		log.info("Test logs for INFO level");
		log.warn("Test logs for WARN level");
		log.error("Test logs for ERROR level");
		int sum = 2 + 2;
		assert sum == 4 : "2 + 2 should equal 4";
	}

	@Test
	public void testWrongAddition() {
		log.trace("Test logs for TRACE level");
		log.debug("Test logs for DEBUG level");
		log.info("Test logs for INFO level");
		log.warn("Test logs for WARN level");
		log.error("Test logs for ERROR level");
		int sum = 2 + 2;
		assert sum == 5 : "2 + 2 should equal 5";
	}
}
