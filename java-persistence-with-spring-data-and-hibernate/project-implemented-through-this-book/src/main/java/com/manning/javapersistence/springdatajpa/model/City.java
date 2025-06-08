package com.manning.javapersistence.springdatajpa.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class City {
  @Nonnull
  @Column(nullable = false)
  private String zipcode;

  @Nonnull
  @Column(nullable = false)
  private String country;

  @Nonnull
  @Column(nullable = false)
  private String name;

  public City() {
  }

  public City(String zipcode, String country, String name) {
    this.zipcode = zipcode;
    this.country = country;
    this.name = name;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    City other = (City) obj;
    if (zipcode == null) {
      if (other.zipcode != null)
        return false;
    } else if (!zipcode.equals(other.zipcode))
      return false;
    if (country == null) {
      if (other.country != null)
        return false;
    } else if (!country.equals(other.country))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "City [zipcode=" + zipcode + ", country=" + country + ", name=" + name + "]";
  }

}
