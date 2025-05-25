package com.manning.javapersistence.ch06.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Address {
  @Column(nullable = false)
  private String street;

  @Basic(optional = false)
  private String zipcode;

  @NotNull
  @Column(nullable = false)
  private String city;

  public Address() {}

  public Address(String zipcode, String street, String city) {
    this.zipcode = zipcode;
    this.street = street;
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(street, address.street)
        && Objects.equals(zipcode, address.zipcode)
        && Objects.equals(city, address.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, zipcode, city);
  }
}
