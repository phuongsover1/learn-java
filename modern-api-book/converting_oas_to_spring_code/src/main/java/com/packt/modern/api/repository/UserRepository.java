package com.packt.modern.api.repository;

import com.packt.modern.api.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByUsername(@NotNull(message = "User name is required.") String username);
}
