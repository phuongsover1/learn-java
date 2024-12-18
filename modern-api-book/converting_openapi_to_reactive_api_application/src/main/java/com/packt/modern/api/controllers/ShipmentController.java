package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.notFound;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.packt.modern.api.ShipmentApi;
import com.packt.modern.api.hateoas.ShipmentRepresentationModelAssembler;
import com.packt.modern.api.model.Shipment;
import com.packt.modern.api.service.OrderService;
import com.packt.modern.api.service.ShipmentService;

import reactor.core.publisher.Mono;

@RestController
public class ShipmentController implements ShipmentApi {
  private final ShipmentService sService;
  private final ShipmentRepresentationModelAssembler assembler;
  private final OrderService oService;

  public ShipmentController(ShipmentService sService, ShipmentRepresentationModelAssembler assembler, OrderService oService) {
    this.sService = sService;
    this.assembler = assembler;
    this.oService = oService;
  }

@Override
public Mono<ResponseEntity<Shipment>> getShipmentByOrderId(String id, ServerWebExchange exchange) throws Exception {
    return  oService.getShipmentByOrderId(id)
    .map(ship -> assembler.entityToModel(ship, exchange))
    .map(ResponseEntity::ok)
    .defaultIfEmpty(notFound().build());
}
}
