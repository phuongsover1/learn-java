package com.manning.javapersistence.springdatajpa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class MonetaryAmount implements Serializable {
  private final BigDecimal amount;
  private final Currency currency;

  public MonetaryAmount(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  @Override
  public String toString() {
    return amount + " " + currency;
  }

  public static MonetaryAmount fromString(String s) {
    String[] split = s.split(" ");
    return new MonetaryAmount(new BigDecimal(split[0]), Currency.getInstance(split[1]));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + ((currency == null) ? 0 : currency.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MonetaryAmount other = (MonetaryAmount) obj;
    if (amount == null) {
      if (other.amount != null)
        return false;
    } else if (!amount.equals(other.amount))
      return false;
    if (currency == null) {
      if (other.currency != null)
        return false;
    } else if (!currency.equals(other.currency))
      return false;
    return true;
  }

}
