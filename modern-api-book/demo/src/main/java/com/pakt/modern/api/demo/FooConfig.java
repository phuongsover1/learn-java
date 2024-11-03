package com.pakt.modern.api.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class FooConfig {
  @Bean
  public FooBean fooBean() {
    return new FooBean();
  }
}
