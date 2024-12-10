package com.packt.modern.api.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("ecomm.card")
public class CardEntity {
  @Id
  @Column("id")
  private UUID id;

  @Column("number")
  private String number;

  @Column("expires")
  private String expires;

  @Column("cvv")
  private String cvv;

  private UserEntity user;

  private List<OrderEntity> orders;

  public UUID getId() {
    return id;
  }

  public CardEntity setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getNumber() {
    return number;
  }

  public CardEntity setNumber(String number) {
    this.number = number;
    return this;
  }

  public String getExpires() {
    return expires;
  }

  public CardEntity setExpires(String expires) {
    this.expires = expires;
    return this;
  }

  public String getCvv() {
    return cvv;
  }

  public CardEntity setCvv(String cvv) {
    this.cvv = cvv;
    return this;
  }

  public UserEntity getUser() {
    return user;
  }

  public CardEntity setUser(UserEntity user) {
    this.user = user;
    return this;
  }

  public List<OrderEntity> getOrderEntity() {
    return orders;
  }

  public CardEntity setOrderEntity(List<OrderEntity> orders) {
    this.orders = orders;
    return this;
  }
}
