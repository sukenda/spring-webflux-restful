package com.kenda.webflux.restful;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringWebfluxRestfulApplicationTests {

	@Test
	void contextLoads() {
		String value = "SpringWebFlux";

		Assertions.assertEquals("SpringWebFlux", value);
	}

}
