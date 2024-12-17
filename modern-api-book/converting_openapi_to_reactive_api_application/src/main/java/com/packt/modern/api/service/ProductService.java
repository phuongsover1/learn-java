package com.packt.modern.api.service;

import com.packt.modern.api.entity.ProductEntity;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.validation.annotation.Validated;

@Validated
public interface ProductService {
  @NotNull
  Flux<ProductEntity> getAllProducts();

  Mono<ProductEntity> getProduct(String id);
}
