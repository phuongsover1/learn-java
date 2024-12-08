package com.packt.modern.api.service;

import com.packt.modern.api.entity.ProductEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface ProductService {
  @NotNull
  Iterable<ProductEntity> getAllProducts();

  Optional<ProductEntity> getProduct(String id);
}
