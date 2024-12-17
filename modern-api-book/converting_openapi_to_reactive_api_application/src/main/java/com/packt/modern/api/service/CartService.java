package com.packt.modern.api.service;

import com.packt.modern.api.model.Item;
import com.packt.modern.api.entity.CartEntity;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CartService {
  Flux<Item> addCartItemsByCustomerId(CartEntity cartEntity, @Valid Mono<Item> item);
  Flux<Item> addOrReplaceItemsByCustomerId(CartEntity customerId, @Valid Mono<Item> item);
  Mono<Void> deleteCart(String customerId, String cartId);
  Mono<Void> deleteItemFromCart(CartEntity cartEntity, String itemId);
  Mono<CartEntity> getCartByCustomerId(String customerId);
}
