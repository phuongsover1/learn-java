package com.packt.modern.api.controllers;

import com.packt.modern.api.ProductApi;
import com.packt.modern.api.hateoas.ProductRepresentationAssembler;
import com.packt.modern.api.model.Product;
import com.packt.modern.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ProductController implements ProductApi {
  private final ProductService pService;
  private final ProductRepresentationAssembler assembler;

  public ProductController(ProductService pService, ProductRepresentationAssembler assembler) {
    this.pService = pService;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<Product> getProduct(String id) {
    return pService.getProduct(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<List<Product>> queryProducts(String tag, String name, Integer page, Integer size) {
    return ok(assembler.toModelList(pService.getAllProducts()));
  }
}
