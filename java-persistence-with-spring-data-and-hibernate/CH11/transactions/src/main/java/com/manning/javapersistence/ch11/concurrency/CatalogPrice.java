package com.manning.javapersistence.ch11.concurrency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Simple entity used only to demonstrate non-repeatable reads.
 * Kept separate from Item/Bid/Category/Wallet so existing concurrency tests stay untouched.
 */
@Entity
public class CatalogPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String productName;

  private int price;

  public CatalogPrice() {}

  public CatalogPrice(String productName, int price) {
    this.productName = productName;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
