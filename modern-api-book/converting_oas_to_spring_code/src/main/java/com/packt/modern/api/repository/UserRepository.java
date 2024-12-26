package com.packt.modern.api.repository;

import com.packt.modern.api.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByUsername(@NotNull(message = "User name is required.") String username);

  @Query(
      value =
          "select count(u.*) from ecomm.\"user\" u where u.username = :username or u.email = :email",
      nativeQuery = true)
  Integer findByUsernameOrEmail(
      @NotNull(message = "User name is required.") String username, String email);
}
