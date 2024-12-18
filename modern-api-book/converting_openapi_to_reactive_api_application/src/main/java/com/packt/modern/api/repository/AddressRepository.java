package com.packt.modern.api.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.packt.modern.api.entity.AddressEntity;

public interface AddressRepository extends ReactiveCrudRepository<AddressEntity, UUID> {
}
