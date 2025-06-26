package com.manning.javapersistence.springdatajpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import com.manning.javapersistence.springdatajpa.converter.MonetaryAmountConverter;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Item {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @Access(AccessType.PROPERTY)
  @Column(name = "ITEM_NAME")
  private String name;

  @Nonnull
  @Column(name = "ITEM_DESCRIPTION")
  private String description;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AutionType autionType = AutionType.HIGHEST_BID;

  @Formula("CONCAT(SUBSTR(ITEM_DESCRIPTION, 1, 12), '...')")
  private String shortDescription;

  @Column(name = "IMPERIALWEIGHT")
  @ColumnTransformer(read = "IMPERIALWEIGHT / 2.0", write = "? * 2.0")
  private double metricWeight;

  @CreationTimestamp
  private LocalDate createdOn;

  @UpdateTimestamp
  private LocalDate lastModified;

  @Column(insertable = false)
  @ColumnDefault("1.00")
  @Generated(org.hibernate.annotations.GenerationTime.INSERT)
  private BigDecimal initialPrice;

  @NonNull
  @Convert(converter = MonetaryAmountConverter.class)
  @Column(name = "PRICE", length = 63)
  private MonetaryAmount buyNowPrice;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AutionType getAutionType() {
    return autionType;
  }

  public void setAutionType(AutionType autionType) {
    this.autionType = autionType;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public double getMetricWeight() {
    return metricWeight;
  }

  public void setMetricWeight(double metricWeight) {
    this.metricWeight = metricWeight;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public LocalDate getLastModified() {
    return lastModified;
  }

  public BigDecimal getInitialPrice() {
    return initialPrice;
  }

  public MonetaryAmount getBuyNowPrice() {
    return buyNowPrice;
  }

  public void setBuyNowPrice(MonetaryAmount buyNowPrice) {
    this.buyNowPrice = buyNowPrice;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((autionType == null) ? 0 : autionType.hashCode());
    result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
    long temp;
    temp = Double.doubleToLongBits(metricWeight);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
    result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
    result = prime * result + ((initialPrice == null) ? 0 : initialPrice.hashCode());
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
    Item other = (Item) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (autionType != other.autionType)
      return false;
    if (shortDescription == null) {
      if (other.shortDescription != null)
        return false;
    } else if (!shortDescription.equals(other.shortDescription))
      return false;
    if (Double.doubleToLongBits(metricWeight) != Double.doubleToLongBits(other.metricWeight))
      return false;
    if (createdOn == null) {
      if (other.createdOn != null)
        return false;
    } else if (!createdOn.equals(other.createdOn))
      return false;
    if (lastModified == null) {
      if (other.lastModified != null)
        return false;
    } else if (!lastModified.equals(other.lastModified))
      return false;
    if (initialPrice == null) {
      if (other.initialPrice != null)
        return false;
    } else if (!initialPrice.equals(other.initialPrice))
      return false;
    return true;
  }

}
