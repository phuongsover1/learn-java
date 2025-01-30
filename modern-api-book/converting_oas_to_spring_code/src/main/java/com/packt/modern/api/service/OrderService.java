package com.packt.modern.api.service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.NewOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderService {

  Optional<OrderEntity> addOrder(@Valid NewOrder newOrder);
  Iterable<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId);
  Optional<OrderEntity> getByOrderId(@NotNull String id);
  Optional<ShipmentEntity> getShipmentByOrderId(@NotNull String id);
  Iterable<OrderEntity> searchUsersOrders();
}
