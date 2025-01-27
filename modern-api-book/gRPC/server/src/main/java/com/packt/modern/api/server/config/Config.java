package com.packt.modern.api.server.config;

import io.micrometer.core.instrument.binder.grpc.ObservationGrpcServerInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
  @Bean
  public ObservationGrpcServerInterceptor interceptor(ObservationRegistry registry) {
    return new ObservationGrpcServerInterceptor(registry);
  }
}
