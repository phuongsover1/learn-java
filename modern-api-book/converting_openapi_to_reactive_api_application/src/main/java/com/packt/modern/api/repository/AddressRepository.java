package com.packt.modern.api.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.packt.modern.api.entity.AddressEntity;

import reactor.core.publisher.Mono;

public interface AddressRepository extends ReactiveCrudRepository<AddressEntity, UUID> {
    @Query("insert into ecomm.user_address(user_id, address_id) values (:userId, :addressId)")
    Mono<Void> saveMapping(UUID userId, UUID addressId);

    @Query("delete from ecomm.user_address where user_id = :userId and address_id = :addressId")
    Mono<Void> deleteMapping(UUID userId, UUID addressId);
}
