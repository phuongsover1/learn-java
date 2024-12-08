package com.packt.modern.api.service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.ShipmentRepository;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShipmentServiceImpl implements ShipmentService {
  private final ShipmentRepository sRepo;
  private final OrderRepository oRepo;

  public ShipmentServiceImpl(ShipmentRepository sRepo, OrderRepository oRepo) {
    this.sRepo = sRepo;
    this.oRepo = oRepo;
  }

  @Override
  public Optional<ShipmentEntity> getShipmentByOrderId(
      String id) {
    return oRepo.findById(UUID.fromString(id)).map(OrderEntity::getShipments);
  }
}
