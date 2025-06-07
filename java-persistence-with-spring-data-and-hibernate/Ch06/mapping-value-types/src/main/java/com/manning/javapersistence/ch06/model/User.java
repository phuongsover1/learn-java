package com.manning.javapersistence.ch06.model;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @org.hibernate.annotations.Nationalized
  private String username;
  // The Address is @Embeddable, no annotation needed here ...
  private Address homeAddress;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
    @AttributeOverride(name = "city.zipcode", column = @Column(name = "BILLING_ZIPCODE")),
    @AttributeOverride(name = "city.country", column = @Column(name = "BILLING_COUNTRY")),
      @AttributeOverride(name = "city.name", column = @Column(name = "BILLING_CITY"))
  })
  private Address billingAddress;

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

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
