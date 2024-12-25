package com.example.converting_oas_to_spring_code;

import com.packt.modern.api.EcommerceApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

class ConvertingOasToSpringCodeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		Mono<Object> empty1 = Mono.empty();
		Mono<Object> empty2 = Mono.empty();
		Mono.zip(empty1, empty2).subscribe(t -> {
			if (t.getT1() == null)
				Assertions.assertNull(t.getT1());
			if (t.getT2() == null)
				Assertions.assertNotNull(t.getT2());
		});


	}


}
