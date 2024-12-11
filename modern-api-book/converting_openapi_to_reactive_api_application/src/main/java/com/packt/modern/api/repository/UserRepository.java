package com.packt.modern.api.repository;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
  @Query("SELECT a.* FROM ecomm.\"user\" u, ecomm.address a, ecomm.user_address ua where u.id = :id and ua.user_id = u.id and ua.address_id = a.id")
  Flux<AddressEntity> getAddressesByCustomerId(UUID id);


  @Query("SELECT c.* FROM ecomm.\"user\" u, ecomm.card c WHERE c.user_id = u.id and u.id = :id")
  Mono<CardEntity> findCardByCustomerId(UUID id);
}
