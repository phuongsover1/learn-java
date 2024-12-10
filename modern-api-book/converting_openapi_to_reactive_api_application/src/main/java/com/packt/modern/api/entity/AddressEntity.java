package com.packt.modern.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("ecomm.address")
public class AddressEntity {
  @Id
  @Column("id")
  private UUID id;

  @Column("number")
  private String number;

  @Column("residency")
  private String residency;

  @Column("street")
  private String street;

  @Column("city")
  private String city;

  @Column("country")
  private String country;

  @Column("pincode")
  private String pincode;


  public UUID getId() {
    return id;
  }

  public AddressEntity setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getNumber() {
    return number;
  }

  public AddressEntity setNumber(String number) {
    this.number = number;
    return this;
  }

  public String getResidency() {
    return residency;
  }

  public AddressEntity setResidency(String residency) {
    this.residency = residency;
    return this;
  }

  public String getStreet() {
    return street;
  }

  public AddressEntity setStreet(String street) {
    this.street = street;
    return this;
  }

  public String getCity() {
    return city;
  }

  public AddressEntity setCity(String city) {
    this.city = city;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public AddressEntity setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getPincode() {
    return pincode;
  }

  public AddressEntity setPincode(String pincode) {
    this.pincode = pincode;
    return this;
  }

}
