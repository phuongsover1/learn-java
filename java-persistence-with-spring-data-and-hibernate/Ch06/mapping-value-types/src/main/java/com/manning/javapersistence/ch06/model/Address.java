package com.manning.javapersistence.ch06.model;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class Address {
  @Column(nullable = false)
  private String street;

  @AttributeOverride(name = "name", column = @Column(name = "CITY", nullable = false))
  private City city;

  public Address() {}

  public Address(String street, City city) {
    this.street = street;
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }


  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(street, address.street) && Objects.equals(city, address.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, city);
  }
}
