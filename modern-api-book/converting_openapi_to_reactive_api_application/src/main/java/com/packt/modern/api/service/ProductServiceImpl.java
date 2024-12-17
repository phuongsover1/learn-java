package com.packt.modern.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.ProductEntity;
import com.packt.modern.api.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository pRepo;

  public ProductServiceImpl(ProductRepository pRepo) {
    this.pRepo = pRepo;
  }

  @Override
  public Flux<ProductEntity> getAllProducts() {
    return pRepo.findAll();
  }

  @Override
  public Mono<ProductEntity> getProduct(String id) {
    return pRepo.findById(UUID.fromString(id));
  }
}
