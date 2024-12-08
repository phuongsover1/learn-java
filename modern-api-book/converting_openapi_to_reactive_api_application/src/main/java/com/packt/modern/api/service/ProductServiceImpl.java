package com.packt.modern.api.service;

import com.packt.modern.api.entity.ProductEntity;
import com.packt.modern.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository pRepo;

  public ProductServiceImpl(ProductRepository pRepo) {
    this.pRepo = pRepo;
  }

  @Override
  public Iterable<ProductEntity> getAllProducts() {
    return pRepo.findAll();
  }

  @Override
  public Optional<ProductEntity> getProduct(String id) {
    return pRepo.findById(UUID.fromString(id));
  }
}
