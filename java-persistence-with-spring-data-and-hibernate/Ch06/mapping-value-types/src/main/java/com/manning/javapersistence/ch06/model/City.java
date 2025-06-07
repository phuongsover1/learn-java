package com.manning.javapersistence.ch06.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class City {
  @NotNull
  @Column(nullable = false)
  private String zipcode;

  @NotNull
  @Column(nullable = false)
  private String country;
  @NotNull
  @Column(nullable = false)
  private String name;

  public City() {}

  public City(String name, String zipcode, String country) {
    this.name = name;
    this.zipcode = zipcode;
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    City city = (City) o;
    return Objects.equals(zipcode, city.zipcode) && Objects.equals(country, city.country) && Objects.equals(name, city.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zipcode, country, name);
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
