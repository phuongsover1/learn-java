package com.packt.modern.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.packt.modern.api.entity.UserAddressEntity;
import reactor.core.publisher.Mono;

public interface UserAddressRepository extends ReactiveCrudRepository<UserAddressEntity, UUID> {
  @Query("delete from ecomm.user_address where user_id = :userId and address_id = :addressId")
  Mono<Void> deleteAddressOfUser(UUID userId, UUID addressId);

  Mono<UserAddressEntity> findByUserIdAndAddressId(UUID userId, UUID addressId);
}
