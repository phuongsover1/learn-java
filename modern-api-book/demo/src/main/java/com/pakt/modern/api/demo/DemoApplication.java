package com.pakt.modern.api.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		// ApplicationContext appContext = new
		// AnnotationConfigApplicationContext(DemoApplication.class);
		// FooBean fooBean = appContext.getBean(FooBean.class);
		// BarBean barBean = appContext.getBean(BarBean.class);
	}

}
