package com.pakt.modern.api.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
public class AppConfig {
  private int count = 0;

  @TimeMonitor
  private synchronized void increaseCount() {
    count++;
    System.out.println("Count = " + count);
  }

  @TimeMonitor
  @Bean
  public FooBean fooBean() {
    System.out.println("In FooBean");
    increaseCount();
    return new FooBean();
  }

  @TimeMonitor
  @Bean
  @DependsOn({ "fooBean", "barBean" })
  public BazBean bazBean() {
    System.out.println("In BazBean");
    increaseCount();
    return new BazBean();
  }

  @TimeMonitor
  @Bean
  public BarBean barBean() {
    System.out.println("In BarBean");
    increaseCount();
    return new BarBean();
  }

}
