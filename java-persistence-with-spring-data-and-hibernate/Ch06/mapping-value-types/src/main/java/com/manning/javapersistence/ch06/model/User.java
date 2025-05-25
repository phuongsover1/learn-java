package com.manning.javapersistence.ch06.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  private String username;
  // The Address is @Embeddable, no annotation needed here ...
  private Address homeAddress;

  public Address getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(Address homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
