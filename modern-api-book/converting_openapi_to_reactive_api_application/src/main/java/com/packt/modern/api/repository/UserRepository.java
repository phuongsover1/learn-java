package com.packt.modern.api.repository;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
  @Query(
      "SELECT a.* FROM ecomm.\"user\" u, ecomm.address a, ecomm.user_address ua where u.id = :id and ua.user_id = u.id and ua.address_id = a.id")
  Flux<AddressEntity> getAddressesByCustomerId(UUID id);

  @Query("SELECT c.* FROM ecomm.\"user\" u, ecomm.card c WHERE c.user_id = u.id and u.id = :id")
  Mono<CardEntity> findCardByCustomerId(UUID id);

  Mono<UserEntity> findByUsername(@NotNull(message = "User name is required.") String username);

  @Query("select count(u.*) from ecomm.\"user\" u where u.username = :username or u.email = :email")
  Mono<Integer> findByUsernameOrEmail(
      @NotNull(message = "User name is required.") String username, String email);
}
