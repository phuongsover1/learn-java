package com.packt.modern.api;

import com.packt.modern.api.server.GrpcServer;
import com.packt.modern.api.server.GrpcServerRunner;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerAppTests {

	@Autowired
	private ApplicationContext context;

	@Test
	@Order(1)
	void beanGrpcServerRunnerTest() {
		assertNotNull(context.getBean(GrpcServer.class));
		assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(GrpcServerRunner.class),
						"GrpcServerRunner should not be loaded during test");

	}


}
