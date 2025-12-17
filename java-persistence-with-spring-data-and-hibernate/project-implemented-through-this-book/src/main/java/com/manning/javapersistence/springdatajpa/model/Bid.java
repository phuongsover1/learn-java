package com.manning.javapersistence.springdatajpa.model;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
