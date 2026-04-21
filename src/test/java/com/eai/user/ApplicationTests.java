package com.eai.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Tag("unit")
class ApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true, "Application can't start");
	}

}
