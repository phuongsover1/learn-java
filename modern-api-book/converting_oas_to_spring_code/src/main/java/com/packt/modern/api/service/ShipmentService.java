package com.packt.modern.api.service;

import com.packt.modern.api.entity.ShipmentEntity;
import jakarta.validation.constraints.Min;

import java.util.Optional;

public interface ShipmentService {
  Optional<ShipmentEntity> getShipmentByOrderId(String id);
}
