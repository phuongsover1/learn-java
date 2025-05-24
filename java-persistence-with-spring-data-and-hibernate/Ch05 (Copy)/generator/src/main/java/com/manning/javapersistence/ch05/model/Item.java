package com.manning.javapersistence.ch05.model;

import javax.persistence.Entity;
import javax.validation.constraints.Future;
import java.util.Date;

@Entity
public class Item {
  private Long id;
  private String name;
  @Future
  protected Date auctionEnd;

  public Long getId() { // Optional but useful
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getAuctionEnd() {
    return auctionEnd;
  }

  public void setAuctionEnd(Date auctionEnd) {
    this.auctionEnd = auctionEnd;
  }
}
