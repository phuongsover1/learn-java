package com.packt.modern.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.ShipmentRepository;

import reactor.core.publisher.Mono;

@Service
public class ShipmentServiceImpl implements ShipmentService {
  private final ShipmentRepository sRepo;
  private final OrderRepository oRepo;

  public ShipmentServiceImpl(ShipmentRepository sRepo, OrderRepository oRepo) {
    this.sRepo = sRepo;
    this.oRepo = oRepo;
  }

  @Override
  public Mono<ShipmentEntity> getShipmentByOrderId(
      String id) {
    return oRepo.findById(UUID.fromString(id)).map(OrderEntity::getShipments);
  }
}
