package com.packt.modern.api.repository;

import com.packt.modern.api.entity.ShipmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ShipmentRepository extends ReactiveCrudRepository<ShipmentEntity, UUID> {
}
