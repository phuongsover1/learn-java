package com.packt.modern.api.config;

import io.micrometer.core.instrument.binder.grpc.ObservationGrpcClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
  @Bean
  public ObservationGrpcClientInterceptor interceptor(ObservationRegistry registry) {
    return new ObservationGrpcClientInterceptor(registry);
  }
}
