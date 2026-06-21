package com.manning.javapersistence.ch11.concurrency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Simple entity used only to demonstrate transaction isolation (dirty reads).
 * Kept separate from Item/Bid/Category so existing concurrency tests stay untouched.
 */
@Entity
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String owner;

  private int balance;

  public Wallet() {}

  public Wallet(String owner, int balance) {
    this.owner = owner;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }
}
