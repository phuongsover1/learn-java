package com.manning.javapersistence.ch06.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Item {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @Access(AccessType.PROPERTY)
  @Column(name = "ITEM_NAME")
  private String name;

  @Column(name = "ITEM_DESCRIPTION")
  private String description;

  @Formula("CONCAT(SUBSTR(ITEM_DESCRIPTION, 1, 12), '...')")
  private String shortDescription;

  @Column(name = "IMPERIALWEIGHT")
  @ColumnTransformer(read = "IMPERIALWEIGHT / 2.0", write = "? * 2.0")
  private double metricWeight;

  @CreationTimestamp
  private LocalDate createdOn;

  @UpdateTimestamp
  private LocalDateTime lastModified;

  @Column(insertable = false) // will always get the default value
  @ColumnDefault("1.00")
  @Generated(org.hibernate.annotations.GenerationTime.INSERT) // auto generated this value when inserting first time,
                                                              // and refresh this property value after inserting
  private BigDecimal initialPrice;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AuctionType auctionType = AuctionType.HIGHEST_BID;

  @Lob
  @NotNull
  @NotEmpty
  private java.sql.Blob imageblob;

  @Lob
  private java.sql.Clob imageDescription;

  public java.sql.Blob getImageblob() {
    return imageblob;
  }

  public void setImageblob(java.sql.Blob imageblob) {
    this.imageblob = imageblob;
  }

  public java.sql.Clob getImageDescription() {
    return imageDescription;
  }

  public void setImageDescription(java.sql.Clob imageDescription) {
    this.imageDescription = imageDescription;
  }

  public AuctionType getAuctionType() {
    return auctionType;
  }

  public void setAuctionType(AuctionType auctionType) {
    this.auctionType = auctionType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = !name.startsWith("AUCTION: ") ? "AUCTION: " + name : name;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getMetricWeight() {
    return metricWeight;
  }

  public void setMetricWeight(double metricWeight) {
    this.metricWeight = metricWeight;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public BigDecimal getInitialPrice() {
    return initialPrice;
  }

  public Long getId() {
    return id;
  }
}
