package com.pakt.modern.api.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class BarConfig {
  @Bean
  public BarBean barBean() {
    return new BarBean();
  }
}
