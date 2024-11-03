package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import java.time.Duration;

@SpringBootApplication
public class CacheResponseApplication {
  public static void main(String[] args) {
    SpringApplication.run(CacheResponseApplication.class, args);
  }

  // @Bean
  public RouteLocator basicRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("use_cache", r -> r
            .header("demo", "cache-response")
            .filters(f -> f.localResponseCache(Duration.ofSeconds(5), DataSize.ofMegabytes(100)))
            .uri("http://httpbin.org:80/get"))
        .build();
  }
}
