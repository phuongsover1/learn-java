package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.packt.modern.api.ProductApi;
import com.packt.modern.api.hateoas.ProductRepresentationModelAssembler;
import com.packt.modern.api.model.Product;
import com.packt.modern.api.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController implements ProductApi {
  private final ProductService pService;
  private final ProductRepresentationModelAssembler assembler;

  public ProductController(ProductService pService, ProductRepresentationModelAssembler assembler) {
    this.pService = pService;
    this.assembler = assembler;
  }

    @Override
  public Mono<ResponseEntity<Product>> getProduct(String id, ServerWebExchange exchange) {
    return pService.getProduct(id).map(p -> assembler.entityToModel(p, exchange))
        .map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<Product>>> queryProducts(@Valid String tag, @Valid String name,
      @Valid Integer page, @Valid Integer size, ServerWebExchange exchange) {
    return Mono.just(ok(assembler.toListModel(pService.getAllProducts(), exchange)));
  }
}
