package com.manning.javapersistence.ch06.model;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

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
}
