package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringGatewayApplication.class, args);
  }

  @Bean
  public RouteLocator basicRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(p -> p
            .path("/get")
            .filters(f -> f.addResponseHeader("Hello", "World"))
            .uri("http://httpbin.org:80")
        )
        .build();
  }

}
