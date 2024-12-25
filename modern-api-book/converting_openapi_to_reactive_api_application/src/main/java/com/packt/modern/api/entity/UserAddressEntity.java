package com.packt.modern.api.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("ecomm.user_address")
public class UserAddressEntity {
  @Column("user_id")
  private UUID userId;

  @Column("address_id")
  private UUID addressId;

  public UUID getUserId() {
    return userId;
  }

  public UserAddressEntity setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public UUID getAddressId() {
    return addressId;
  }

  public UserAddressEntity setAddressId(UUID addressId) {
    this.addressId = addressId;
    return this;
  }
}
