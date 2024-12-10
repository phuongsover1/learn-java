package com.packt.modern.api.controllers;

import com.packt.modern.api.CartApi;
import com.packt.modern.api.model.Cart;
import com.packt.modern.api.model.Item;
import com.packt.modern.api.service.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

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
  public ResponseEntity<List<Item>> addCartItemsByCustomerId(String customerId, @Valid Item item) {
    log.info("Request for customer ID: {}\nItem: {}", customerId, item);
    return ok(cService.addCartItemsByCustomerId(customerId, item));
  }

  @Override
  public ResponseEntity<Cart> getCartByCustomerId(String customerId) {
    return ok(assembler.toModel(cService.getCartByCustomerId(customerId)));
  }

  @Override
  public ResponseEntity<List<Item>> addOrReplaceItemsByCustomerId(String customerId, Item item) {
    return ok(cService.addOrReplaceItemsByCustomerId(customerId, item));
  }

  @Override
  public ResponseEntity<Void> deleteCart(String customerId) {
    cService.deleteCart(customerId);
    return accepted().build();
  }

  @Override
  public ResponseEntity<Void> deleteItemFromCart(String customerId, String itemId) {
    cService.deleteItemFromCart(customerId, itemId);
    return accepted().build();
  }

  @Override
  public ResponseEntity<List<Item>> getCartItemsByCustomerId(String customerId) {
   return ok(cService.getCartItemsByCustomerId(customerId));
  }

  @Override
  public ResponseEntity<Item> getCartItemsByItemId(String customerId, String itemId) {
    return ok(cService.getCartItemsByItemId(customerId, itemId));
  }
}
