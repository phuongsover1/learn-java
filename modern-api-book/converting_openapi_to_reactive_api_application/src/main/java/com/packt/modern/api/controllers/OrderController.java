package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.packt.modern.api.OrderApi;
import com.packt.modern.api.hateoas.OrderRepresentationModelAssembler;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.model.Order;
import com.packt.modern.api.service.OrderService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class OrderController implements OrderApi {
  private final OrderService oService;
  private final OrderRepresentationModelAssembler assembler;

  public OrderController(OrderService oService, OrderRepresentationModelAssembler assembler) {
    this.oService = oService;
    this.assembler = assembler;
  }

  @Override
  public Mono<ResponseEntity<Order>> addOrder(@Valid Mono<NewOrder> newOrder, ServerWebExchange exchange) {
    return oService.addOrder(newOrder.cache())
    .zipWhen(x -> oService.updateMapping(x))
    .map(t -> status(HttpStatus.CREATED).body(assembler.entityToModel(t.getT2(), exchange)))
    .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Order>> getByOrderId(String id, ServerWebExchange exchange)  {
    return oService.getByOrderId(id).map(o -> status(HttpStatus.OK).body(assembler.entityToModel(o, exchange)))
    .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<Order>>> getOrdersByCustomerId(String customerId, ServerWebExchange exchange)  {
    return Mono.just(ok(assembler.toListModel(oService.getOrdersByCustomerId(customerId), exchange)));
  }
}
