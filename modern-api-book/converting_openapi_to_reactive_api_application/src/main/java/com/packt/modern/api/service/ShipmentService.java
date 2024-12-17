package com.packt.modern.api.service;

import com.packt.modern.api.entity.ShipmentEntity;

import reactor.core.publisher.Mono;

public interface ShipmentService {
  Mono<ShipmentEntity> getShipmentByOrderId(String id);
}
