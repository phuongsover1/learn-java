package com.packt.modern.api.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "ecomm.cart")
public class CartEntity {
  @Id
  @Column("id")
  private UUID id;

  private UserEntity user;

  private List<ItemEntity> items = new ArrayList<>();

  public UUID getId() {
    return id;
  }

  public CartEntity setId(UUID id) {
    this.id = id;
    return this;
  }

  public UserEntity getUser() {
    return user;
  }

  public CartEntity setUser(UserEntity user) {
    this.user = user;
    return this;
  }

  public List<ItemEntity> getItems() {
    return items;
  }

  public CartEntity setItems(List<ItemEntity> items) {
    this.items = items;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartEntity that = (CartEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(items, that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, items);
  }
}
