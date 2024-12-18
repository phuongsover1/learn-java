package com.packt.modern.api.service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.model.NewOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderService {

  Mono<OrderEntity> addOrder(@Valid Mono<NewOrder> newOrder);
  Flux<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId);
  Mono<OrderEntity> getByOrderId(@NotNull String id);
  Mono<ShipmentEntity> getShipmentByOrderId(@NotNull String id);
  Mono<OrderEntity> updateMapping(@Valid OrderEntity orderEntity);
}
