package com.packt.modern.api.entity;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ecomm.user_token")
public class UserTokenEntity {

  @Id
  @Column("id")
  private UUID id;

  @NotNull(message = "refresh token is required")
  @Column("refresh_token")
  private String refreshToken;

  @Column("user_id")
  @NotNull(message = "user identifier is required")
  private UUID userId;

  public UUID getId() {
    return id;
  }

  public UserTokenEntity setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public UserTokenEntity setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public UUID getUserId() {
    return userId;
  }

  public UserTokenEntity setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }
}
