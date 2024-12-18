package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.packt.modern.api.CartApi;
import com.packt.modern.api.hateoas.CartRepresentationModelAssembler;
import com.packt.modern.api.model.Cart;
import com.packt.modern.api.model.Item;
import com.packt.modern.api.service.CartService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CartsController implements CartApi {

  private static final Logger log = LoggerFactory.getLogger(CartsController.class);
  private final CartService cService;
  private final CartRepresentationModelAssembler assembler;

  public CartsController(CartService cService, CartRepresentationModelAssembler assembler) {
    this.cService = cService;
    this.assembler = assembler;
  }

  @Override
  public Mono<ResponseEntity<Flux<Item>>> addCartItemsByCustomerId(String customerId, @Valid Mono<Item> item,
      ServerWebExchange exchange) {
    log.info("Request for customer ID: {}\nItem: {}", customerId, item);
    return cService.getCartByCustomerId(customerId)
        .map(cart -> status(HttpStatus.CREATED).body(cService.addCartItemsByCustomerId(cart, item.cache())))
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Cart>> getCartByCustomerId(String customerId, ServerWebExchange exchange) {
    return cService.getCartByCustomerId(customerId)
        .map(c -> ok(assembler.entityToModel(c, exchange)))
        .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<Item>>> addOrReplaceItemsByCustomerId(String customerId, Mono<Item> item,
      ServerWebExchange exchange) {
    return cService.getCartByCustomerId(customerId)
        .map(c -> cService.addOrReplaceItemsByCustomerId(c, item))
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteCart(String customerId, ServerWebExchange exchange) {
    return cService.getCartByCustomerId(customerId)
        .flatMap(c -> cService.deleteCart(customerId, c.getId().toString())
            .then(Mono.just(status(HttpStatus.ACCEPTED).<Void>build())))
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteItemFromCart(String customerId, String itemId, ServerWebExchange exchange) {
    return cService.getCartByCustomerId(customerId)
        .flatMap(c -> cService.deleteItemFromCart(c, itemId)
            .then(Mono.just(status(HttpStatus.ACCEPTED).<Void>build())))
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Flux<Item>>> getCartItemsByCustomerId(String customerId, ServerWebExchange exchange)
      throws Exception {
    return cService.getCartByCustomerId(customerId)
        .map(c -> Flux.fromIterable(assembler.itemFromEntitities(c.getItems())))
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Item>> getCartItemsByItemId(String customerId, String itemId, ServerWebExchange exchange)
      throws Exception {
    return cService.getCartByCustomerId(customerId)
        .map(cart -> assembler.itemFromEntitities(cart.getItems().stream()
            .filter(i -> i.getProductId().toString().equals(itemId.trim())).toList())
            .get(0))
        .map(ResponseEntity::ok)
        .onErrorReturn(notFound().build())
        .switchIfEmpty(Mono.just(notFound().build()));
  }

}
