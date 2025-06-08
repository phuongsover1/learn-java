package com.manning.javapersistence.springdatajpa.model;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Bid {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @NonNull
  private BigDecimal amount;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

}
