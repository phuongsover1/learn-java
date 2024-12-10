package com.packt.modern.api.controllers;

import com.packt.modern.api.OrderApi;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.model.Order;
import com.packt.modern.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;


@RestController
public class OrderController implements OrderApi {
  private final OrderService oService;
  private final OrderRepresentationModelAssembler assembler;

  public OrderController(OrderService oService, OrderRepresentationModelAssembler assembler) {
    this.oService = oService;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<Order> addOrder(NewOrder newOrder) {
    return status(HttpStatus.CREATED).body(oService.addOrder(newOrder).map(assembler::toModel).get());
  }

  @Override
  public ResponseEntity<Order> getByOrderId(String id)  {
    return oService.getByOrderId(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<List<Order>> getOrdersByCustomerId(String customerId)  {
    return  ok(assembler.toModelList(oService.getOrdersByCustomerId(customerId)));
  }
}
