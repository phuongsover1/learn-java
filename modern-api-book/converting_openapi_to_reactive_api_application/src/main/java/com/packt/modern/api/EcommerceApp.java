package com.packt.modern.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EcommerceApp.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}

}
