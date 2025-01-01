package com.packt.modern.api.repository;

import com.packt.modern.api.entity.UserTokenEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserTokenRepository extends ReactiveCrudRepository<UserTokenEntity, UUID> {
  Mono<UserTokenEntity> findByRefreshToken(@NotNull(message = "refresh token is required") String refreshToken);

  Mono<UserTokenEntity> findByUserId(@NotNull(message = "user identifier is required") UUID userId);

  Mono<Void> deleteByUserId(@NotNull(message = "user identifier is required") UUID userId);

}
