package com.kenda.webflux.restful;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application.properties"})
class SpringWebfluxRestfulApplicationTests {

	@Test
	void contextLoads() {
		String value = "SpringWebFlux";

		Assertions.assertEquals("SpringWebFlux", value);
	}

}
